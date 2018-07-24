package com.dji.bricks.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dji.bricks.MainEntry;

public class ExcelUtils {
	private static final Logger LOG = Logger.getLogger(ExcelUtils.class);
	
	private final String CURRENT_DIR = System.getProperty("user.dir");
	
	private XSSFWorkbook workbook;
	private File xlsFile;
	
	private ExcelUtils() {
		init();
	}
	
	private static class HolderClass {
		private final static ExcelUtils instance = new ExcelUtils();
	}
	
	public static ExcelUtils getInstance() {
		return HolderClass.instance;
	}
	
	private void init() {
		Date now = new Date();
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
		try {
			xlsFile = new File(CURRENT_DIR + File.separator + "performanceIndex" + File.separator + format.format(now) + ".xlsx");
			if (xlsFile.exists()) {
				FileInputStream fip = new FileInputStream(xlsFile);
				workbook = new XSSFWorkbook(fip);
			} else {
				workbook = new XSSFWorkbook();
				xlsFile.getParentFile().mkdirs();
				FileOutputStream out = new FileOutputStream(xlsFile);
				workbook.write(out);
				out.close();
			}
			
		} catch (Exception e) {
			LOG.error(e);
		}  
	}
	
	public XSSFSheet getSheet(String sheetName) {
		XSSFSheet worksheet = null;
		if (workbook.getSheet(sheetName) == null)
			worksheet = workbook.createSheet(sheetName);	
		else
			worksheet = workbook.getSheet(sheetName);
		
		return worksheet;
	}
	
	public synchronized void updateWorkbook() {
		try {
			FileOutputStream out = new FileOutputStream(xlsFile);
			workbook.write(out);
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public XSSFCellStyle setCellAlignCenter() {
		XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
	}
	
	public CellStyle getPercentageStyle() {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setDataFormat(workbook.createDataFormat().getFormat("0.000%"));
		return style;
	}
}
