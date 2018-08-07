package com.dji.bricks.backgrounder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.logging.LogEntry;

import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.IDevice;
import com.dji.bricks.MainEntry;
import com.dji.bricks.PerformanceWatcher;
import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.dji.bricks.backgrounder.execution.RunTestCase;

import io.appium.java_client.android.AndroidDriver;

public class ExecutionMain {
	private static final Logger LOG = Logger.getLogger("ExecutionMain.class");
	
	private String pkg = "";
	private String launchActivity = null;
	private String startAct = "";
	private PrintWriter log_file_writer;
	private boolean isRunning = false;
	private AndroidDriver driver;
	
	private static final ExecutionMain instance = new ExecutionMain();
    
    private ExecutionMain(){}
    
    public static ExecutionMain getInstance(){
        return instance;
    }
    
	public void RunTestCase(JSONArray jsonFile, JTextArea logText, IDevice device, String pkg, String caseName) {

		this.pkg = pkg;
		isRunning = true;
		
		switch (pkg) {
			case "com.dji.industry.pilot":
				launchActivity = "com.dji.industry.pilot.SplashActivity";
				break;
			case "dji.pilot":
				launchActivity = "dji.pilot.home.cs.activity.DJICsMainActivity";
				break;
			case "dji.go.v4":
				launchActivity = "dji.pilot.main.activity.DJILauncherActivity";
				break;
			case "com.dpad.launcher":
				launchActivity = "com.dpad.launcher.Launcher";
				break;
			case "dji.prof.mg": case "dji.prof.args.tiny":
				launchActivity = "dji.prof.mg.main.SplashActivity";
				break;
			case "dji.pilot.pad":
				launchActivity = "dji.pilot.main.activity.DJILauncherActivity";
				break;
			case "com.android.settings":
				launchActivity = "com.android.settings.Settings";
				break;
		}
		
		if (caseName == null)
			caseName = "tmpTest";
		
		try {
			driver = AppiumInit.getInstance(device, pkg, launchActivity).getDriver();
			
			PerformanceWatcher watcher = new PerformanceWatcher(device, pkg);
			MainEntry.cachedThreadPool.submit(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (isRunning) {
						watcher.startWatch();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
			RunTestCase testCase = new RunTestCase(jsonFile, 0, driver, logText, device, pkg, caseName);
			testCase.run();
		} catch (NullPointerException e1) {
			LOG.error(e1);
		} catch (NoSuchSessionException e2) {
			LOG.error(e2);
		} catch (Exception e) {
			LOG.error(e);
		}finally {
			try {
				isRunning = false;
				saveLog(caseName);
				log_file_writer.close();
//				driver.quit();
			} catch (Exception e1) {
				LOG.error(e1);
			}
		}
	}
	
	private void saveLog(String caseName) throws FileNotFoundException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	    Date date = Calendar.getInstance().getTime();
	    String reportDate = df.format(date);
	    
	    String logPath = System.getProperty("user.dir") + File.separator + "log" + File.separator + "AppiumLog_";
	    LOG.info(driver.getSessionId() + ": Saving device log...");
	    List<LogEntry> logEntries = driver.manage().logs().get("logcat").filter(Level.CONFIG);
	    File logFile = null;
    	logFile = new File(logPath + reportDate + "_" + caseName + ".log");
	    
	    if (!logFile.exists())
	    	logFile.getParentFile().mkdirs();
	    
		log_file_writer = new PrintWriter(logFile);
		for (int i=0; i<logEntries.size(); i++)
			log_file_writer.println(logEntries.get(i));
	    log_file_writer.flush();
	    LOG.info(driver.getSessionId() + ": Saving device log - Done.");
	}
	
	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getStartAct() {
		return startAct;
	}

	public void setStartAct(String startAct) {
		this.startAct = startAct;
	}

}
