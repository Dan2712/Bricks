package com.dji.bricks.backgrounder.execution;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.backgrounder.base.CusAction;
import com.dji.bricks.backgrounder.base.CusElement;
import com.dji.bricks.backgrounder.base.CusValidation;
import com.dji.bricks.tools.ExcelUtils;
import com.dji.bricks.tools.GfxAnalyse;
import com.dji.bricks.tools.TimeUtils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.api.general.AppiumWebDriverEventListener;

public class RunTestCase implements AppiumWebDriverEventListener{
	private static final Logger LOG = Logger.getLogger("RunTestCase.class");
	
	private WebElement ele_sub;
	private String ele_customName;
	private int runMode;
//	private String path;
	private AndroidDriver driver;
	private CusAction action;
	private CusValidation validation;
	private String pkg;
	private JTextArea logText;
	private IDevice device;
	private JSONArray jsonFile;
	private List<WebElement> dragedElement;
	private Boolean isDraged = false;
	private String tmp;
	private String caseName;
	private ExcelUtils exlUtils;
	private GfxAnalyse gfxUtil;
	private CollectingOutputReceiver receiver;
	private MultiLineReceiver mReceiver;
	private String pid;
	private XSSFSheet CPUSheet;
	private XSSFSheet MemSheet;
	private XSSFSheet FPSSheet;
	private XSSFRow CPURow;
	private XSSFRow MemRow;
	private XSSFRow FPSRow;
	private XSSFCellStyle style;
	private String screenshotRunPath;
	private int CPURowNum;
	private int MemRowNum;
	private int FPSRowNum;
	private Map<String, Object[]> CPUInfo = null;
	private Map<String, Object[]> MemInfo = null;
	private Map<String, Object[]> FPSInfo = null;
	
	public RunTestCase(JSONArray jsonFile, int runMode, AndroidDriver driver, JTextArea logText, IDevice device, String pkg, String caseName) {
//		this.path = path;
		this.runMode = runMode;
		this.driver = driver;
		this.logText = logText;
		this.device = device;
		this.jsonFile = jsonFile;
		this.pkg = pkg;
		this.caseName = caseName;
		init();
	}
	
	private void init() {
		action = new CusAction(driver, device);
		validation = new CusValidation(driver);
		exlUtils = new ExcelUtils();
		gfxUtil = new GfxAnalyse(device, pkg);
		
		//init screenshot running cap
		screenshotRunPath = System.getProperty("user.dir") + File.separator + "screenshot/RunCap/" +
	    		File.separator + TimeUtils.formatTimeForFile(System.currentTimeMillis());
		File screenRun = new File(screenshotRunPath);
		if (!screenRun.exists())
			screenRun.mkdirs();
		
		//init performance archive
		receiver = new CollectingOutputReceiver();
		mReceiver = new MultiLineReceiver() {
			
			@Override
			public boolean isCancelled() {
				return false;
			}
			
			@Override
			public void processNewLines(String[] lines) {
				String[] proLines = lines[0].split("\n");
			}
		};
		
		try {
			device.executeShellCommand("ps | grep " + pkg, receiver);
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e1) {
			e1.printStackTrace();
		}
		receiver.flush();
		pid = receiver.getOutput().split("\\s+")[1];
		
		style = exlUtils.setCellAlignCenter();
		
		CPUSheet = exlUtils.getSheet("CPU");
		MemSheet = exlUtils.getSheet("Memory");
		FPSSheet = exlUtils.getSheet("FPS");
		CPURowNum = CPUSheet.getLastRowNum();
		MemRowNum = MemSheet.getLastRowNum();
		FPSRowNum = FPSSheet.getLastRowNum();
		CPUInfo = new TreeMap<String, Object[]>();
		MemInfo = new TreeMap<String, Object[]>();
		FPSInfo = new TreeMap<String, Object[]>();
		
		if (CPURowNum == 0) 
			initRow(CPUInfo, CPURow, CPUSheet);
		
		if (MemRowNum == 0)
			initRow(MemInfo, MemRow, MemSheet);
		
		if (FPSRowNum == 0)
			initRow(FPSInfo, FPSRow, FPSSheet);
	}
	
