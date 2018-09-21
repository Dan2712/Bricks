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
	
	public void setDriver(AndroidDriver driver) {
		this.driver = driver;
	}

	//1.text val
	public boolean getText(String eleName, String expectText) {
		WebElement ele = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(eleName);
		String text = ele.getAttribute("text");
		
		if (text.equals(expectText))
			return true;
		
		return false;
	}
	
	//2.ele val
	public boolean getExactEle(String eleName) {
		WebElement ele = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(eleName);
		
		if (ele != null)
			return true;
		return false;
	}
	
	public int getEleShowNum(String eleName) {
		WebElement ele = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(eleName);
		int num = Integer.parseInt(ele.getAttribute("text"));
		
		return num;
	}
	
	//4.tmp val
	public boolean checkTmp(String eleName, StringBuilder tmpStore) {
		WebElement ele = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(eleName);
		String text = ele.getAttribute("text");
		if (text.equals(tmpStore.toString()))
			return true;
		
		return false;
	}
}
