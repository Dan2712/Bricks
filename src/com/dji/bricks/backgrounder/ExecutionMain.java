package com.dji.bricks.backgrounder;

import javax.swing.JTextArea;

import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.IDevice;
import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.dji.bricks.backgrounder.execution.RunTestCase;

public class ExecutionMain {

	private String pkg = "";
	private String launchActivity = null;
	private String startAct = "";
	
	private static final ExecutionMain instance = new ExecutionMain();
    
    private ExecutionMain(){
    	
    }
    
    public static ExecutionMain getInstance(){
        return instance;
    }
    
	public void RunTestCase(JSONArray jsonFile, JTextArea logText, IDevice device, String pkg) {

		this.pkg = pkg;
		
		switch (pkg) {
			case "com.dji.industry.pilot":
				launchActivity = "com.dji.industry.pilot.SplashActivity";
				break;
			case "dji.pilot":
				launchActivity = "dji.pilot.home.cs.activity.DJICsMainActivity";
				break;
			case "dji.go.v4":
				launchActivity = "dji.pilot.main.activity.DJILauncherActivity";
				break;
			case "com.dpad.launcher":
				launchActivity = "com.dpad.launcher.Launcher";
				break;
		}
		
		try {
//			if (path != null) {
//				if (autoLaunch == true) {
//					AppiumInit.setUp(device, pkg, launchActivity, autoLaunch);
//					
//					RunTestCase testCase = new RunTestCase(path, 0, AppiumInit.driver, logText, device);
//					testCase.run();
//				} else {
//				
//				}
//			} else {
//				if (autoLaunch == true) {
//					
//				} else {
//					AppiumInit.setUp(device, pkg, "", autoLaunch);
//					RunTestCase testCase = new RunTestCase();
//				}
//			}

			AppiumInit.setUp(device, pkg, launchActivity);
			RunTestCase testCase = new RunTestCase(jsonFile, 0, AppiumInit.driver, logText, device);
			testCase.run();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				AppiumInit.driver.quit();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getStartAct() {
		return startAct;
	}

	public void setStartAct(String startAct) {
		this.startAct = startAct;
	}

}
