package backgrounder;

import backgrounder.execution.CommonInit;
import backgrounder.execution.RunTestCase;

public class ExecutionMain {

	public void RunTestCase() {
		
		CommonInit init = new CommonInit();
		try {
			init.setUp();
			
			RunTestCase testCase = new RunTestCase("src/json/case_json.json", 0, init.getDriver());
			testCase.run();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				init.tearDown();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
