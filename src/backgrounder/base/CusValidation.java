package backgrounder.base;

import org.openqa.selenium.WebElement;

import backgrounder.execution.CommonInit;
import io.appium.java_client.AppiumDriver;

public class CusValidation {
	
	private AppiumDriver driver;
	
	public CusValidation(AppiumDriver driver) {
		this.driver = driver;
	}
	
	public Boolean getText(String eleName, int searchMode, String expectText) {
		WebElement ele = new CusElement(CommonInit.WAIT_TIME, driver).explicitlyWait(searchMode, eleName);
		String text = ele.getAttribute("text");
		
		if (text.equals(expectText))
			return true;
		
		return false;
	}
}
