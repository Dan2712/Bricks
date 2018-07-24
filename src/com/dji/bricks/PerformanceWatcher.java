package com.dji.bricks;

import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
		watchSheet = exlUtils.getSheet("Persistent observation");
		sysInfoRowNum = watchSheet.getLastRowNum();
		sysInfoMap = new TreeMap<>();
		
		if (sysInfoRowNum == 0)
			initRow();
		
		pid = sysInfo.getPid();
	}
	
	private void initRow() {
		Object[] arrayOb = new Object[5];
		arrayOb[0] = "time/type";
		arrayOb[1] = "CPU(Total)";
		arrayOb[2] = "CPU(Process)";
		arrayOb[3] = "Memory";
		arrayOb[4] = "FPS";
		
		sysInfoMap.put("1", arrayOb);
		
		XSSFRow row = watchSheet.createRow(0);
		int cellid = 0;
		for (Object obj : arrayOb) {
			Cell cell = row.createCell(cellid ++);
			cell.setCellValue((String) obj);
		}
		
		exlUtils.updateWorkbook();
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
		exlUtils.updateWorkbook();
	}
	
	public void startWatch() {
		Object[] infoList = new Object[5];
		infoList[0] = TimeUtils.formatTimeStamp(System.currentTimeMillis());
		infoList[1] = (float)(Math.round(sysInfo.getTotalCpu()*100)) / 100;
		infoList[2] = (float)(Math.round(sysInfo.getProcessCpu(pid)*100)) / 100;
//		infoList[3] = sysInfo.getMemory();
//		infoList[1] = sysInfo.getTotalCpu();
//		infoList[2] = sysInfo.getProcessCpu(pid);
		infoList[4] = sysInfo.getFps();
		
		updateRow(infoList);
	}
}
