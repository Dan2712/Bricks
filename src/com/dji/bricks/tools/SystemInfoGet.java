package com.dji.bricks.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;

public class SystemInfoGet {
	private static Logger LOG = Logger.getLogger(SystemInfoGet.class);
	
	private MultiLineReceiver multiReceiver;
	
	private String[] proLines;
	private IDevice device;
//	private String pkg;
	
	private int lastTotalIdle = -1;
	private int lastTotalAll = -1;
//	private int lastMyAll = -1;
//	private int lastTotalASec = -1;
	private Map<String, Integer> lastMyAllMap;
	private Map<String, Integer> lastIorxMap;
	private Map<String, Integer> lastIotxMap;
	
	private final static String CURRENT_DIR = System.getProperty("user.dir");
	private String statPath = CURRENT_DIR + File.separator + "log" + File.separator + "stat";
	private String[] pkgs = {"com.android.settings", "com.dpad.launcher", "dji.go.v4"};
	
	public SystemInfoGet(IDevice device, String pkg) {
		this.device = device;
//		this.pkg = pkg;
		init();
	}

	public void init() {
		lastMyAllMap = new TreeMap();
		lastMyAllMap.put("Setting", -1);
		lastMyAllMap.put("Launcher", -1);
		lastMyAllMap.put("DJI GO", -1);
		
		lastIorxMap = new TreeMap<>();
		lastIorxMap.put("Setting", -1);
		lastIorxMap.put("Launcher", -1);
		lastIorxMap.put("DJI GO", -1);
		
		lastIotxMap = new TreeMap<>();
		lastIotxMap.put("Setting", -1);
		lastIotxMap.put("Launcher", -1);
		lastIotxMap.put("DJI GO", -1);
		
		multiReceiver = new MultiLineReceiver() {
			
			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void processNewLines(String[] lines) {
				// TODO Auto-generated method stub
				if (lines[0].equals(""))
					return;
				if (lines.length == 1) 
					proLines = lines[0].split("\n");
				else
					proLines = lines;
			}
		};
	}
	
	public synchronized int getMemory(){
		CollectingOutputReceiver singleReceiver = new CollectingOutputReceiver();
		try {
			device.executeShellCommand("\"su 0 \"procrank | grep \'dji.go.v4\'\"\"", singleReceiver);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			LOG.error(e);
		}
		singleReceiver.flush();
		System.out.println(singleReceiver.getOutput());
		int memValue = Integer.parseInt(singleReceiver.getOutput().split("\\s+")[3]);
		
		return memValue;
	}
	
