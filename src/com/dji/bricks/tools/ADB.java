package com.dji.bricks.tools;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.SubjectForListener;

/**
*
* @author Dan
*/

public class ADB implements IDeviceChangeListener, SubjectForListener {
	
	private AndroidDebugBridge mAndroidDebugBridge = null;
	private String adbPath = null;
	private String adbPlatformTools = "platform-tools";
	
	public static boolean hasInitAdb = false;
	private List<GlobalObserver> observers = new ArrayList<GlobalObserver>();
	private IDevice[] devices = new IDevice[10];
	
//	public ADB() {
//		init();
//	}
//	
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
					e.printStackTrace();
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
			e.printStackTrace();
		}
		devices[0] = device;
		notifyObservers(devices);
	}

	@Override
	public void deviceDisconnected(IDevice device) {
		System.out.println("disconnect");
		devices[0] = null;
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
	public void notifyObservers(Object obj) {
		if (obj instanceof IDevice[]) {
			IDevice[] devices = (IDevice[]) obj;
			for (GlobalObserver observer : observers) {
				observer.ADBChange(devices);
			}
		}
	}
}
