package com.dji.bricks.backgrounder.execution;

import java.awt.Point;
import java.util.List;

import javax.swing.JTextArea;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.IDevice;
import com.dji.bricks.backgrounder.ExecutionMain;
import com.dji.bricks.backgrounder.base.CusAction;
import com.dji.bricks.backgrounder.base.CusElement;
import com.dji.bricks.backgrounder.base.CusValidation;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.api.general.AppiumWebDriverEventListener;

public class RunTestCase implements AppiumWebDriverEventListener{

	private WebElement ele_sub;
	private String ele_customName;
	private int runMode;
//	private String path;
	private AndroidDriver driver;
	private CusAction action;
	private CusValidation validation;
	private JTextArea logText;
	private IDevice device;
	private JSONArray jsonFile;
	private List<WebElement> dragedElement;
	private Boolean isDraged = false;
	private String eleXpath;
	
	public RunTestCase(JSONArray jsonFile, int runMode, AndroidDriver driver, JTextArea logText, IDevice device) {
//		this.path = path;
		this.runMode = runMode;
		this.driver = driver;
		this.logText = logText;
		this.device = device;
		this.jsonFile = jsonFile;
		action = new CusAction(driver);
		validation = new CusValidation(driver);
	}
	
	public RunTestCase(int runMode, AndroidDriver driver, IDevice device, String eleXpath) {
		this.runMode = runMode;
		this.driver = driver;
		this.device = device;
		this.eleXpath = eleXpath;
		action = new CusAction(driver);
	}

	public void run() {
		
		if (this.runMode == 0) {
			for (int i=0; i<jsonFile.size(); i++) {
				JSONObject obj = jsonFile.getJSONObject(i);
				try {
					if (obj.getString("property").equals("ele")) {
							ele_sub = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(obj.getString("ele_xpath"));
							this.ele_customName = obj.getString("custom_name");
					} else if (obj.getString("property").equals("act")) {
						this.actionSwitch(obj);
					} else if (obj.getString("property").equals("val")) {
						this.validationSwitch(obj);
					} else if (obj.getString("property").equals("time")) {
						int time = ((Integer)obj.getJSONObject("params").get("time")) * 1000;
						Thread.sleep(time);
					}
				} catch (Exception e) {
					if (e instanceof NoSuchElementException)
						logText.append("No such element: " + this.ele_customName);
					else {
						logText.append("Case Failed");
						e.printStackTrace();
					}
					break;
				}
			}
		} else if (this.runMode == 1) {
			WebElement tmpEle = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(eleXpath);
			action.click(tmpEle);
		}
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
			action.setText(ele_sub, action_info.getString("inputText"));
			logText.append(this.ele_customName + " set text successed" + "\n");
			break;
		case 4:
			JSONObject params = action_info.getJSONObject("params");
			action.pointDrag(ele_sub, new Point(params.getJSONObject("DesPoint").getIntValue("x"), params.getJSONObject("DesPoint").getIntValue("y")));
			logText.append(this.ele_customName + " is draged to the position" + "\n");
			break;
		case 5:
			JSONObject params_swipe = action_info.getJSONObject("params");
			action.swipe(new Point(params_swipe.getJSONObject("StartPoint").getIntValue("x"), params_swipe.getJSONObject("StartPoint").getIntValue("y")), 
					new Point(params_swipe.getJSONObject("DesPoint").getIntValue("x"), params_swipe.getJSONObject("DesPoint").getIntValue("y")));
			logText.append("Page is swiped");
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			action.dragBar(ele_sub);
			logText.append(this.ele_customName + " is dragged" + "\n");
			break;
		case 11:
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
