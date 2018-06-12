package com.dji.bricks.backgrounder;

import javax.swing.JTextArea;

import org.openqa.selenium.NoSuchSessionException;

import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.IDevice;
import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.dji.bricks.backgrounder.execution.RunTestCase;

public class ExecutionMain {

	private String pkg = "";
	private String launchActivity = null;
	private String startAct = "";
	private int runMode;
	private IDevice device;
	
	private static final ExecutionMain instance = new ExecutionMain();
    
    public ExecutionMain(){
    	
    }
    
    public ExecutionMain(int runMode, IDevice device) {
    	this.runMode = runMode;
    	this.device = device;
    	try {
			AppiumInit.setUp(device, "com.dpad.launcher", "com.dpad.launcher.Launcher");
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }
    
//    public static ExecutionMain getInstance(){
//        return instance;
//    }
    
	public void runTestCase(JSONArray jsonFile, JTextArea logText, IDevice device, String pkg) {

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
			AppiumInit.setUp(device, pkg, launchActivity);
			RunTestCase testCase = new RunTestCase(jsonFile, 0, AppiumInit.driver, logText, device);
			testCase.run();
			
		} catch (NullPointerException e1) {
			
		} catch (NoSuchSessionException e2) {
			
		} catch (Exception e) {
//			e.printStackTrace();
		}finally {
			try {
				AppiumInit.driver.quit();
			} catch (Exception e1) {
//				e1.printStackTrace();
			}
		}
	}
	
	public void runTestCase(String eleXpath) {
		RunTestCase testCase = new RunTestCase(runMode, AppiumInit.driver, device, eleXpath);
		testCase.run();
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
