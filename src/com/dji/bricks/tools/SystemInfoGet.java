package com.dji.bricks.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;

public class SystemInfoGet {
	private static Logger LOG = Logger.getLogger(SystemInfoGet.class);
	
	private CollectingOutputReceiver singleReceiver;
	private MultiLineReceiver multiReceiver;
	
	private String[] proLines;
	private IDevice device;
	private String pkg;
	
	private int lastTotalIdle = -1;
	private int lastTotalAll = -1;
	private int lastMyAll = -1;
	private int lastTotalASec = -1;
	
	private final static String CURRENT_DIR = System.getProperty("user.dir");
	
	public SystemInfoGet(IDevice device, String pkg) {
		this.device = device;
		this.pkg = pkg;
		init();
	}

	public void init() {
		singleReceiver = new CollectingOutputReceiver();
		
		multiReceiver = new MultiLineReceiver() {
			
			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void processNewLines(String[] lines) {
				// TODO Auto-generated method stub
				if (lines.length == 1) 
					proLines = lines[0].split("\n");
				else
					proLines = lines;
			}
		};
	}
	
	public int getMemory(){
		try {
			device.executeShellCommand("\"su 0 \"procrank | grep \'dji.go.v4\'\"\"", singleReceiver);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			LOG.error(e);
		}
		singleReceiver.flush();
		System.out.println(singleReceiver.getOutput());
		int memValue = Integer.parseInt(singleReceiver.getOutput().split("\\s+")[3]);
		
		return memValue;
	}
	
	public float getTotalCpu(){
		int user, nice, sys, idle, iowait, irq, softirq, all;
		float totalCpuUsage = -1;
		
		BufferedReader reader = null;
		try {
			device.pullFile("/proc/stat", CURRENT_DIR + File.separator + "log" + File.separator + "stat");
			reader = new BufferedReader(new FileReader(CURRENT_DIR + File.separator + "log" + File.separator + "stat"));
			
			String tmpStr = reader.readLine();
			String[] toks = tmpStr.split(" ");
			
			user = Integer.parseInt(toks[2]);
			nice = Integer.parseInt(toks[3]);
			sys = Integer.parseInt(toks[4]);
			idle = Integer.parseInt(toks[5]);
			iowait = Integer.parseInt(toks[6]);
			irq = Integer.parseInt(toks[7]);
			softirq = Integer.parseInt(toks[8]);
			
			all = user + nice + sys + idle + iowait + irq + softirq;
			reader.close();
			
			if (lastTotalAll != -1) {
				totalCpuUsage = (float)((all - idle) - (lastTotalAll - lastTotalIdle)) / (all - lastTotalAll) * 100;
			}
			
			lastTotalASec = all - lastTotalAll;
			lastTotalAll = all;
			lastTotalIdle = idle;
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (SyncException | IOException | AdbCommandRejectedException | TimeoutException e) {
			LOG.error(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}
		
		return totalCpuUsage;
	}
	
	public float getProcessCpu(int pid){
		int utime, stime, cutime, cstime, all;
		float cpuUsage = -1;
		
		BufferedReader reader = null;
		try {
			device.pullFile("/proc/" + pid + "/stat", CURRENT_DIR + File.separator + "log" + File.separator + "stat");
			reader = new BufferedReader(new FileReader(CURRENT_DIR + File.separator + "log" + File.separator + "stat"));
			
			String tmpStr = reader.readLine();
			String[] toks = tmpStr.split(" ");
			
			utime = Integer.parseInt(toks[13]);
			stime = Integer.parseInt(toks[14]);
			cutime = Integer.parseInt(toks[15]);
			cstime = Integer.parseInt(toks[16]);
			
			all = utime + stime + cutime + cstime;
			reader.close();
			
			if (lastTotalASec != -1) {
				cpuUsage = (float)(all - lastMyAll) / lastTotalASec * 100;
			}
			lastMyAll = all;
		} catch (SyncException | IOException | AdbCommandRejectedException | TimeoutException e) {
			LOG.error(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}
		return cpuUsage;
	}
	
	public int getFps() {
		return new GfxAnalyse(device, pkg).getGfxInfo();
	}
	
	public int getPid(){
		proLines = null;
		try {
			device.executeShellCommand("ps | grep " + pkg, multiReceiver);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			LOG.error(e);
		}
		if (proLines.length == 2) {
			if (proLines[0].split("\\s+")[1].equals(proLines[1].split("\\s+")[2]))
				return Integer.parseInt(proLines[0].split("\\s+")[1]);
		} else if (proLines.length == 1) {
			return Integer.parseInt(proLines[0].split("\\s+")[1]);
		}
		
		return 0;
	}
}
