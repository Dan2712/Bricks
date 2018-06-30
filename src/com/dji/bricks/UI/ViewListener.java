package com.dji.bricks.UI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;

import com.dji.bricks.tools.SQLUtils;

public class ViewListener implements ItemListener {

	private SQLUtils sql;
	private JComboBox<String> comboxEleName;
	private EleListener elisten;
	private StringBuilder appName;
	
	public ViewListener(SQLUtils sql, JComboBox<String> comboxEleName, EleListener elisten, StringBuilder appName) {
		// TODO Auto-generated constructor stub
		this.sql = sql;
		this.comboxEleName = comboxEleName;
		this.elisten = elisten;
		this.appName = appName;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String viewName = (String) e.getItem();
			ResultSet rs = sql.queryElement("ELEMENT", appName.toString(), viewName);
			
			comboxEleName.removeAllItems();
			comboxEleName.removeItemListener(elisten);
			try {
				rs.next();
				String eleFirst = new String(rs.getBytes(1), "UTF-8");
				comboxEleName.addItem(eleFirst);
				while ((rs.next())) {
					String eleName = rs.getString(1);
					comboxEleName.addItem(eleName);
				}
				comboxEleName.setSelectedItem(null);
				comboxEleName.addItemListener(elisten);
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
						rs = null;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
