package com.dji.bricks;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.soap.Detail;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.android.ddmlib.IDevice;
import com.dji.bricks.tools.ExcelUtils;
import com.dji.bricks.tools.SystemInfoGet;
import com.dji.bricks.tools.TimeUtils;

public class PerformanceWatcher {
	
	private IDevice device;
	
	private ExcelUtils exlUtils;
	private XSSFSheet watchSheet;
	private SystemInfoGet sysInfo;
	private XSSFCellStyle style;
	
	private Map<String, Object[]> sysInfoMap = null;
	private int sysInfoRowNum;
	private int[] pids;
	private String[] networkKey = {"total_skb_rx_bytes", "total_skb_rx_packets", "total_skb_tx_bytes", "total_skb_tx_packets", "rx_tcp_bytes", "rx_tcp_packets", "rx_udp_bytes",
								"rx_udp_packets", "rx_other_bytes", "rx_other_packets", "tx_tcp_bytes", "tx_tcp_packets", "tx_udp_bytes", "tx_udp_packets", "tx_other_bytes", "tx_other_packets"};
	
	public PerformanceWatcher(IDevice device, String pkg, SystemInfoGet sysInfoGet) {
		this.device = device;
		this.sysInfo = sysInfoGet;
		init();
	}
	
	private void init() {
		exlUtils = ExcelUtils.getInstance();
		watchSheet = exlUtils.getWatchSheet("Persistent observation");
		sysInfoRowNum = watchSheet.getLastRowNum();
		sysInfoMap = new TreeMap<>();
		style = exlUtils.getCellAlignCenter(exlUtils.getWorkbookWatch());
		
		if (sysInfoRowNum == 0)
			initRow();
		
		pids = sysInfo.getPids();
	}
	
