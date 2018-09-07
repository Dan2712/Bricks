package com.dji.bricks.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	private static final Logger LOG = Logger.getLogger(ExcelUtils.class);
	
	private final String CURRENT_DIR = System.getProperty("user.dir");
	
	private XSSFWorkbook workbookResult;
	private XSSFWorkbook workbookWatch;

	private File xlsFileResult;
	private File xlsFileWatch;
	
	private ExcelUtils() {
		initResultXls();
		initWatchXls();
	}
	
	private static class HolderClass {
		private final static ExcelUtils instance = new ExcelUtils();
	}
	
	public static ExcelUtils getInstance() {
		return HolderClass.instance;
	}
	
	private void initWatchXls() {
		Date now = new Date();
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
		try {
			xlsFileWatch = new File(CURRENT_DIR + File.separator + "performanceIndex" + File.separator + format.format(now) + "-Watch.xlsx");
			if (xlsFileWatch.exists()) {
				FileInputStream fip = new FileInputStream(xlsFileWatch);
				workbookWatch = new XSSFWorkbook(fip);
			} else {
				workbookWatch = new XSSFWorkbook();
				xlsFileWatch.getParentFile().mkdirs();
				FileOutputStream out = new FileOutputStream(xlsFileWatch);
				workbookWatch.write(out);
				out.close();
			}
			
		} catch (Exception e) {
			LOG.error(e);
		}  
	}
	
	private void initResultXls() {
		Date now = new Date();
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
		try {
			xlsFileResult = new File(CURRENT_DIR + File.separator + "performanceIndex" + File.separator + format.format(now) + "-Result.xlsx");
			if (xlsFileResult.exists()) {
				FileInputStream fip = new FileInputStream(xlsFileResult);
				workbookResult = new XSSFWorkbook(fip);
			} else {
				workbookResult = new XSSFWorkbook();
				xlsFileResult.getParentFile().mkdirs();
				FileOutputStream out = new FileOutputStream(xlsFileResult);
				workbookResult.write(out);
				out.close();
			}
			
		} catch (Exception e) {
			LOG.error(e);
		}  
	}
	
	public XSSFWorkbook getWorkbookWatch() {
		return workbookWatch;
	}
	
	public XSSFWorkbook getWorkbookResult() {
		return workbookResult;
	}
	
	public XSSFSheet getResultSheet(String sheetName) {
		XSSFSheet worksheet = null;
		if (workbookResult.getSheet(sheetName) == null)
			worksheet = workbookResult.createSheet(sheetName);	
		else
			worksheet = workbookResult.getSheet(sheetName);
		
		return worksheet;
	}
	
	public synchronized void updateResultWorkbook() {
		System.out.println(Thread.currentThread());
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(xlsFileResult);
			workbookResult.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public XSSFSheet getWatchSheet(String sheetName) {
		XSSFSheet worksheet = null;
		if (workbookWatch.getSheet(sheetName) == null)
			worksheet = workbookWatch.createSheet(sheetName);	
		else
			worksheet = workbookWatch.getSheet(sheetName);
		
		return worksheet;
	}
	
	public synchronized void updateWatchWorkbook() {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(xlsFileWatch);
			workbookWatch.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public XSSFCellStyle getCellAlignCenter(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
	}
}
