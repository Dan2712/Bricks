package backgrounder.base;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;

public class CusAction {
	 
	private AppiumDriver driver;
	private TouchAction touchAction;
	
	public CusAction(AppiumDriver driver) {
		this.driver = driver;
		touchAction = new TouchAction(driver);
	}
	
	//1.click
	public void click(WebElement ele) {
		ele.click();
	}
	
	//2.long press
	public void longPress(WebElement ele) {
		driver.performTouchAction(touchAction.longPress(ele));
	}
	
	//3.input text
	public void setText(WebElement ele, String text) {
		ele.clear();
		ele.sendKeys(text);
	}
	
	//10.seekbar drag
	public void dragBar(WebElement ele) {
		int xAxisStartPoint = ele.getLocation().x;
		int xAxisEndPoint = ele.getSize().width + xAxisStartPoint;
		int yAxis = ele.getLocation().y;
		
		touchAction.press(xAxisStartPoint, yAxis)
					.waitAction(500)
					.moveTo(xAxisEndPoint + 10, yAxis)
					.release();
		touchAction.perform();
	}
}