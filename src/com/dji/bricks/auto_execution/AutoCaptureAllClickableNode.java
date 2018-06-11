package com.dji.bricks.auto_execution;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.mini_decode.MiniCapUtil;

/**
 * @author Dan
 * @Description AutoExec entry
 */

public class AutoCaptureAllClickableNode {
	private static final Logger LOG = Logger.getLogger(AutoCaptureAllClickableNode.class);

	private String MINICAP_TAKESCREENSHOT_COMMAND = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/minicap -P %s@%s/90 -s >%s";
	private String MINICAP_WM_SIZE_COMMAND = "wm size";
	private String ADB_GET_ORIENTATION = "dumpsys display | grep 'mDefaultViewport'";
	
	private IDevice device;
	private String size;

	public AutoCaptureAllClickableNode(IDevice device) {
		super();
		this.device = device;
		init();
	}
	
	private String executeShellCommand(String command) {
		CollectingOutputReceiver receiver = new CollectingOutputReceiver();
		
		try {
			device.executeShellCommand(command, receiver, 0);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
			if (e.getMessage().contains("not found"))
				JOptionPane.showMessageDialog(null,"Device ADB error, please check your device");
		}
		receiver.flush();
		return receiver.getOutput();
	}
	
	private void init() {
		String output = this.executeShellCommand(MINICAP_WM_SIZE_COMMAND);
		size = output.split(":")[1].trim();
	}
	
	private void takeScreenshot() {
		String savePath = "/data/local/tmp/screenshot.jpg";
		String takeScreenShotCommand = String.format(
				MINICAP_TAKESCREENSHOT_COMMAND, size,
				size, savePath);
		String localPath = System.getProperty("user.dir") + "/autoExecCapture/screenshot.jpg";
		try {
			executeShellCommand(takeScreenShotCommand);
			device.pullFile(savePath, localPath);
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
	
	public void runAuto() {
		this.takeScreenshot();
	}
}
