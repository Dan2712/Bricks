package com.dji.bricks.tools;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

public class GfxAnalyse {
	
	private IDevice device;
	private String appPackage;
	private String[] lineList;
	
	public GfxAnalyse(IDevice device, String appPackage) {
		super();
		this.device = device;
		this.appPackage = appPackage;
	}
	
	public int getGfxInfo() {
		String cmd = String.format("dumpsys gfxinfo %s", appPackage);
		double vsync_sum = 0.0;
		int count = 0;
		
		MultiLineReceiver receiver = new MultiLineReceiver() {
			
			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void processNewLines(String[] line) {
				// TODO Auto-generated method stub
//				receiveLines.append(line[0]);
				if (line[0].equals(""))
					return;
				lineList = line;
			}
		};
		try {
			device.executeShellCommand(cmd, receiver, 10, TimeUnit.SECONDS);
			receiver.flush();
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        LinkedList<DumpItem> items = new LinkedList<DumpItem>();
        Pattern p = Pattern.compile("([0-9,.]{4})(\\s+[0-9,.]{4})(\\s+[0-9,.]{4})(\\s+[0-9,.]{4})");
        
//        String[] lineList = receiveLines.toString().split("\n");
        DecimalFormat df = new DecimalFormat();
        for (int i=0; i<lineList.length; i++) {
        	Matcher m = p.matcher(lineList[i]);
        	while (m.find()) {
                try {
                	double drawValue = df.parse(m.group(0).trim()).doubleValue();
                	double prepareValue = df.parse(m.group(1).trim()).doubleValue();
                	double processValue = df.parse(m.group(2).trim()).doubleValue();
                	double executeValue = df.parse(m.group(3).trim()).doubleValue();
                	
                	vsync_sum += calVsync(drawValue, prepareValue, processValue, executeValue);
                	count ++;
    			} catch (ParseException e) {
    				e.printStackTrace();
    			}
            }
        }
        
        int fps = (int) (count * 60 / (count + vsync_sum));
        return fps;
	}
	
	private double calVsync(double drawValue, double prepareValue, double processValue, double executeValue) {
		double renderTime = drawValue + prepareValue + processValue + executeValue;
		return Math.ceil(renderTime / 16.67 - 1);
	}
	
	class DumpItem {
	    final public double draw;
	    final public double process;
	    final public double execute;

	    public DumpItem(double draw, double process, double execute) {
	        this.draw = draw;
	        this.process = process;
	        this.execute = execute;
	    }

	}
}
