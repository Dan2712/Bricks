package com.dji.bricks;

import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.android.ddmlib.IDevice;
import com.dji.bricks.tools.ExcelUtils;
import com.dji.bricks.tools.SystemInfoGet;
import com.dji.bricks.tools.TimeUtils;

public class PerformanceWatcher {
	
	private IDevice device;
	private String pkg;
	
	private ExcelUtils exlUtils;
	private XSSFSheet watchSheet;
	private SystemInfoGet sysInfo;
	
	private Map<String, Object[]> sysInfoMap = null;
	private int sysInfoRowNum;
	private int pid;
	
	public PerformanceWatcher(IDevice device, String pkg) {
		this.device = device;
		this.pkg = pkg;
		init();
	}
	
	private void init() {
		exlUtils = ExcelUtils.getInstance();
		sysInfo = new SystemInfoGet(device, pkg);
		watchSheet = exlUtils.getWatchSheet("Persistent observation");
		sysInfoRowNum = watchSheet.getLastRowNum();
		sysInfoMap = new TreeMap<>();
		
		if (sysInfoRowNum == 0)
			initRow();
		
		pid = sysInfo.getPid();
	}
	
	private void initRow() {
		Object[] arrayOb = new Object[6];
		arrayOb[0] = "time/type";
		arrayOb[1] = "CPU(Total)";
		arrayOb[2] = "CPU(Process)";
		arrayOb[3] = "Memory";
		arrayOb[4] = "FPS";
		arrayOb[5] = "Power Consume";
		
		sysInfoMap.put("1", arrayOb);
		
		XSSFRow row = watchSheet.createRow(0);
		int cellid = 0;
		for (Object obj : arrayOb) {
			Cell cell = row.createCell(cellid ++);
			cell.setCellValue((String) obj);
		}
		
		exlUtils.updateWatchWorkbook();
	}
	
	private void updateRow(Object[] infoList) {
		sysInfoMap.put(String.valueOf(sysInfoRowNum++), infoList);
		XSSFRow row = watchSheet.createRow(sysInfoRowNum);
		int cellInfoid = 0;
		for (Object obj : infoList) {
			Cell cell = row.createCell(cellInfoid++);
			if (obj instanceof String)
				cell.setCellValue((String) obj);
			else if (obj instanceof Integer)
				cell.setCellValue((Integer) obj);
			else if (obj instanceof Float) {
				cell.setCellValue((Float) obj);
//				cell.setCellStyle(exlUtils.getPercentageStyle());
			}
		}
		exlUtils.updateWatchWorkbook();
	}
	
	public void startWatch() {
		Object[] infoList = new Object[6];
		infoList[0] = TimeUtils.formatTimeStamp(System.currentTimeMillis());
		infoList[1] = (float)(Math.round(sysInfo.getTotalCpu()*100)) / 100;
		infoList[2] = (float)(Math.round(sysInfo.getProcessCpu(pid)*100)) / 100;
//		infoList[3] = sysInfo.getMemory();
		System.out.println("1:"+infoList[1]);
		System.out.println("2:"+infoList[2]);
		infoList[4] = sysInfo.getFps();
		infoList[5] = sysInfo.getPower();
		
		updateRow(infoList);
	}
}
