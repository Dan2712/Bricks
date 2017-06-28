package backgrounder.execution;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;

public class CommonInit {
	
	public static final int WAIT_TIME = 5;
	
	private AppiumDriver driver; 
	private Connection connection;
	private Statement stmt;
	
	public void setUp() throws Exception {
		//init appium  
        DesiredCapabilities capabilities = new DesiredCapabilities();  
        capabilities.setCapability("deviceName","XGC4C16530008053");  
        capabilities.setCapability("platformName","Android");  
        capabilities.setCapability("platformVersion","5.1.1");  
          
       //configuration
        capabilities.setCapability("appPackage", "dji.go.v4");  
        capabilities.setCapability("appActivity", "dji.pilot.main.activity.DJILauncherActivity");  
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("sessionOverride", true);    //override session everytime， otherwise cannot start a new session second time 
//        capabilities.setCapability("unicodeKeyboard", true);
//        capabilities.setCapability("resetKeyboard", false);

        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);  
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        
        //init sqlite
        try {
        	Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public AppiumDriver getDriver() {
		return driver;
	}
	
    public void tearDown() throws Exception {  
        driver.quit();
        try {
        	stmt.close();
            connection.close();
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
	
}
