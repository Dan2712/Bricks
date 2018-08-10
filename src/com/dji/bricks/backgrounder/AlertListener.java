package com.dji.bricks.backgrounder;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.security.Credentials;

import io.appium.java_client.events.api.general.AlertEventListener;
import io.appium.java_client.events.api.general.ElementEventListener;

public class AlertListener implements ElementEventListener {

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		System.out.println("1");
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		// TODO Auto-generated method stub
		System.out.println("2");
	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		System.out.println("3");
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		System.out.println("4");
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		// TODO Auto-generated method stub
		System.out.println("5");
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		System.out.println("6");
	}

	

}
