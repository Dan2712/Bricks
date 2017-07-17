package com.dji.bricks.backgrounder.execution;

import javax.swing.JTextArea;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.dji.bricks.backgrounder.base.CusAction;
import com.dji.bricks.backgrounder.base.CusElement;
import com.dji.bricks.backgrounder.base.CusValidation;
import io.appium.java_client.AppiumDriver;
import com.dji.bricks.tools.FileUtils;

public class RunTestCase implements Runnable{

	private WebElement ele_sub;
	private String ele_customName;
	private int runMode;
	private String path;
	private AppiumDriver driver;
	private CusAction action;
	private CusValidation validation;
	private JTextArea logText;
	
	public RunTestCase(String path, int runMode, AppiumDriver driver, JTextArea logText) {
		this.path = path;
		this.runMode = runMode;
		this.driver = driver;
		this.logText = logText;
		action = new CusAction(driver);
		validation = new CusValidation(driver);
	}

	@Override
	public void run() {
		
		JSONArray jsonFile = FileUtils.loadJson(this.path);
		if (this.runMode == 0) {
			for (int i=0; i<jsonFile.size(); i++) {
				JSONObject obj = jsonFile.getJSONObject(i);
				if (obj.getString("property").equals("ele")) {
					try {
						ele_sub = new CusElement(AppiumInit.WAIT_TIME, driver).explicitlyWait(obj.getString("ele_xpath"));
						this.ele_customName = obj.getString("custom_name");
					} catch (NoSuchElementException e) {
						logText.append("No such element: " + this.ele_customName);
						break;
					}
				} else if (obj.getString("property").equals("act")) {
					this.actionSwitch(obj);
				} else if (obj.getString("property").equals("val")) {
					this.validationSwitch(obj);
				}
			}
		}
	}
	
	public void actionSwitch(JSONObject action_info) {
		int action_name = action_info.getIntValue("action_name");
		switch (action_name) {
		case 1:
			action.click(ele_sub);
			logText.append(this.ele_customName + " is clicked" + "\n");
			break;
		case 2:
			action.longPress(ele_sub);
			break;
		case 3:
			action.setText(ele_sub, action_info.getString("inputText"));
			break;
		case 4:
			break;
		case 5:
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
			logText.setText(this.ele_customName + " is dragged" + "\n");
			break;
		case 11:
			break;
		}
		
	}
	
	public void validationSwitch(JSONObject validation_info) {
		int validation_name = validation_info.getIntValue("validation_name");
		JSONObject params = validation_info.getJSONObject("params");
		
		switch (validation_name) {
		case 1:
			String ele_name = params.getString("ele_path");
			String except_text = params.getString("expect_text");
			if (validation.getText(ele_name, except_text))
				logText.append("success" + "\n");
			else
				logText.append("fail" + "\n");
			break;
		case 2:
			break;
		}
	}
}