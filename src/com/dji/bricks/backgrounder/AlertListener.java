package com.dji.bricks.backgrounder;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.security.Credentials;

import io.appium.java_client.events.api.general.AlertEventListener;

public class AlertListener implements AlertEventListener {

	@Override
	public void afterAlertAccept(WebDriver arg0, Alert arg1) {
		// TODO Auto-generated method stub
		System.out.println("after accpet");
	}

	@Override
	public void afterAlertDismiss(WebDriver arg0, Alert arg1) {
		// TODO Auto-generated method stub
		System.out.println("after dimiss");
	}

	@Override
	public void afterAlertSendKeys(WebDriver arg0, Alert arg1, String arg2) {
		// TODO Auto-generated method stub
		System.out.println("after send key");
	}

	@Override
	public void afterAuthentication(WebDriver arg0, Alert arg1, Credentials arg2) {
		// TODO Auto-generated method stub
		System.out.println("after authentication");
	}

	@Override
	public void beforeAlertAccept(WebDriver arg0, Alert arg1) {
		// TODO Auto-generated method stub
		System.out.println("before accept");
	}

	@Override
	public void beforeAlertDismiss(WebDriver arg0, Alert arg1) {
		// TODO Auto-generated method stub
		System.out.println("before dismiss");
	}

	@Override
	public void beforeAlertSendKeys(WebDriver arg0, Alert arg1, String arg2) {
		// TODO Auto-generated method stub
		System.out.println("after send key");
	}

	@Override
	public void beforeAuthentication(WebDriver arg0, Alert arg1, Credentials arg2) {
		// TODO Auto-generated method stub
		System.out.println("after authentication");
	}

}
