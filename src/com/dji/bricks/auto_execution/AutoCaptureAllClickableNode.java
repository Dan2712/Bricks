package com.dji.bricks.auto_execution;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.MainEntry;
import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.dji.bricks.node_selection.UiAutomatorHelper;
import com.dji.bricks.node_selection.UiAutomatorHelper.UiAutomatorException;
import com.dji.bricks.node_selection.UiAutomatorHelper.UiAutomatorResult;
import com.dji.bricks.node_selection.UiAutomatorModel;
import com.dji.bricks.node_selection.tree.UiNode;
import com.dji.bricks.tools.FileUtils;

/**
 * @author Dan
 * @Description AutoExec entry
 */

public class AutoCaptureAllClickableNode {
	private static final Logger LOG = Logger.getLogger(AutoCaptureAllClickableNode.class);

	private String MINICAP_TAKESCREENSHOT_COMMAND = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/minicap -P %s@%s/%s -s >%s";
	private String MINICAP_WM_SIZE_COMMAND = "wm size";
	private String ADB_GET_ORIENTATION = "dumpsys display | grep 'mDefaultViewport'";
	
	private BufferedImage mScreenshot;
	private UiAutomatorModel mModel;
    private UiAutomatorResult result;
	private IDevice device;
	private String size;
	private ArrayList<UiNode> clickNodeList;

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
		clickNodeList = new ArrayList<>();
	}
	
	private BufferedImage takeScreenshot() {
		String savePath = "/data/local/tmp/screenshot.jpg";
		String takeScreenShotCommand = String.format(
				MINICAP_TAKESCREENSHOT_COMMAND, size,
				size,  dumpsOrientation(), savePath);
		String localPath = System.getProperty("user.dir") + "/autoExecCapture/" + System.currentTimeMillis() + ".jpg";
		File autoExecFile = new File(localPath);
		if (!autoExecFile.getParentFile().exists())
			autoExecFile.getParentFile().mkdirs();
		
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
		
		return FileUtils.getLocalImage(localPath);
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
	
	public ArrayList<UiNode> captureAll() {
		mScreenshot = this.takeScreenshot();
		try {
			mModel = null;
			result = UiAutomatorHelper.takeSnapshot(device, null, false, mScreenshot, AppiumInit.driver);

			if (result != null)
				mModel = result.model;
			
			List<UiNode> tmpList = mModel.getmNodelist();
			System.out.println(tmpList.size());
			for (int i=0; i<tmpList.size(); i++) {
				UiNode node = tmpList.get(i);
				if (node.getAttribute("clickable").equals("true"))
					clickNodeList.add(node);
			}
		} catch (UiAutomatorException e) {
			System.out.println(e);
			LOG.debug("Loading. Current page doesn't contain UI Hierarchy xml.");
		}
		
		if (clickNodeList != null)
			return clickNodeList;
		
		return null;
	}
}
