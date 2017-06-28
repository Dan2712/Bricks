package backgrounder.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.javascript.host.Element;

import backgrounder.base.CusAction;
import backgrounder.base.CusElement;
import backgrounder.base.CusValidation;
import io.appium.java_client.AppiumDriver;
import tools.FileOperation;

public class RunTestCase implements Runnable{

	private WebElement ele_sub;
	private String ele_customName;
	private int runMode;
	private String path;
	private AppiumDriver driver;
	private CusAction action;
	private CusValidation validation;
	
	public RunTestCase(String path, int runMode, AppiumDriver driver) {
		this.path = path;
		this.runMode = runMode;
		this.driver = driver;
		action = new CusAction(driver);
		validation = new CusValidation(driver);
	}

	@Override
	public void run() {
		
		LinkedHashMap<String, String> jsonMap = FileOperation.loadJson(this.path);
		if (this.runMode == 0) {
			for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
				if (entry.getKey().endsWith("1")) {
					JSONObject obj = JSON.parseObject(entry.getValue());
					if (obj.containsKey("ele_id"))
						ele_sub = new CusElement(CommonInit.WAIT_TIME, driver).explicitlyWait(0, obj.getString("ele_id")); 
					else if (obj.containsKey("ele_xpath"))
						ele_sub = new CusElement(CommonInit.WAIT_TIME, driver).explicitlyWait(1, obj.getString("ele_xpath"));
					
					this.ele_customName = obj.getString("custom_name");
				} else if (entry.getKey().endsWith("2")) {
					JSONObject obj = JSON.parseObject(entry.getValue());
					this.actionSwitch(obj);
				} else if (entry.getKey().endsWith("3")) {
					JSONObject obj = JSON.parseObject(entry.getValue());
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
			System.out.println(this.ele_customName + " is clicked");
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
			System.out.println(this.ele_customName + " is dragged");
			break;
		case 11:
			break;
		}
		
	}
	
	public void validationSwitch(JSONObject validation_info) {
		System.out.println("text validation");
		int validation_name = validation_info.getIntValue("validation_name");
		String ele_name = validation_info.getString("ele_id");
		int search_mode = 0;
		if (validation_info.containsKey("ele_id"))
			search_mode = 0;
		else if (validation_info.containsKey("ele_xpath"))
			search_mode = 1;
		
		switch (validation_name) {
		case 1:
			
			String except_text = validation_info.getString("expect_text");
			if (validation.getText(ele_name, search_mode, except_text))
				System.out.println("success");
			else
				System.out.println("fail");
			break;
		case 2:
			break;
		}
	}
}
