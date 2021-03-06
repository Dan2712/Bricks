package com.dji.bricks.backgrounder.base;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;

public class CusElement {
	
	private int wait_time;
	private AndroidDriver driver;
	private WebElement element;
	
	public CusElement(int wait_time, AndroidDriver driver) {
		this.wait_time = wait_time;
		this.driver = driver;
	}

	public WebElement explicitlyWait(String ele) throws NoSuchElementException{
		WebDriverWait wait = new WebDriverWait(driver, wait_time);
		
		element = wait.until(new ExpectedCondition<WebElement>() {

			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(By.xpath(ele));
			}
		});
		return element;
	}
}
