package com.dji.bricks.backgrounder.execution;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.common.events.EventDriverFactory;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.IDevice;
import com.dji.bricks.backgrounder.AlertListener;
import com.dji.bricks.tools.ExcelUtils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.Setting;
import io.appium.java_client.events.EventFiringWebDriverFactory;

public class AppiumInit {
	private static final Logger LOG = Logger.getLogger(AppiumInit.class);
	
	public static final int WAIT_TIME = 30;
	public AndroidDriver driver; 
	private static IDevice device;
	private static String appPackage;
	private static String launchActivity;
	
	private AppiumInit() {
		setUp(device, appPackage, launchActivity);
	}
	
	private static class HolderInit {
		private final static AppiumInit instance = new AppiumInit();
	}
	
	public static AppiumInit getInstance(IDevice device1, String appPackage1, String launchActivity1) {
		device = device1;
		appPackage = appPackage1;
		launchActivity = launchActivity1;
		return HolderInit.instance;
	}
	
	public AndroidDriver getDriver() {
		return driver;
	}
	
	public void setUp(IDevice device, String appPackage, String launchActivity) {
		//init appium  
        DesiredCapabilities capabilities = new DesiredCapabilities();  
        capabilities.setCapability("deviceName", device.getSerialNumber());  
        capabilities.setCapability("platformName", "Android");  
        capabilities.setCapability("platformVersion", device.getProperty(IDevice.PROP_BUILD_VERSION));  
          
       //configuration
        capabilities.setCapability("appPackage", appPackage);  
        capabilities.setCapability("appActivity", launchActivity);
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("full-reset", false);
        capabilities.setCapability("--session-override", true);    //override session everytime， otherwise cannot start a new session second time
        capabilities.setCapability("ignoreUnimportantViews", false);
        capabilities.setCapability("newCommandTimeout", 600);

        try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			LOG.error(e);
		}
//        driver = EventFiringWebDriverFactory.getEventFiringWebDriver(driver, new AlertListener());
//        driver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, Duration.ofMillis(100));
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
	}
	
	public static void setUp(JSONObject[] devices, String appPackage, String launchActivity, String url) throws Exception {
		
	}
}
