package com.dji.bricks.backgrounder.execution;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.IDevice;
import io.appium.java_client.AppiumDriver;

public class AppiumInit {
	
	public static final int WAIT_TIME = 30;
	public static AppiumDriver driver; 
	
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
        capabilities.setCapability("sessionOverride", true);    //override session everytime， otherwise cannot start a new session second time 

        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);  
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
	}
	
	public static void setUp(JSONObject[] devices, String appPackage, String launchActivity, String url) throws Exception {
		
	}
}
