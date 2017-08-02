package com.dji.bricks.backgrounder;

import javax.swing.JTextArea;

import com.android.ddmlib.IDevice;

import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.dji.bricks.backgrounder.execution.RunTestCase;

public class ExecutionMain {

	private String pkg = "com.dji.industry.pilot";
	private String startAct = "";
	
	private static final ExecutionMain instance = new ExecutionMain();
    
    private ExecutionMain(){}
    public static ExecutionMain getInstance(){
        return instance;
    }
    
	public static void RunTestCase(String path, JTextArea logText, IDevice device) {
		
		try {
			AppiumInit.setUp(device, "com.dji.industry.pilot", "com.dji.industry.pilot.SplashActivity");
			
			RunTestCase testCase = new RunTestCase(path, 0, AppiumInit.driver, logText, device);
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
