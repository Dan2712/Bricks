package com.dji.bricks.backgrounder.base;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.backgrounder.execution.AppiumInit;

import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class CusAction {
	
//	private String pushCmd = "adb push %s %s";
	private String localPath =  System.getProperty("user.dir") + File.separator + "rm500_0042.bin";
	private String savePath = "/sdcard/rm500_0042.bin";
	 
	private AndroidDriver driver;
	private IDevice device;
	
	public CusAction(AndroidDriver driver, IDevice device) {
		this.driver = driver;
		this.device = device;
	}
	
	//0.click
	public void click(WebElement ele) {
//		for (int i=0; i<Math.random()*6; i++) {
		ele.click();
//		WebElement parent = driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView[@resource-id='com.android.systemui:id/ntf']"));
//		System.out.println(parent.findElements(By.className("android.widget.ImageView")).size());
//		}
	}
	
	//1.long press
	public void longPress(WebElement ele) {
		TouchAction touchAction = new TouchAction(driver);
		driver.performTouchAction(touchAction.longPress(ele));
	}
	
	//2.input text
	public void setText(WebElement ele, String text) {
		try {
	        driver.hideKeyboard();
	    } catch (WebDriverException e) {
	        
	    }
		ele.clear();
		ele.sendKeys(text);
	}
	
	//4.point drag
	public void pointDrag(WebElement ele, Point desPoint) {
		TouchAction touchAction = new TouchAction(driver);
		int xAxisEndPoint = desPoint.x;
		int yAxisEndPoint = desPoint.y;
		
		touchAction.longPress(ele)
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
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
						driver.swipe(centerX, centerY + 80, centerX, centerY - 80, 500);
						break;
					case 1:
						driver.swipe(centerX, centerY - 80, centerX, centerY + 80, 500);
						break;
				}
			}
		}
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	public void swipe(double position, String containerPath, int heading) {
		if (!containerPath.equals("")) {
			WebElement eleContainer = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(containerPath);
			
			Dimension size = eleContainer.getSize();
			int x = size.getWidth();
			int y = size.getHeight();
			
			org.openqa.selenium.Point start = eleContainer.getLocation();
			int startX = start.x;
			int startY = start.y;
			
			int endX = x + startX;
			int endY = y + startY;
			
			double scrollX = (startX + endX) / 2;
			double scrollY_UP = y * position + startY;
			double scrollY_DOWN = endY - y * position;
			
			switch (heading) {
				case 0:
					driver.swipe((int)scrollX, (int)scrollY_UP, (int)scrollX, startY + 5, 500);
					break;
				case 1:
					driver.swipe((int)scrollX, (int)scrollY_DOWN, (int)scrollX, endY - 5, 500);
					break;
				case 2:
					break;
				case 3:
					break;
			}
		} else {
			int x = 1920;
			int y = 1080;
			switch (heading) {
				case 0:
					driver.swipe(x / 2, (int)(y * position), x / 2, 5, 500);
					break;
				case 1:
					driver.swipe(x / 2, (int)((1 - position) * y), x / 2, y - 5, 500);
					break;
			}
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
		TouchAction touchAction = new TouchAction(driver);
		int xAxisStartPoint = ele.getLocation().x;
		int xAxisEndPoint = ele.getSize().width + xAxisStartPoint;
		int yAxis = ele.getLocation().y;
		
		touchAction.press(xAxisStartPoint, yAxis)
					.waitAction(500)
					.moveTo(xAxisEndPoint + 10, yAxis)
					.release();
		touchAction.perform();
	}
	
	// ("new UiScrollable(new UiSelector()).scrollIntoView("+ "new UiSelector().text(\"India\"));")
	//11.spinner select
	public void spinnerSelect(WebElement ele, String option) {
		ele.click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(" + "new UiSelector().text(\"" + option + "\"));")).click();;
	}
	
	//12.tap point
	public void tapPoint(int x, int y) {
		TouchAction touchAction = new TouchAction(driver);
		touchAction.tap(x, y);
		touchAction.perform();
	}
	
	//13.save to tmp store
	public void saveToTmp(WebElement ele, StringBuilder tmpStore) {
		String text = ele.getText();
		tmpStore.delete(0, tmpStore.length()).append(text);
	}
	
	//14.send adb cmd
	public void sendAdb(String cmd) {
		try {
			device.executeShellCommand(cmd, new CollectingOutputReceiver());
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//15.get children view num
	public void getChildViewNum(String parentName, String childClass, StringBuilder tmpStore) {
		driver.getPageSource();
		tmpStore.delete(0, tmpStore.length()).append(driver.findElement(By.name(parentName)).findElements(By.className(childClass)).size());
	}
}
