package com.dji.bricks.backgrounder.base;

import org.openqa.selenium.WebElement;

import com.dji.bricks.backgrounder.execution.AppiumInit;

import io.appium.java_client.android.AndroidDriver;

public class CusValidation {
	
	private AndroidDriver driver;
	
	private int[] validationList = new int[50];
	
	public CusValidation(AndroidDriver driver) {
		this.driver = driver;
	}
	
	public Boolean getText(String eleName, String expectText) {
		WebElement ele = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(eleName);
		String text = ele.getAttribute("text");
		
		if (text.equals(expectText))
			return true;
		
		return false;
	}
	
	public Boolean getExactEle(String eleName) {
		WebElement ele = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(eleName);
		
		if (ele != null)
			return true;
		return false;
	}
}