	private void initRow(Map<String, Object[]> infoMap, XSSFRow row, XSSFSheet sheet) {
		Object[] arrayOb = new Object[101];
		arrayOb[0] = "caseName/Num";
		for (int i=1; i<=100; i++) {
			arrayOb[i] = i;
		}
		
		infoMap.put("1", arrayOb);
		int rowid = 0;
		
//		if (type == 0) {
		row = sheet.createRow(rowid ++);
		Object [] objectArr = infoMap.get("1");
		int cellid = 0;
		for (Object obj : objectArr) {
			Cell cell = row.createCell(cellid ++);
			if (obj instanceof String)
				cell.setCellValue((String) obj);
			else if (obj instanceof Integer)
				cell.setCellValue(String.valueOf(obj));
		}
//		} else if (type == 1) {
//			row = sheet.createRow(0);
//			XSSFRow rowGPU = sheet.createRow(1);
//			Object[] objectArr = infoMap.get("1");
//			for (Object obj : objectArr) {
//				if (obj instanceof String) {
//					Cell cell = row.createCell(0);
//					cell.setCellValue((String) obj);
//					GPUSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
//				} else if (obj instanceof Integer) {
//					int cellid = ((Integer) obj) * 3 - 2;
//					Cell cell = row.createCell(cellid);
//					cell.setCellValue(String.valueOf(obj));
//					
//					int cellGpuid = (Integer) obj;
//					if (cellGpuid % 3 == 1) {
//						Cell cellDraw = rowGPU.createCell(cellGpuid);
//						cellDraw.setCellValue("Draw");
//						cellDraw.setCellStyle(style);
//					} else if (cellGpuid % 3 == 2) {
//						Cell cellPro = rowGPU.createCell(cellGpuid);
//						cellPro.setCellValue("Process");
//						cellPro.setCellStyle(style);
//					} else if (cellGpuid % 3 == 0) {
//						Cell cellExe = rowGPU.createCell(cellGpuid);
//						cellExe.setCellValue("Execute");
//						cellExe.setCellStyle(style);
//					}
//					cell.setCellStyle(style);
//					GPUSheet.addMergedRegion(new CellRangeAddress(0, 0, cellid, cellid + 2));
//				}
//			}
//		}
		exlUtils.updateWorkbook();
	}

	public void run() {
		int actionCount = 0;
		BufferedWriter out = null;
		
		Object[] memList = new Object[jsonFile.size() + 1];
		memList[0] = caseName;
		
		Object[] fpsList = new Object[jsonFile.size() + 1];
		fpsList[0] = caseName;
		
		//running start
		if (this.runMode == 0) {
			for (int i=0; i<jsonFile.size(); i++) {
				JSONObject obj = jsonFile.getJSONObject(i);
				try {
					if (obj.getString("property").equals("ele")) {
							ele_sub = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(obj.getString("ele_xpath"));
							this.ele_customName = obj.getString("custom_name");
					} else if (obj.getString("property").equals("act")) {
						Thread.sleep(300);
						actionCount ++;
					    getScreenshot(screenshotRunPath, actionCount);
						this.actionSwitch(obj);
						
						performanceGet(memList, fpsList, actionCount);
					} else if (obj.getString("property").equals("val")) {
						this.validationSwitch(obj);
						Thread.sleep(1000);
					} else if (obj.getString("property").equals("time")) {
						int time = ((Integer)obj.getJSONObject("params").get("time")) * 1000;
						Thread.sleep(time);
					}
				} catch (Exception e) {
					if (e instanceof NoSuchElementException)
						logText.append("No such element: " + this.ele_customName);
					else {
						logText.append("Case Failed" + "\n");
					}
					LOG.error(e);
					break;
				}
			}
			updateRow(memList, fpsList);
		}
	}
	
	private void performanceGet(Object[] memList, Object[] fpsList, int index) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
		//get mem value
		device.executeShellCommand("adb shell \"su 0 \"procrank | grep \'dji.go.v4\'\"\"", receiver);
		receiver.flush();
		int memValue = Integer.parseInt(receiver.getOutput().split("\\s+")[3]);
		memList[index] = memValue;
		
