package backgrounder;

import javax.swing.JTextArea;

import com.android.ddmlib.IDevice;

import backgrounder.execution.AppiumInit;
import backgrounder.execution.RunTestCase;

public class ExecutionMain {

	public static void RunTestCase(String path, JTextArea logText, IDevice device) {
		
		try {
			AppiumInit.setUp(device, "dji.go.v4", "dji.pilot.main.activity.DJILauncherActivity");
			
			RunTestCase testCase = new RunTestCase(path, 0, AppiumInit.driver, logText);
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
