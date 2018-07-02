package com.dji.bricks.backgrounder.base;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class CusAction {
	
//	private String pushCmd = "adb push %s %s";
	private String localPath =  System.getProperty("user.dir") + File.separator + "rm500_0042.bin";
	private String savePath = "/sdcard/rm500_0042.bin";
	 
	private AndroidDriver driver;
	private TouchAction touchAction;
	private IDevice device;
	
	public CusAction(AndroidDriver driver, IDevice device) {
		this.driver = driver;
		this.device = device;
		touchAction = new TouchAction(driver);
	}
	
	//1.click
	public void click(WebElement ele) {
//		for (int i=0; i<Math.random()*6; i++) {
			ele.click();
//		}
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
	
	//5.push file to device
	public void pushFileToDevice() {
		try {
			device.pushFile(localPath, savePath);
		} catch (TimeoutException | AdbCommandRejectedException | IOException | SyncException e) {
			e.printStackTrace();
		}
	}
	
	//6.adb reboot
	public void reboot() {
		try {
			device.reboot(null);;
		} catch (TimeoutException | AdbCommandRejectedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//7.scroll
	public void swipe(String elePath, String containerPath, int heading) {
		boolean found = false;
		while (!found) {
			try {
				driver.findElement(By.xpath(elePath));
				found = true;
			} catch(NoSuchElementException e) {
				WebElement eleContainer = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(containerPath);
				
				Dimension size = eleContainer.getSize();
				int x = size.getWidth();
				int y = size.getHeight();
				
				org.openqa.selenium.Point start = eleContainer.getLocation();
				int startX = start.x;
				int startY = start.y;
				
				int endX = x + startX;
				int endY = y + startY;
				
				int centerX = (startX + endX) / 2;
				int centerY = (startY + endY) / 2;
				
				switch (heading) {
					case 0:
						driver.swipe(centerX, centerY + 60, centerX, centerY - 60, 500);
						break;
					case 1:
						driver.swipe(centerX, centerY - 60, centerX, centerY + 60, 500);
						break;
				}
			}
		}
	}
	
	public void swipe(double position, String containerPath, int heading) {
//		WebElement eleContainer = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(containerPath);
		MobileElement eleContainer = (MobileElement) driver.findElement(By.xpath(containerPath));
		
		Dimension size = eleContainer.getSize();
		int x = size.getWidth();
		int y = size.getHeight();
		
		org.openqa.selenium.Point start = eleContainer.getLocation();
		int startX = start.x;
		int startY = start.y;
		
		int endX = x + startX;
		int endY = y + startY;
		
		double scrollX = (startX + endX) / 2;
		double scrollY = y * position + startY;
		
		switch (heading) {
			case 0:
				driver.swipe((int)scrollX, (int)scrollY, (int)scrollX, startY + 5, 500);
				break;
			case 1:
				driver.swipe((int)scrollX, (int)scrollY, (int)scrollX, endY - 5, 500);
				break;
		}
	}
	
	//8.Key HOME
	public void keyHOME() {
		driver.pressKeyCode(3);
	}
	
	//9.Key BACK
	public void KeyBACK() {
		driver.pressKeyCode(4);
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
