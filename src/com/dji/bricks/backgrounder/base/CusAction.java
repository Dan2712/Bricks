package com.dji.bricks.backgrounder.base;

import java.awt.Point;

import org.openqa.selenium.WebElement;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class CusAction {
	 
	private AndroidDriver driver;
	private TouchAction touchAction;
	
	public CusAction(AndroidDriver driver) {
		this.driver = driver;
		touchAction = new TouchAction(driver);
	}
	
	//1.click
	public void click(WebElement ele) {
		for (int i=0; i<Math.random()*6; i++) {
			ele.click();
		}
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
	
	//4.point drag
	public void pointDrag(WebElement ele, Point desPoint) {
		int xAxisEndPoint = desPoint.x;
		int yAxisEndPoint = desPoint.y;
		
		touchAction.longPress(ele)
					.waitAction(3000)
					.moveTo(xAxisEndPoint, yAxisEndPoint)
					.release();
		touchAction.perform();
	}
	
	//5.swipe
	public void swipe(Point startPoint, Point desPoint) {
		touchAction.longPress(startPoint.getLocation().x, startPoint.getLocation().y)
					.waitAction()
					.moveTo(desPoint.getLocation().x, desPoint.getLocation().y)
					.release();
		touchAction.perform();
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