	private void initRow() {
		Object[] arrayOb = new Object[8];
		arrayOb[0] = "time/type";
		arrayOb[1] = "CPU(Total)";
		arrayOb[2] = "CPU(Process)";
		arrayOb[3] = "Memory";
		arrayOb[4] = "FPS";
		arrayOb[5] = "Power Consume";
		arrayOb[6] = "I/O Speed Top 5";
		arrayOb[7] = "Network Speed";
		
		sysInfoMap.put("0", arrayOb);
		
		watchSheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));
		watchSheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 56));
		watchSheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
		watchSheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
		watchSheet.addMergedRegion(new CellRangeAddress(0, 2, 5, 5));
		watchSheet.addMergedRegion(new CellRangeAddress(0, 2, 6, 6));
		watchSheet.addMergedRegion(new CellRangeAddress(0, 2, 7, 7));
		watchSheet.addMergedRegion(new CellRangeAddress(0, 2, 8, 8));
		watchSheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		watchSheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
		watchSheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
		int colNum = 9;
		while (colNum < 57) {
			watchSheet.addMergedRegion(new CellRangeAddress(1, 1, colNum, colNum + 2));
			colNum += 3;
		}
		
		XSSFRow row1 = watchSheet.createRow(0);
		XSSFRow row2 = watchSheet.createRow(1);
		XSSFRow row3 = watchSheet.createRow(2);
		int cellid1 = 0;
		for (Object obj : arrayOb) {
			if (cellid1 == 2) {
				Cell cell = row1.createCell(cellid1);
				Cell cell22 = row2.createCell(cellid1);
				cell22.setCellValue("Setting");
				Cell cell23 = row2.createCell(cellid1 + 1);
				cell23.setCellValue("Launcher");
				Cell cell24 = row2.createCell(cellid1 + 2);
				cell24.setCellValue("DJI GO");
				cell.setCellValue((String) obj);
				cellid1 += 3;
				cell.setCellStyle(style);
				cell22.setCellStyle(style);
				cell23.setCellStyle(style);
				cell24.setCellStyle(style);
			} else if (cellid1 == 9) {
				int cellid2 = cellid1;
				Cell cell = row1.createCell(cellid1);
				cell.setCellValue((String) obj);
				for (int i=0; i<networkKey.length; i++) {
					Cell cell2i = row2.createCell(cellid2);
					cell2i.setCellValue(networkKey[i]);
					Cell cell31 = row3.createCell(cellid2);
					cell31.setCellValue("wlan");
					Cell cell32 = row3.createCell(cellid2 + 1);
					cell32.setCellValue("usb");
					Cell cell33 = row3.createCell(cellid2 + 2);
					cell33.setCellValue("lo");
					cellid2 += 3;
					cell2i.setCellStyle(style);
					cell31.setCellStyle(style);
					cell32.setCellStyle(style);
					cell33.setCellStyle(style);
				}
				cellid1 += 48;
				cell.setCellStyle(style);
			} else {
				Cell cell = row1.createCell(cellid1);
				cell.setCellValue((String) obj);
				cellid1 ++;
				cell.setCellStyle(style);
			}
//			else if (cellid1 == 8) {
//				Cell cell = row1.createCell(cellid1);
//				Cell cell21 = row2.createCell(cellid1);
//				cell21.setCellValue("Setting");
//				Cell cell31 = row3.createCell(cellid1);
//				cell31.setCellValue("rx");
//				Cell cell32 = row3.createCell(cellid1 + 1);
//				cell32.setCellValue("tx");
//				Cell cell22 = row2.createCell(cellid1 + 2);
//				cell22.setCellValue("Launcher");
//				Cell cell33 = row3.createCell(cellid1 + 2);
//				cell33.setCellValue("rx");
//				Cell cell34 = row3.createCell(cellid1 + 3);
//				cell34.setCellValue("tx");
//				Cell cell23 = row2.createCell(cellid1 + 4);
//				cell23.setCellValue("DJI GO");
//				Cell cell35 = row3.createCell(cellid1 + 4);
//				cell35.setCellValue("rx");
//				Cell cell36 = row3.createCell(cellid1 + 5);
//				cell36.setCellValue("tx");
//				cell.setCellValue((String) obj);
//				cellid1 += 6;
//			} 
		}
		watchSheet.setColumnWidth(8, watchSheet.getColumnWidth(8) * 12);
		sysInfoRowNum = 2;
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
			else if (obj instanceof Float) 
				cell.setCellValue((Float) obj);
			else if (obj instanceof Long)
				cell.setCellValue((Long) obj);
			
			cell.setCellStyle(style);
		}
		exlUtils.updateWatchWorkbook();
	}
	
	private long timeStore = -1;
	private long[] wlanStore = new long[16];
	private long[] usbStore = new long[16];
	private long[] loStore = new long[16];
	private int timeStamp = 0;
	private long currentTime = System.currentTimeMillis();
	public void startWatch() {
		Object[] infoList = new Object[57];
//		infoList[0] = TimeUtils.formatTimeStamp(System.currentTimeMillis());
		infoList[0] = timeStamp;
		float[] totalCpuInfo = sysInfo.getTotalCpu();
		infoList[1] = (float)(Math.round(totalCpuInfo[0]*100)) / 100;
		infoList[2] = (float)(Math.round(sysInfo.getProcessCpu(pids[0], "Setting", Math.round(totalCpuInfo[1]))*100)) / 100;
		infoList[3] = (float)(Math.round(sysInfo.getProcessCpu(pids[1], "Launcher", Math.round(totalCpuInfo[1]))*100)) / 100;
		infoList[4] = (float)(Math.round(sysInfo.getProcessCpu(pids[2], "DJI GO", Math.round(totalCpuInfo[1]))*100)) / 100;
			
//		infoList[5] = sysInfo.getMemory();
		infoList[6] = sysInfo.getFps();
		infoList[7] = sysInfo.getPower();
		infoList[8] = sysInfo.getIO();
		
		ArrayList<long[]> netInfo = sysInfo.getNetwork();
		if (timeStore != -1) {
			long currentTime = System.currentTimeMillis();
			long[] currentWlanStore = netInfo.get(0);
			long[] currentUsbStore = netInfo.get(1);
			long[] currentLoStore = netInfo.get(2);
			long deltaTime = (currentTime - timeStore) / 1000;
			timeStore = currentTime;
			
			for (int i=9; i<57; i++) {
				if (i % 3 == 0) {
					infoList[i] = (currentWlanStore[(i-9) / 3] - wlanStore[(i-9) / 3]) / deltaTime;
				} else if (i % 3 == 1) {
					infoList[i] = (currentUsbStore[(i-10) / 3] - usbStore[(i-10) / 3]) / deltaTime;
				} else if (i % 3 == 2) {
					infoList[i] = (currentLoStore[(i-11) / 3] - loStore[(i-11) / 3]) / deltaTime;
				}
			}
		} else {
			timeStore = System.currentTimeMillis();
			wlanStore = netInfo.get(0);
			usbStore = netInfo.get(1);
			loStore = netInfo.get(2);
		}
		
		timeStamp += (System.currentTimeMillis() - currentTime) / 1000;
		currentTime = System.currentTimeMillis();
		updateRow(infoList);
	}
}