		//get FPS value
		int fps = gfxUtil.getGfxInfo();
		fpsList[index] = fps;
	}
	
	private void updateRow(Object[] memList, Object[] fpsList) {
		MemInfo.put(String.valueOf(MemRowNum++), memList);
		MemRow = MemSheet.createRow(MemRowNum);
		int cellMemid = 0;
		for (Object obj : memList) {
			Cell cell = MemRow.createCell(cellMemid++);
			if (obj instanceof String)
				cell.setCellValue((String)obj);
			else if (obj instanceof Integer)
				cell.setCellValue(String.valueOf(obj));
		}
		
		FPSInfo.put(String.valueOf(FPSRowNum++), fpsList);
		FPSRow = FPSSheet.createRow(FPSRowNum);
		int cellFpsid = 0;
		for (Object obj : fpsList) {
			Cell cell = FPSRow.createCell(cellFpsid++);
			if (obj instanceof String)
				cell.setCellValue((String)obj);
			else if (obj instanceof Integer)
				cell.setCellValue(String.valueOf(obj));
		}
		exlUtils.updateWorkbook();
	}
	
	public void actionSwitch(JSONObject action_info) throws Exception {
		int action_name = action_info.getIntValue("action_name");
		switch (action_name) {
		case 0:
			action.click(ele_sub);
			logText.append(this.ele_customName + " is clicked" + "\n");
			break;
		case 1:
			action.longPress(ele_sub);
			logText.append(this.ele_customName + " is long pressed" + "\n");
			break;
		case 2:
			JSONObject params_text = action_info.getJSONObject("params");
			action.setText(ele_sub, params_text.getString("inputText"));
			logText.append(this.ele_customName + " set text successed" + "\n");
			break;
		case 4:
			JSONObject params_drag = action_info.getJSONObject("params");
			action.pointDrag(ele_sub, new Point(params_drag.getJSONObject("DesPoint").getIntValue("x"), params_drag.getJSONObject("DesPoint").getIntValue("y")));
			logText.append(this.ele_customName + " is draged to the position" + "\n");
			break;
		case 5:
			action.pushFileToDevice();
			logText.append("Pushing files..." + "\n");
			break;
		case 6:
			action.reboot();
			logText.append("Rebooting..." + "\n");
			AppiumInit.setUp(device, "com.dpad.launcher", "com.dpad.launcher.Launcher");
			this.driver = AppiumInit.driver;
			break;
		case 7:
			JSONObject params_swipe = action_info.getJSONObject("params");
			if (params_swipe.getString("elePath") == null) {
				String scrollPosition = params_swipe.getString("scrollPos");
				double position = 0.0;
				switch (scrollPosition) {
					case "From quarter":
						position = 0.25;
						break;
					case "From half":
						position = 0.5;
						break;
					case "From Bottom":
						position = 0.9;
						break;
				}
				action.swipe(position, params_swipe.getString("containerPath"), params_swipe.getIntValue("heading"));
			} else {
				action.swipe(params_swipe.getString("elePath"), params_swipe.getString("containerPath"), params_swipe.getIntValue("heading"));
			}
			logText.append("Page is swiped" + "\n");
			break;
		case 8:
			action.keyHOME();
			logText.append("Press HOME" + "\n");
			break;
		case 9:
			action.KeyBACK();
			logText.append("Press BACK" + "\n");
			break;
		case 10:
			action.dragBar(ele_sub);
			logText.append(this.ele_customName + " is dragged" + "\n");
			break;
		case 11:
			JSONObject params_spinner = action_info.getJSONObject("params");
			action.spinnerSelect(ele_sub, params_spinner.getString("choose"));
			logText.append("Spinner choose " + params_spinner.getString("choose") + "\n");
			break;
		}
		
	}
	
	public void validationSwitch(JSONObject validation_info) throws Exception {
		int validation_name = validation_info.getIntValue("validation_name");
		JSONObject params = validation_info.getJSONObject("params");
		
		switch (validation_name) {
			case 1:
				String ele_name_text = (String) params.get("ele_path");
				String except_text = (String) params.get("expect_text");
				if (validation.getText(ele_name_text, except_text))
					logText.append("Text validation success" + "\n");
				else
					logText.append("Text validation fail" + "\n");
				break;
			case 2:
				String ele_name_elval = (String) params.get("ele_path");
				if (validation.getExactEle(ele_name_elval))
					logText.append("Element validation success" + "\n");
				else
					logText.append("Element validation fail" + "\n");
				break;
			case 3:
				String ele_name_numVal = (String) params.get("ele_path");
		}
	}

	private void getScreenshot(String screenshotRunPath, int actionCount) throws TimeoutException, AdbCommandRejectedException, IOException, ShellCommandUnresponsiveException {
		RawImage rawImg = device.getScreenshot();
		Boolean landscape = false;
		
		CollectingOutputReceiver receiver = new CollectingOutputReceiver();
		device.executeShellCommand("dumpsys display | grep 'mDefaultViewport'", receiver, 0);
		switch (Character.getNumericValue(receiver.getOutput().charAt(72))) {
			case 0:
				landscape = false;
				break;
			case 1:
				landscape = true;
				break;
		}
		
		if (landscape) 
			rawImg = rawImg.getRotated();

		if (rawImg != null) {
			BufferedImage image = new BufferedImage(rawImg.width, rawImg.height,  
                    BufferedImage.TYPE_INT_RGB);
			
			int index = 0;
		    int IndexInc = rawImg.bpp >> 3;
		    for (int y = 0; y < rawImg.height; y++) {
		        for (int x = 0; x < rawImg.width; x++) {
		            int value = rawImg.getARGB(index);
		            index += IndexInc;
		            image.setRGB(x, y, value);
		        }
		    }
	
		    String filePath = screenshotRunPath + File.separator + actionCount + ".png";
		    if (!ImageIO.write(image, "png", new File(filePath))) {
		        throw new IOException("Failed to find png writer");
		    }
		}
	}
	
	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertAccept(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertDismiss(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeAlertAccept(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeAlertDismiss(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
