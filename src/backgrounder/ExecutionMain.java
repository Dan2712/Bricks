package backgrounder;

import backgrounder.execution.AppiumInit;
import backgrounder.execution.RunTestCase;

public class ExecutionMain {

	public void RunTestCase() {
		
		try {
//			AppiumInit.setUp();
			
			RunTestCase testCase = new RunTestCase("/json/case_json.json", 0, AppiumInit.driver);
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
