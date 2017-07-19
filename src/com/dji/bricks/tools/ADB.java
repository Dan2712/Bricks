package com.dji.bricks.tools;
import java.io.File;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.SubjectForListener;

public class ADB implements SubjectForListener {
	
	private AndroidDebugBridge mAndroidDebugBridge = null;
	private String adbPath = null;
	private String adbPlatformTools = "platform-tools";
	
	public static boolean hasInitAdb = false;
	
	public ADB() {
		init();
	}
	
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
	private void init() {
		boolean connect_success = false;
		if (!hasInitAdb) {
			String adbPath = getAdbPath();
			
			if (adbPath != null) {
				AndroidDebugBridge.init(false);
				mAndroidDebugBridge = AndroidDebugBridge.createBridge(adbPath, true);
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
	
	public IDevice[] getDevices() {
		IDevice[] devices = null;
		if (mAndroidDebugBridge != null)
			devices = mAndroidDebugBridge.getDevices();
		
		return devices;
	}

	@Override
	public void registerObserver(GlobalObserver o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObserver(GlobalObserver o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyObservers(Object obj) {
		// TODO Auto-generated method stub
		
	}
}
