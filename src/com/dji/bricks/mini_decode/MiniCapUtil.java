package com.dji.bricks.mini_decode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceUnixSocketNamespace;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.MainEntry;
import com.dji.bricks.SubjectForListener;
import com.dji.bricks.tools.ConstantsUtils;

/**
 * @author Dan
 * @Description receiving image stream from phone, and decode to a image
 */

public class MiniCapUtil implements SubjectForListener{
	private static final Logger LOG = Logger.getLogger(MiniCapUtil.class);
	private ExecutorService cachedThreadPool = null;
	
	public static final String ABIS_ARM64_V8A = "arm64-v8a";
	public static final String ABIS_ARMEABI_V7A = "armeabi-v7a";
	public static final String ABIS_X86 = "x86";
	public static final String ABIS_X86_64 = "x86_64";
	
	private String REMOTE_PATH = "/data/local/tmp";
	private String ABI_COMMAND = "ro.product.cpu.abi";
	private String SDK_COMMAND = "ro.build.version.sdk";
	private String MINICAP_BIN = "minicap";
	private String MINICAP_SO = "minicap.so";
	private String MINICAP_CHMOD_COMMAND = "chmod 777 %s/%s";
	private String MINICAP_WM_SIZE_COMMAND = "wm size";
	private String MINICAP_START_COMMAND = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/minicap -P %s@%s/%s";
	private String MINICAP_TAKESCREENSHOT_COMMAND = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/minicap -P %s@%s/90 -s >%s";
	private String ADB_PULL_COMMAND = "adb -s %s pull %s %s";
	private String ADB_GET_ORIENTATION = "dumpsys display | grep 'mDefaultViewport'";
	private String GET_PID = "ps | grep /data/local/tmp/minicap";
	private String GET_DPI = "getprop ro.sf.lcd_density";
	private String GET_CRYSTAL = "cat /system/build.prop | grep \"product.model\"";
	private String start_command = "";
	
	private Queue<byte[]> dataQueue = new LinkedBlockingQueue<byte[]>();
	private List<GlobalObserver> observers = new ArrayList<GlobalObserver>();
	
	private Banner banner = new Banner();
	private String size;
	private volatile boolean isRunning = false;
	
	private static final int PORT = 1717;
	private IDevice device;
	private Socket socket;
	
	private String PID;
	private int orientation_tag = 0;
	private boolean isPad = false;
	
	private BufferedImage image_tmp = null;
	
