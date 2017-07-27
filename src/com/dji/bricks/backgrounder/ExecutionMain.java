package com.dji.bricks.backgrounder;

import javax.swing.JTextArea;

import com.android.ddmlib.IDevice;

import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.dji.bricks.backgrounder.execution.RunTestCase;

public class ExecutionMain {

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
	
}
