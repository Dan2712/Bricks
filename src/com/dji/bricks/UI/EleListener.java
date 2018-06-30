package com.dji.bricks.UI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;

import com.dji.bricks.tools.SQLUtils;

public class EleListener implements ItemListener {

	private ResultSet xpathSet = null;
	private SQLUtils sql;
	
	private StringBuilder xpath;
	private StringBuilder cus_name;
	private StringBuilder scrshot_pathname;
	
	public EleListener(SQLUtils sql, StringBuilder xpath, StringBuilder cus_name, StringBuilder scrshot_pathname) {
		// TODO Auto-generated constructor stub
		this.sql = sql;
		this.xpath = xpath;
		this.cus_name = cus_name;
		this.scrshot_pathname = scrshot_pathname;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String ele_cus = (String) e.getItem();
			xpathSet = sql.queryElement("ELEMENT", ele_cus);
		}
		
		try {
			while (xpathSet.next()) {
				xpath.delete(0, xpath.length()).append(xpathSet.getString("XPATH"));
				cus_name.delete(0, cus_name.length()).append(xpathSet.getString("CUSTOM_NAME"));
				scrshot_pathname.delete(0, scrshot_pathname.length()).append(xpathSet.getString("SCREEN_PATH"));
				String state = xpathSet.getString(5);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			
		}
	}
}