	public synchronized float[] getTotalCpu(){
System.out.println(Thread.currentThread());
		float[] info = new float[2];
		int user, nice, sys, idle, iowait, irq, softirq, all;
		float totalCpuUsage = -1;
		int lastTotalASec = -1;
		
		BufferedReader reader = null;
		try {
			File file = new File(statPath);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			
			device.pullFile("/proc/stat", statPath);
			reader = new BufferedReader(new FileReader(statPath));
			
			String tmpStr = reader.readLine();
			String[] toks = tmpStr.split(" ");
			
			user = Integer.parseInt(toks[2]);
			nice = Integer.parseInt(toks[3]);
			sys = Integer.parseInt(toks[4]);
			idle = Integer.parseInt(toks[5]);
			iowait = Integer.parseInt(toks[6]);
			irq = Integer.parseInt(toks[7]);
			softirq = Integer.parseInt(toks[8]);
			
			all = user + nice + sys + idle + iowait + irq + softirq;
			reader.close();
System.out.println("Tall:"+all+"--"+"Tidle:"+idle+"--"+"Tlasttotalall:"+lastTotalAll+"--"+"Tlasttotalidle:"+lastTotalIdle);
			if (lastTotalAll != -1) {
				totalCpuUsage = (float)((all - idle) - (lastTotalAll - lastTotalIdle)) / (all - lastTotalAll) * 100;
//System.out.println("total cpu:"+totalCpuUsage);
			}
			
			lastTotalASec = all - lastTotalAll;
			lastTotalAll = all;
			lastTotalIdle = idle;
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (SyncException | IOException | AdbCommandRejectedException | TimeoutException e) {
			LOG.error(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}
		
		info[0] = totalCpuUsage;
		info[1] = lastTotalASec;
		return info;
	}
	
	public synchronized float getProcessCpu(int pid, String apk, int lastTotalASec){
		if (pid == 0)
			return 0;
		
		int utime, stime, cutime, cstime, all;
		float cpuUsage = -1;
		
		BufferedReader reader = null;
		
		int count = 0;
		int maxTries = 3;
		boolean stopGet = false;
		while (!stopGet) {
			try {
	//			device.pullFile("/proc/" + pid + "/stat", CURRENT_DIR + File.separator + "stat");
	//			reader = new BufferedReader(new FileReader(CURRENT_DIR + File.separator + "stat"));
				
				proLines = null;
				device.executeShellCommand("cat /proc/" + pid + "/stat", multiReceiver);
				multiReceiver.flush();
				String[] toks = proLines[0].split(" ");
				utime = Integer.parseInt(toks[13]);
				stime = Integer.parseInt(toks[14]);
				cutime = Integer.parseInt(toks[15]);
				cstime = Integer.parseInt(toks[16]);
				all = utime + stime + cutime + cstime;

				if (lastTotalASec != -1) {
					int last = lastMyAllMap.get(apk);
System.out.println("Pkg: " + apk + " Proc all: " + all + " Proc lastTotalASec: " + lastTotalASec + " Proc last: " + last);
					cpuUsage = (float)(all - last) / lastTotalASec * 100;
				}
				lastMyAllMap.put(apk, all);
				stopGet = true;
			} catch (BindException e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (++count == maxTries) {
					stopGet = true;
					e.printStackTrace();
				}
			} catch (IOException | AdbCommandRejectedException | TimeoutException e) {
				e.printStackTrace();
			} catch (ShellCommandUnresponsiveException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
						reader = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return cpuUsage;
	}
	
	public synchronized int getFps() {
		return new GfxAnalyse(device).getGfxInfo();
	}
	
	public synchronized int getPower() {
		CollectingOutputReceiver singleReceiver = new CollectingOutputReceiver();
		try {
			device.executeShellCommand("getprop hw.bat_current", singleReceiver, 5, TimeUnit.SECONDS);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			LOG.error(e);
		}
		singleReceiver.flush();
		
		int power = Math.abs(Integer.parseInt(singleReceiver.getOutput().trim()));
		return power;
	}
	
	public String getIO() {
//		if (pid == 0)
//			return new int[]{0, 0};
//		
//		int periodRead = -1;
//		int periodWrite = -1;
//		CollectingOutputReceiver singleReceiver = new CollectingOutputReceiver();
//		try {
//			device.executeShellCommand("cat /proc/" + pid + "/io", singleReceiver);
//		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
//			e.printStackTrace();
//		}
//		singleReceiver.flush();
//		String[] toks = singleReceiver.getOutput().split("\n");
//		int nowReadbytes = Integer.parseInt(toks[4].split(": ")[1]);
////System.out.println(nowReadbytes);
//		int nowWritebytes = Integer.parseInt(toks[5].split(": ")[1]);
//		if (lastIorxMap.get(apk) != -1 && lastIotxMap.get(apk) != -1) {
//			periodRead = nowReadbytes - lastIorxMap.get(apk);
//			periodWrite = nowWritebytes - lastIotxMap.get(apk);
//		}
//		lastIorxMap.put(apk, nowReadbytes);
//		lastIotxMap.put(apk, nowWritebytes);
//		
//		return new int[]{periodRead, periodWrite};
		CollectingOutputReceiver receiver = new CollectingOutputReceiver();
		try {
			device.executeShellCommand("iotop -m5 -n 1", receiver);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		receiver.flush();
		return receiver.getOutput();
	}
	
	public ArrayList<long[]> getNetwork() {
		CollectingOutputReceiver receiver = new CollectingOutputReceiver();
		try {
			device.executeShellCommand("cat /proc/net/xt_qtaguid/iface_stat_fmt", receiver);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		receiver.flush();
		String[] portOut = receiver.getOutput().split("\n");
		System.out.println(portOut[1]);
		System.out.println(portOut[2]);
		System.out.println(portOut[3]);
		long[] wlanData = new long[16];
		long[] usbData = new long[16];
		long[] loData = new long[16];
		for (int i=1; i<portOut.length; i++) {
			String[] data = portOut[i].split(" ");
			if (data[0].equals("wlan0")) {
				for (int j=1; j<17; j++) 
					wlanData[j-1] = Long.parseLong(data[j]);
			} else if (data[0].equals("usb0")) {
				for (int j=1; j<17; j++) 
					usbData[j-1] = Long.parseLong(data[j]);
			} else if (data[0].equals("lo")) {
				for (int j=1; j<17; j++) 
					loData[j-1] = Long.parseLong(data[j]);
			}
		}
		
		ArrayList<long[]> tmp = new ArrayList<>();
		tmp.add(wlanData);
		tmp.add(usbData);
		tmp.add(loData);
		return tmp;
	}
	
	public synchronized int[] getPids(){
		int[] pids = new int[3];
		proLines = null;
		for (int i=0; i<pids.length; i++) {
			try {
				String exeCmd = "ps | grep " + pkgs[i];
				device.executeShellCommand(exeCmd, multiReceiver);
			} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
				LOG.error(e);
			}
			if (proLines != null) {
				if (proLines.length == 2) {
					if (proLines[0].split("\\s+")[1].equals(proLines[1].split("\\s+")[2]))
						pids[i] = Integer.parseInt(proLines[0].split("\\s+")[1]);
				} else if (proLines.length == 1) {
					pids[i] = Integer.parseInt(proLines[0].split("\\s+")[1]);
				}
			}
		}
		
		return pids;
	}
}
