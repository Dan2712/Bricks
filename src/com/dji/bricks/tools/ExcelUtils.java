package com.dji.bricks.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtils {
	
	public ExcelUtils() {
		
	}
	
	public void readExcel(File file) {
		try {
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook类
			Workbook wb = Workbook.getWorkbook(is);
			// Excel的页签数量
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < sheet_size; index++) {
				// 每个页签创建一个Sheet对象
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()返回该页的总行数
				for (int i = 0; i < sheet.getRows(); i++) {
					// sheet.getColumns()返回该页的总列数
					for (int j = 0; j < sheet.getColumns(); j++) {
						String cellinfo = sheet.getCell(j, i).getContents();
						System.out.println(cellinfo);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readExcelWrite2TXT(File file) {
		// 创建文件输出流
		FileWriter fw = null;
		PrintWriter out = null;
		try {
			// 指定生成txt的文件路径
			String fileName = file.getName().replace(".xls", "");
			fw = new FileWriter(file.getParent() + "/" + fileName + ".txt");
			out = new PrintWriter(fw);
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook类
			Workbook wb = Workbook.getWorkbook(is);
			// Excel的页签数量
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < sheet_size; index++) {
				// 每个页签创建一个Sheet对象
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()返回该页的总行数
				for (int i = 0; i < sheet.getRows(); i++) {
					// sheet.getColumns()返回该页的总列数
					for (int j = 0; j < sheet.getColumns(); j++) {
						String cellinfo = sheet.getCell(j, i).getContents();
						// 将从Excel中读取的数据写入到txt中
						out.println(cellinfo);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 记得关闭流
				out.close();
				fw.close();
				// 由于此处用到了缓冲流，如果数据量过大，不进行flush操作，某些数据将依旧
				// 存在于内从中而不会写入文件，此问题一定要注意
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeExcel(File file) {
		try {  
            // 打开文件  
            WritableWorkbook book = Workbook.createWorkbook(file);  
            // 生成名为“sheet1”的工作表，参数0表示这是第一页  
            WritableSheet sheet = book.createSheet("sheet1", 0);  
            // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0),单元格内容为string  
            Label label = new Label(0, 0, "string");  
            // 将定义好的单元格添加到工作表中  
            sheet.addCell(label);  
            // 生成一个保存数字的单元格,单元格位置是第二列，第一行，单元格的内容为1234.5  
            Number number = new Number(1, 0, 1234.5);  
            sheet.addCell(number);  
            // 生成一个保存日期的单元格，单元格位置是第三列，第一行，单元格的内容为当前日期  
            // 写入数据并关闭文件  
            book.write();  
            book.close();  
        } catch (Exception e) {  
            System.out.println(e);  
        } 
	}
}
