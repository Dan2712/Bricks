package com.dji.bricks.tools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Sleeper;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.MainEntry;
import com.dji.bricks.PerformanceWatcher;
import com.dji.bricks.SubjectForListener;
import com.gargoylesoftware.htmlunit.javascript.host.performance.Performance;

/**
*
* @author Dan
*/

public class DeviceConnection implements IDeviceChangeListener, SubjectForListener {
	private static Logger LOG = Logger.getLogger(DeviceConnection.class);
	
	private AndroidDebugBridge mAndroidDebugBridge = null;
	private String adbPath = null;
	private String adbPlatformTools = "platform-tools";
	
	public static boolean hasInitAdb = false;
	private List<GlobalObserver> observers = new ArrayList<GlobalObserver>();
	private IDevice[] devices = new IDevice[10];
	
	//get adb tools path
	private String getAdbPath() {
		if (adbPath == null) {
			adbPath = System.getenv("ANDROID_HOME");
			
			if (adbPath != null) {
				adbPath += File.separator + adbPlatformTools;
			} else 
				return null;
		}
		
		adbPath += File.separator + "adb";
		return adbPath;
	}
	
	//init adb
	public void init() {
		boolean connect_success = false;
		if (!hasInitAdb) {
			String adbPath = getAdbPath();
			
			if (adbPath != null) {
				AndroidDebugBridge.init(false);
				mAndroidDebugBridge = AndroidDebugBridge.createBridge(adbPath, true);
				mAndroidDebugBridge.addDeviceChangeListener(this);
				if (mAndroidDebugBridge != null) {
					hasInitAdb = true;
					connect_success = true;
				}
			}
		}
		
		if(hasInitAdb) {
			int loop_count = 0;
			while (mAndroidDebugBridge.hasInitialDeviceList() == false) {
				try {
					Thread.sleep(100);
					loop_count ++;
				} catch (InterruptedException e) {
					LOG.error(e);
				}
				if (loop_count > 100) {
					connect_success = false;
					break;
				}
			}	
		}
	}

	@Override
	public void deviceChanged(IDevice arg0, int arg1) {
	}

	@Override
	public void deviceConnected(IDevice device) {
		System.out.println("connect");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
		devices[0] = device;
		notifyObservers(devices);
	}

	@Override
	public void deviceDisconnected(IDevice device) {
		System.out.println("disconnect");
		devices[0] = null;
		notifyObservers(devices);
	}

	@Override
	public void registerObserver(GlobalObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(GlobalObserver o) {
		int index = observers.indexOf(o);
		if (index != -1) {
			observers.remove(o);
		}
	}

	@Override
	public synchronized void notifyObservers(Object obj) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
		if (obj instanceof IDevice[]) {
			IDevice[] devices = (IDevice[]) obj;
			MainEntry.cachedThreadPool.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
//SystemInfoGet sysinfo = new SystemInfoGet(devices[0], "dji.go.v4");
//int pid = sysinfo.getPid();
//System.out.println(sysinfo.getTotalCpu());
//System.out.println(sysinfo.getProcessCpu(pid));
////System.out.println(sysinfo.getMemory());
//System.out.println(sysinfo.getFps());
//System.out.println("1------");
//System.out.println(sysinfo.getTotalCpu());
//System.out.println(sysinfo.getProcessCpu(pid));
////System.out.println(sysinfo.getMemory());
//System.out.println(sysinfo.getFps());
//System.out.println("2------");
//System.out.println(sysinfo.getTotalCpu());
//System.out.println(sysinfo.getProcessCpu(pid));
////System.out.println(sysinfo.getMemory());
//System.out.println(sysinfo.getFps());
//System.out.println("3------");
//System.out.println(sysinfo.getTotalCpu());
//System.out.println(sysinfo.getProcessCpu(pid));
////System.out.println(sysinfo.getMemory());
//System.out.println(sysinfo.getFps());
//System.out.println("4------");
//System.out.println(sysinfo.getTotalCpu());
//System.out.println(sysinfo.getProcessCpu(pid));
////System.out.println(sysinfo.getMemory());
//System.out.println(sysinfo.getFps());
//System.out.println("5------");
PerformanceWatcher watcher = new PerformanceWatcher(devices[0], "dji.go.v4");
MainEntry.cachedThreadPool.submit(new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			watcher.startWatch();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
});
						IDevice device = devices[0];
						String appPath = System.getProperty("user.dir") + "/lib/app-debug.apk";
						String testPath = System.getProperty("user.dir") + "/lib/app-debug-androidTest.apk";
						device.pushFile(appPath, "/data/local/tmp/dan.dji.com.dumpxml");
						device.pushFile(testPath, "/data/local/tmp/dan.dji.com.dumpxml.test");
						
						boolean isAppInstall = false;
						boolean isTestInstall = false;
						CollectingOutputReceiver appReceiver = new CollectingOutputReceiver();
						device.executeShellCommand("pm list packages dan.dji.com.dumpxml", appReceiver);
						appReceiver.flush();
						CollectingOutputReceiver testReceiver = new CollectingOutputReceiver();
						device.executeShellCommand("pm list packages dan.dji.com.dumpxml.test", testReceiver);
						testReceiver.flush();
						
						if (!appReceiver.getOutput().equals(""))
							isAppInstall = true;
						
						if (!testReceiver.getOutput().equals(""))
							isTestInstall = true;
						
						if (!isAppInstall)
							device.executeShellCommand("pm install -t -r \"/data/local/tmp/dan.dji.com.dumpxml\"", new CollectingOutputReceiver(), 40000);
							
						if (!isTestInstall)
							device.executeShellCommand("pm install -t -r \"/data/local/tmp/dan.dji.com.dumpxml.test\"", new CollectingOutputReceiver(), 40000);

					} catch (SyncException | IOException | AdbCommandRejectedException | TimeoutException e) {
						LOG.error(e);
					} catch (ShellCommandUnresponsiveException e) {
						LOG.error(e);
					}
				}
			});
			for (GlobalObserver observer : observers) {
				observer.ADBChange(devices);
			}
		}
	}
}
