package backgrounder.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.functions.ExpectedCondition;

public class CusElement {
	
	private int wait_time;
	private AppiumDriver driver;
	private WebElement element;
	
	public CusElement(int wait_time, AppiumDriver driver) {
		this.wait_time = wait_time;
		this.driver = driver;
	}

	public WebElement explicitlyWait(int searchMode, String ele) {
		WebDriverWait wait = new WebDriverWait(driver, wait_time);
		
		if (searchMode == 0) {
			element = wait.until(new ExpectedCondition<WebElement>() {

				@Override
				public WebElement apply(WebDriver d) {
					return d.findElement(By.id(ele));
				}
			});
		} else if (searchMode == 1) {
			element = wait.until(new ExpectedCondition<WebElement>() {

				@Override
				public WebElement apply(WebDriver d) {
					System.out.println(ele);
					return d.findElement(By.xpath(ele));
				}
			});
		}
		
		return element;
	}
}
