package com.dji.bricks.backgrounder.execution;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.IDevice;
import com.dji.bricks.backgrounder.AlertListener;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.EventFiringWebDriverFactory;

public class AppiumInit {
	
	public static final int WAIT_TIME = 30;
	public static AndroidDriver driver; 
	
	public static void setUp(IDevice device, String appPackage, String launchActivity) throws Exception {
		//init appium  
        DesiredCapabilities capabilities = new DesiredCapabilities();  
        capabilities.setCapability("deviceName", device.getSerialNumber());  
        capabilities.setCapability("platformName", "Android");  
        capabilities.setCapability("platformVersion", device.getProperty(IDevice.PROP_BUILD_VERSION));  
          
       //configuration
        capabilities.setCapability("appPackage", appPackage);  
        capabilities.setCapability("appActivity", launchActivity);
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("full-reset", false);
        capabilities.setCapability("sessionOverride", true);    //override session everytime， otherwise cannot start a new session second time
        capabilities.setCapability("ignoreUnimportantViews", false);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//        driver = EventFiringWebDriverFactory.getEventFiringWebDriver(driver, new AlertListener());
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
	}
	
	public static void setUp(JSONObject[] devices, String appPackage, String launchActivity, String url) throws Exception {
		
	}
}