	public MiniCapUtil(IDevice device) {
		this.device = device;
		try {
			this.init();
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * start minicap service, and push necessary files
	 * @throws IOException 
	 * @throws ShellCommandUnresponsiveException 
	 * @throws AdbCommandRejectedException 
	 * @throws TimeoutException 
	 */
	private void init() throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
		
		cachedThreadPool = MainEntry.cachedThreadPool;
		String abi = device.getPropertySync(ABI_COMMAND);
		String sdk = device.getPropertySync(SDK_COMMAND);
		File miniCapBin = new File(ConstantsUtils.getMinicapBin(), abi + File.separator + MINICAP_BIN);
		File miniCapSo = new File(ConstantsUtils.getMinicapSo(), "android-" + sdk
				+ File.separator + abi + File.separator + MINICAP_SO);
		
		// push .so and minicap file to specified path
		try {
			//need to debug
			device.pushFile(miniCapBin.getAbsolutePath(), REMOTE_PATH + "/" + MINICAP_BIN);
			device.pushFile(miniCapSo.getAbsolutePath(), REMOTE_PATH + "/" + MINICAP_SO);
			executeShellCommand(String.format(MINICAP_CHMOD_COMMAND,
					REMOTE_PATH, MINICAP_BIN));
			
			// port transmission
			this.device.createForward(PORT, "minicap", DeviceUnixSocketNamespace.ABSTRACT);
			
			// get the screen size
			String output = this.executeShellCommand(MINICAP_WM_SIZE_COMMAND);
			size = output.split(":")[1].trim();
			
			//get the screen orientation
			orientation_tag = dumpsOrientation();
			
			//if the device is a pad
			isPad = isPad();
		} catch (SyncException | IOException | AdbCommandRejectedException | TimeoutException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * judge if the device supported
	 */
	public boolean isSupoort(){
		String supportCommand = String.format("LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/minicap -P %s@%s/0 -t", size,size);
		String output = executeShellCommand(supportCommand);
		if(output.trim().endsWith("OK")){
			return true;
		}
		return false;
	}
	
	/** 
	 * judge if it's a pad 
	 *  
	 * @return 
	 */  
	private boolean isPad() {  
	    int screenWidth = Integer.parseInt(size.split("x")[0]);  
	    int screenHeight = Integer.parseInt(size.split("x")[1]);
	    int dpi = Integer.parseInt(executeShellCommand(GET_DPI).trim());
	    double x = Math.pow(screenWidth / dpi, 2);  
	    double y = Math.pow(screenHeight / dpi, 2); 
	    
	    double screenInches = Math.sqrt(x + y);  
	    // if the screen inches is larger than 6, it's a pad
	    if (screenInches >= 6.0) {  
	        return true;  
	    }  
	    return false;  
	}
	
	private String executeShellCommand(String command) {
		CollectingOutputReceiver receiver = new CollectingOutputReceiver();
		
//		DdmPreferences.setTimeOut(20000);
		try {
			device.executeShellCommand(command, receiver, 0, TimeUnit.SECONDS);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
			if (e.getMessage().contains("not found"))
				JOptionPane.showMessageDialog(null,"Device ADB error, please check your device");
		}
		receiver.flush();
		return receiver.getOutput();
	}
	
	private byte[] subByteArray(byte[] byte1, int start, int end) throws NegativeArraySizeException {
		byte[] byte2 = new byte[0];
		byte2 = new byte[end - start];
		
		System.arraycopy(byte1, start, byte2, 0, end - start);
		return byte2;
	}
	
	private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
	
	/** 
	 * start decoding data from phone, and convert to image
	 */
	public void startScreenListener() {
		isRunning = true;
		Thread frame = new Thread(new DataFrameCollector());
		cachedThreadPool.submit(frame);
		Thread convert = new Thread(new ImageConvert());
		cachedThreadPool.submit(convert);
	}

	/**
	 * stop listener, and restart service
	 */
	public void stopScreenListener() {
		isRunning = false;
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dataQueue.clear();
		executeShellCommand("kill " + PID);
	}

	/**
	 * deprecated. conflict on windows
	 */
	public void takeScreenShotOnce() {
		String savePath = "/data/local/tmp/screenshot.jpg";
		String takeScreenShotCommand = String.format(
				MINICAP_TAKESCREENSHOT_COMMAND, size,
				size, savePath);
		String localPath = System.getProperty("user.dir") + "/screenshot.jpg";
		String pullCommand = String.format(ADB_PULL_COMMAND,
				device.getSerialNumber(), savePath, localPath);
		try {
			executeShellCommand(takeScreenShotCommand);
			device.pullFile(savePath, localPath);
			Runtime.getRuntime().exec(pullCommand);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SyncException e) {
			e.printStackTrace();
		} catch (AdbCommandRejectedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * get the orientation of current screen
	 * 
	 * @return 0 or 90 represented the angle
	 */
	private int dumpsOrientation() {
		int real_ori = -1;
		try {
			String output = this.executeShellCommand(ADB_GET_ORIENTATION);
			real_ori = Character.getNumericValue(output.charAt(72));
		} catch (IndexOutOfBoundsException e) {
			LOG.error("Get screen error");
		}
		
		switch (real_ori) {
		case 0:
			return 0;
		case 1:
			return 90;
		}
		return 0;
	}
	
	/**
	 * generate the image from the byte[]
	 * 
	 * @param byte[]
	 * @return bufferedimage
	 */
	private BufferedImage createImage(byte[] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data); 
		try {
			image_tmp = ImageIO.read(bais);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.info("image created");
		try {
			bais.close();
		} catch (IOException e) {
			LOG.error("image create failed: " + e);
		}
		return image_tmp;
	}
	
	/**
	 * Thread class that collect data
	 * 
	 * @author Dan
	 */
	class DataFrameCollector implements Runnable {

		private InputStream input = null;
		
		@Override
		public void run() {
			LOG.debug("start receiving data");
			
			try {
				if (isPad && !executeShellCommand(GET_CRYSTAL).substring(17, 23).equals("ZS600B"))
					start_command = String.format(MINICAP_START_COMMAND, size, size, 90);
				else
					start_command = String.format(MINICAP_START_COMMAND, size, size, orientation_tag);
				
				// start the minicap in background
				cachedThreadPool.submit(new Runnable() {
					
					@Override
					public void run() {
						LOG.info("minicap start: " + start_command);
						executeShellCommand(start_command);
					}
				});
				
				Thread.sleep(1000);
				
				PID = executeShellCommand(GET_PID).substring(10, 15);
				
				socket = new Socket("localhost", PORT);
				input = socket.getInputStream();
				
				int len = 4096;
				while(isRunning) {
					byte[] buffer = new byte[len];
					int realLen = input.read(buffer);
					if (realLen > 0) {
						if (buffer.length != realLen) {
							buffer = subByteArray(buffer, 0, realLen);
						}
						dataQueue.add(buffer);
					} else {
						isRunning = false;
						dataQueue.clear();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NegativeArraySizeException e) {
				e.printStackTrace();
				LOG.error("No data transmission");
			} catch (StringIndexOutOfBoundsException e) {
				LOG.error("No suitable .so file");
			} finally {
				if (input != null)
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				if (socket != null && socket.isConnected()) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			LOG.debug("stop receiving data");
		}
	}
	
	/**
	 * Thread class convert data to image byte[]
	 * 
	 * @author Dan
	 */
	class ImageConvert implements Runnable {
		
		private int readBannerBytes = 0;
		private int bannerLength = 2;
		private int readFrameBytes = 0;
		private int frameLength = 0;
		private byte[] frameBody = new byte[0];
		private volatile byte[] finalBytes = null;
		private volatile byte[] imageByte_pre = null;
		
		@Override
		public void run() {
			while(isRunning) {
				if (dataQueue.isEmpty())
					continue;
				
				byte[] buffer = dataQueue.poll();
				int cursor = 0;
				int buf_length = buffer.length;
				while(cursor < buf_length) {
					int read_byte = buffer[cursor] & 0xff;				// convert to byte to int
					if (readBannerBytes < bannerLength) {				// 1st buffer zone, and position 2 is banner length
						cursor = parserBanner(cursor, read_byte);
					} else if (readFrameBytes < 4) {					// 2nd buffer zone, and first 4 bytes represents the size
						frameLength += (read_byte << (readFrameBytes * 8)) >>> 0;
						cursor += 1;
						readFrameBytes += 1;
					} else {
						if (buf_length - cursor >= frameLength) {		// capture the image frame body
							LOG.debug("frameBodyLength = " + frameLength);
							byte[] subByte = subByteArray(buffer, cursor,
									cursor + frameLength);
							frameBody = byteMerger(frameBody, subByte);
							if ((frameBody[0] != -1) || frameBody[1] != -40) {
								LOG.error(String
										.format("Frame body does not start with JPG header"));
								return;
							}
							finalBytes = subByteArray(frameBody,
									0, frameBody.length);
							
							if (imageByte_pre == null || !compareByte(finalBytes, imageByte_pre)) {
								imageByte_pre = finalBytes;
								cachedThreadPool.submit(new Thread(new Runnable() {					// convert to bufferedimage
	
									@Override
									public void run() {
											notifyObservers(createImage(finalBytes));
											image_tmp = null;
									}
								}));
							}
							cursor += frameLength;
							restore();
							
						} else {
//							LOG.debug("frame needed : " + frameLength);
							byte[] subByte = subByteArray(buffer, cursor, buf_length);
							frameBody = byteMerger(frameBody, subByte);
							frameLength -= (buf_length - cursor);
							readFrameBytes += (buf_length - cursor);
							cursor = buf_length;
						}
					}
					
				}
			}
		}
		
		private void restore() {
			frameLength = 0;
			readFrameBytes = 0;
			frameBody = new byte[0];
		}
		
		/**
		 * banner info
		 */
		private int parserBanner(int cursor, int read_byte) {
			switch (readBannerBytes) {
			case 0:
				// version
				banner.setVersion(read_byte);
				break;
			case 1:
				// banner length
				bannerLength = read_byte;
				banner.setLength(read_byte);
				break;
			case 2:
			case 3:
			case 4:
			case 5:
				// pid
				int pid = banner.getPid();
				pid += (read_byte << ((readBannerBytes - 2) * 8)) >>> 0;
				banner.setPid(pid);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				// real width
				int realWidth = banner.getReadWidth();
				realWidth += (read_byte << ((readBannerBytes - 6) * 8)) >>> 0;
				banner.setReadWidth(realWidth);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
				// real height
				int realHeight = banner.getReadHeight();
				realHeight += (read_byte << ((readBannerBytes - 10) * 8)) >>> 0;
				banner.setReadHeight(realHeight);
				break;
			case 14:
			case 15:
			case 16:
			case 17:
				// virtual width
				int virtualWidth = banner.getVirtualWidth();
				virtualWidth += (read_byte << ((readBannerBytes - 14) * 8)) >>> 0;
				banner.setVirtualWidth(virtualWidth);
				break;
			case 18:
			case 19:
			case 20:
			case 21:
				// virtual height
				int virtualHeight = banner.getVirtualHeight();
				virtualHeight += (read_byte << ((readBannerBytes - 18) * 8)) >>> 0;
				banner.setVirtualHeight(virtualHeight);
				break;
			case 22:
				// orientation
				banner.setOrientation(read_byte * 90);
				break;
			case 23:
				// quirks
				banner.setQuirks(read_byte);
				break;
			}

			cursor += 1;
			readBannerBytes += 1;

			if (readBannerBytes == bannerLength) {
				LOG.debug(banner.toString());
			}
			return cursor;
		}
		
	}
	
	private Boolean compareByte(byte[] a, byte[] b) {
		if (a.length != b.length)
			return false;
		
		for (int i=0; i<a.length; i++) {
			if (a[i] != b[i])
				return false;
		}
		
		return true;
	}
	
	public Queue<byte[]> getDataQueue() {
		return dataQueue;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void registerObserver(GlobalObserver o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}
	
	public void removeObserver(GlobalObserver o) {
		// TODO Auto-generated method stub
		int index = observers.indexOf(o);
		if (index != -1) {
			observers.remove(o);
		}
	}
	
	@Override
	public void notifyObservers(Object obj) {
		if (obj instanceof BufferedImage) {
			BufferedImage image = (BufferedImage) obj;
			int orien_real = dumpsOrientation();
			if (orien_real != orientation_tag) {
				orientation_tag = orien_real;
				stopScreenListener();
				startScreenListener();
			}
			for (GlobalObserver observer : observers) {
				observer.frameImageChange(image);
			}
		}
	}
}