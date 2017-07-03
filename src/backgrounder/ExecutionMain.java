package backgrounder;

import javax.swing.JTextArea;

import backgrounder.execution.AppiumInit;
import backgrounder.execution.RunTestCase;

public class ExecutionMain {

	public static void RunTestCase(String path, JTextArea logText) {
		
		try {
//			AppiumInit.setUp();
			
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
