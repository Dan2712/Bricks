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
	
	private boolean isFieldExist(String tableName, String fieldName) {
		String tableCreateSql = null;
		try {
		    String queryStr = "select sql from sqlite_master where type = 'table' and name = '%s'";  
		    queryStr = String.format(queryStr, tableName);
		    Statement stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery(queryStr);
		    try {  
		        if (rs != null) {  
		            tableCreateSql = rs.getString("sql");  
		        }  
		    } finally {  
		        if (rs != null)  
		            rs.close();  
		    }
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	    if (tableCreateSql != null && tableCreateSql.contains(fieldName))  
	        return true;  
	    return false;  
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
					+ "(CUSTOM_NAME TEXT NOT NULL,"
					+ "XPATH TEXT NOT NULL,"
					+ "ACTIVITY_NAME TEXT NOT NULL,"
					+ "APP_NAME TEXT NOT NULL,"
					+ "STATE TEXT NOT NULL,"
					+ "SCREEN_PATH TEXT NOT NULL,"
					+ "VIEW_TEXT TEXT);";
			
			String updateElementTable = "ALTER TABLE ELEMENT ADD COLUMN VIEW_TEXT TEXT";
			if (!isTableExist("ELEMENT"))
				stmt.executeUpdate(createElementTable);
			else {
				if (!isFieldExist("ELEMENT", "VIEW_TEXT")) {
					stmt.executeUpdate(updateElementTable);
				}
			}
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void insertEle(String tableName, ArrayList<Map<String, String>> patterns) throws SQLException {
		String activity_name = patterns.get(2).get("ACTIVITY_NAME");
		String app_name = patterns.get(0).get("APP_NAME");
		String custom_name = patterns.get(1).get("CUSTOM_NAME");
		String xpath = patterns.get(3).get("XPATH");
		String state = patterns.get(4).get("STATE");
		String screen_path = patterns.get(5).get("SCREEN_PATH");
		String view_text = patterns.get(6).get("VIEW_TEXT");
		
		Statement stmt = conn.createStatement();
		String insert_act = "INSERT INTO ACTIVITY (ACTIVITY_NAME, APP_NAME) "
				+ "VALUES (\"" + activity_name + "\", \"" + app_name + "\");";
		String insert_ele = "INSERT INTO ELEMENT (CUSTOM_NAME, XPATH, ACTIVITY_NAME, APP_NAME, STATE, SCREEN_PATH, VIEW_TEXT) "
				+ "VALUES (\"" + custom_name + "\", \"" + xpath + "\", \"" + activity_name + "\", \"" + app_name + "\", \"" + state + "\", \"" + screen_path + "\", \"" + view_text + "\");";
		stmt.executeUpdate(insert_ele);
		stmt.executeUpdate(insert_act);
		
		stmt.close();
	}
	
	public ResultSet queryElement(String tableName, String appName) {
		ResultSet rs = null;
		String query = "";
		query = "SELECT DISTINCT * FROM " + tableName + " WHERE APP_NAME = \"" + appName + "\";";
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
	
	public ResultSet queryElement(String tableName, String appName, String viewName, String customName) {
		ResultSet rs = null;
		String query = "SELECT * FROM " + tableName + " WHERE APP_NAME = \"" + appName + "\" AND ACTIVITY_NAME = \"" + viewName + "\" AND CUSTOM_NAME = \"" + customName + "\";";
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public int updateElement(String tableName, ArrayList<Map<String, String>> patterns) throws SQLException {
		String activity_name = patterns.get(2).get("ACTIVITY_NAME");
		String app_name = patterns.get(0).get("APP_NAME");
		String custom_name = patterns.get(1).get("CUSTOM_NAME");
		String xpath = patterns.get(3).get("XPATH");
		String state = patterns.get(4).get("STATE");
		String screen_path = patterns.get(5).get("SCREEN_PATH");
		String view_text = patterns.get(6).get("VIEW_TEXT");
		
		Statement stmt = conn.createStatement();
		String update_act = "UPDATE ELEMENT SET XPATH = \"" + xpath + "\", STATE = \"" + state + "\", SCREEN_PATH = \"" + screen_path + "\", VIEW_TEXT = \"" + view_text +
				"\" WHERE ACTIVITY_NAME = \"" + activity_name + "\" AND APP_NAME = \"" + app_name + "\" AND CUSTOM_NAME = \"" + custom_name + "\";";
		return stmt.executeUpdate(update_act);
	}
}
