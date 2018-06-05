package com.dji.bricks.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

public class SQLUtils {
	
	private Connection conn = null;
	
	public SQLUtils(Connection conn) {
		this.conn = conn;
	}
	
	public Boolean isTableExist(String tableName) {
		boolean isTableExist = true;  
        try {
        	Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name= '" + tableName + "'");
			if (rs.getInt(1) == 0)
				isTableExist = false;
			
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
        
        return isTableExist;
	}
	
	public void creatTable() {
		try {
			Statement stmt = conn.createStatement();
			String createActivityTable = "CREATE TABLE ACTIVITY " +
					"(ACTIVITY_NAME TEXT NOT NULL," +
	                " APP_NAME TEXT NOT NULL)";
			if (!isTableExist("ACTIVITY"))
				stmt.executeUpdate(createActivityTable);
			String createElementTable = "CREATE TABLE ELEMENT "
					+ "(CUSTOM_NAME TEXT NOT NULL UNIQUE,"
					+ "XPATH TEXT NOT NULL,"
					+ "ACTIVITY_NAME TEXT NOT NULL,"
					+ "APP_NAME TEXT NOT NULL,"
					+ "STATE TEXT NOT NULL,"
					+ "SCREEN_PATH TEXT NOT NULL)";
			if (!isTableExist("ELEMENT"))
				stmt.executeUpdate(createElementTable);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void insertEle(String tableName, ArrayList<Map<String, String>> patterns) throws SQLException {
		String activity_name = "";
		String app_name = "";
		String custom_name = "";
		String xpath = "";
		String state = "";
		String screen_path = "";
		
		activity_name = patterns.get(2).get("ACTIVITY_NAME");
		app_name = patterns.get(0).get("APP_NAME");
		custom_name = patterns.get(1).get("CUSTOM_NAME");
		xpath = patterns.get(3).get("XPATH");
		state = patterns.get(4).get("STATE");
		screen_path = patterns.get(5).get("SCREEN_PATH");
		
		Statement stmt = conn.createStatement();
		String insert_act = "INSERT INTO ACTIVITY (ACTIVITY_NAME, APP_NAME) "
				+ "VALUES (\"" + activity_name + "\", \"" + app_name + "\");";
		stmt.executeUpdate(insert_act);
		String insert_ele = "INSERT INTO ELEMENT (CUSTOM_NAME, XPATH, ACTIVITY_NAME, APP_NAME, STATE, SCREEN_PATH) "
				+ "VALUES (\"" + custom_name + "\", \"" + xpath + "\", \"" + activity_name + "\", \"" + app_name + "\", \"" + state + "\", \"" + screen_path + "\");";
		stmt.executeUpdate(insert_ele);
		
		stmt.close();
	}
	
	public ResultSet queryElement(String tableName, String appName) {
		ResultSet rs = null;
		String query = "";
		if (tableName.equals("ACTIVITY"))
			query = "SELECT DISTINCT * FROM " + tableName + " WHERE APP_NAME = \"" + appName + "\";";
		else if (tableName.equals("ELEMENT"))
			query = "SELECT * FROM " + tableName + " WHERE CUSTOM_NAME = \"" + appName + "\";";
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet queryElement(String tableName, String appName, String viewName) {
		ResultSet rs = null;
		String query = "SELECT * FROM " + tableName + " WHERE APP_NAME = \"" + appName + "\" AND ACTIVITY_NAME = \"" + viewName +  "\";";
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
}
