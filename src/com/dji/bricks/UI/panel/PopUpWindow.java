package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.dji.bricks.MainEntry;
import com.dji.bricks.UI.BrickBean;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.EleListener;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.UI.ViewListener;
import com.dji.bricks.tools.PropertyUtil;
import com.dji.bricks.tools.SQLUtils;

public class PopUpWindow extends JFrame {
	
	private static MyIconButton buttonVersetTX_add;
	private static MyIconButton buttonVersetTX_re;
	private static MyIconButton buttonVersetEE_add;
	private static MyIconButton buttonVersetEE_re;
	private JComboBox<String> comboxAppName;
	private JComboBox<String> comboxViewName;
	private JComboBox<String> comboxEleName;
	private JFrame popup_frame;
	
	private Object[] table_row;
	private DefaultTableModel model;
	private JTable casetable;
	private SQLUtils sql;
	private ArrayList<BrickBean> caseList;
	private StringBuilder xpath;
	private StringBuilder cus_name;
	private StringBuilder scrshot_pathname;
	private StringBuilder appName;
	
	private EleListener elisten;
	private ViewListener vlisten;

    public PopUpWindow(Object[] table_row, DefaultTableModel model, JTable casetable, SQLUtils sql, ViewListener vlisten, ArrayList<BrickBean> caseList) {
    	this.table_row = table_row;
    	this.model = model;
    	this.casetable = casetable;
    	this.sql = sql;
    	this.caseList = caseList;
    	
    	init();
    }
    
    private void init() {
    	popup_frame = new JFrame();
    	comboxAppName = new JComboBox<String>();
    	comboxViewName = new JComboBox<String>();
    	comboxEleName = new JComboBox<String>();
    	xpath = new StringBuilder();
    	cus_name = new StringBuilder();
    	scrshot_pathname = new StringBuilder();
    	appName = new StringBuilder();
    	
    	elisten = new EleListener(sql, xpath, cus_name, scrshot_pathname);
    	vlisten = new ViewListener(sql, comboxEleName, elisten, appName);
    }
    
    public void popSelect(int type, BrickBean brick) {
    	switch (type) {
    		case 0:
    			waring();
    			break;
    		case 1:
    			textVer();
    			break;
    		case 2:
    			eleVer();
    			break;
    		case 3:
    			break;
    		case 4:
    			addTime();
    			break;
    		case 5:
    			actionSetText(brick);
    			break;
    	}
    }
    
    private void waring() {
    	// Warning message
		popup_frame.setSize(250, 100);
		popup_frame.setTitle("WARNING");
		popup_frame.setLayout(new BorderLayout());
		popup_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
		popup_frame.setLocationRelativeTo(MainEntry.frame);
		popup_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup_frame.setVisible(true);
		JLabel repick_warn = new JLabel();
		ImageIcon warn_icon = new ImageIcon(System.getProperty("user.dir") + File.separator + "icon" + File.separator + "warning.png");
		JLabel icon_label = new JLabel(warn_icon);
		repick_warn.setText(PropertyUtil.getProperty("bricks.ui.casecre.pickwarn"));
		repick_warn.setFont(new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 12));
		popup_frame.add(icon_label, BorderLayout.WEST);
		popup_frame.add(repick_warn, BorderLayout.CENTER);
    }
    
    private void textVer() {
    	// Text verification method
		popup_frame.setSize(400, 280);
		popup_frame.setTitle("Text Verification");
		popup_frame.setVisible(true);
		JPanel up_panel = new JPanel();
		JPanel up_left_panel = new JPanel();
		JPanel up_right_panel = new JPanel();
		JPanel down_panel = new JPanel();
		up_panel.setLayout(new BorderLayout());
		up_left_panel.setLayout(new GridLayout(6,1));
		JLabel app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appname"));
		app_name.setFont(ConstantsUI.FONT_NORMAL);
		JLabel app_view = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appview"));
		app_view.setFont(ConstantsUI.FONT_NORMAL);
		JLabel ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elename"));
		ele_name.setFont(ConstantsUI.FONT_NORMAL);
		addEleCombox();
		
		JPanel text_pane = new JPanel();
		JPanel text_btn_pane = new JPanel();
		JTextArea ver_text_input = new JTextArea(8,30);
		ver_text_input.setLineWrap(true);
		buttonVersetTX_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
		buttonVersetTX_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
		text_pane.add(new JScrollPane(ver_text_input));
		text_btn_pane.add(buttonVersetTX_add);
		text_btn_pane.add(buttonVersetTX_re);
		
		up_left_panel.add(app_name);
		up_left_panel.add(comboxAppName);
		up_left_panel.add(app_view);
		up_left_panel.add(comboxViewName);
		up_left_panel.add(ele_name);
		up_left_panel.add(comboxEleName);
		up_right_panel.add(text_pane);
		up_panel.add(up_left_panel, BorderLayout.WEST);
		up_panel.add(up_right_panel, BorderLayout.EAST);
		down_panel.add(text_btn_pane);
		popup_frame.add(up_panel, BorderLayout.NORTH);
		popup_frame.add(down_panel, BorderLayout.SOUTH);
		
		// kill the thread, same to the others ver_type case
		popup_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup_frame.setVisible(true);
		// set the popup window's position releat to Main frame
		popup_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
		popup_frame.setLocationRelativeTo(MainEntry.frame);

		// inside-button method
		buttonVersetTX_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.textver");
                	table_row[2] = ver_text_input.getText();
                	table_row[3] = "N/A";
                	table_row[4] = "N/A";
                	
                	int i = casetable.getSelectedRow();
                    if(i >= 0){
                    	model.insertRow(i+1, table_row);
                    }else{
                    	model.addRow(table_row);
                    }
                	
                	String ele_path_text = xpath.toString();
					String expect_text = ver_text_input.getText();
					
					BrickBean brick_valText = new BrickBean();
					brick_valText.setProperty("val");
					brick_valText.setValidation_name(1);
					Map<String, Object> params_text = new HashMap<>();
					params_text.put("ele_path", ele_path_text);
					params_text.put("expect_text", expect_text);
					brick_valText.setParams(params_text);
					caseList.add(brick_valText);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

                popup_frame.dispatchEvent(new WindowEvent(popup_frame, WindowEvent.WINDOW_CLOSING));
            }
        });
		
		buttonVersetTX_re.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try{
                int i = casetable.getSelectedRow();
                
                if(i >= 0) 
                   model.setValueAt("ver_text_re", i, 1);
                else
                    System.out.println("Update Act Error");
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
		});
    }
    
    private void eleVer() {
    	// Element exist verification method
		popup_frame.setSize(300, 200);
		popup_frame.setTitle("Element Picking");
		popup_frame.setLayout(new GridLayout(4,1));
		JPanel app_name_pick = new JPanel();
		JPanel app_view_pick = new JPanel();
		JPanel ele_name_pick = new JPanel();
		JPanel button_pane = new JPanel();
		JLabel app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appname"));
		app_name.setFont(ConstantsUI.FONT_NORMAL);
		JLabel app_view = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appview"));
		app_view.setFont(ConstantsUI.FONT_NORMAL);
		JLabel ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elename"));
		ele_name.setFont(ConstantsUI.FONT_NORMAL);
		addEleCombox();
		
		buttonVersetEE_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
		buttonVersetEE_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
		
		// inside-button method
		buttonVersetEE_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.extver");
                	table_row[2] = comboxAppName.getSelectedItem();
                	table_row[3] = comboxViewName.getSelectedItem();
                	table_row[4] = comboxEleName.getSelectedItem();
                	
                	int i = casetable.getSelectedRow();
                    if(i >= 0){
                    	model.insertRow(i+1, table_row);
                    }else{
                    	model.addRow(table_row);
                    }
                	
                	String ele_path_eleVal = xpath.toString();
					BrickBean brick_valEle = new BrickBean();
					brick_valEle.setProperty("val");
					brick_valEle.setValidation_name(2);
					Map<String, Object> params_eleVal = new HashMap<>();
					params_eleVal.put("ele_path", ele_path_eleVal);
					brick_valEle.setParams(params_eleVal);
					caseList.add(brick_valEle);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

                popup_frame.dispatchEvent(new WindowEvent(popup_frame, WindowEvent.WINDOW_CLOSING));
            }
        });
		
		buttonVersetEE_re.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					// i = the index of the selected row
					int i = casetable.getSelectedRow();
            
					if(i >= 0) 
					{
						model.setValueAt("ver_re", i, 1);
					}
					else{
						System.out.println("Update Act Error");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		app_name_pick.add(app_name);
		app_name_pick.add(comboxAppName);
		app_view_pick.add(app_view);
		app_view_pick.add(comboxViewName);
		ele_name_pick.add(ele_name);
		ele_name_pick.add(comboxEleName);
		button_pane.add(buttonVersetEE_add);
		button_pane.add(buttonVersetEE_re);
		popup_frame.add(app_name_pick);
		popup_frame.add(app_view_pick);
		popup_frame.add(ele_name_pick);
		popup_frame.add(button_pane);
		popup_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
		popup_frame.setLocationRelativeTo(MainEntry.frame);
		popup_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup_frame.setVisible(true);
    }
    
    private void addTime() {
    	// Timer adding method
		popup_frame.setSize(270, 130);
		popup_frame.setTitle("WaitTime Setting");
		popup_frame.setLayout(new BorderLayout());
		JPanel timer_pane = new JPanel();
		timer_pane.setPreferredSize(new Dimension(270, 50));
		JLabel timer_label = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.timer"));
		timer_label.setFont(ConstantsUI.FONT_NORMAL);
		JTextField timer_num = new JTextField();
		timer_num.setPreferredSize(new Dimension(80,20));
		JPanel timer_btn_pane = new JPanel();
		MyIconButton buttonTimer_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.timer"));
		MyIconButton buttonTimer_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.retimer"));
		// inside-button method
		buttonTimer_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.timer");
                	table_row[2] = timer_num.getText() +"  "+ "S";
                	table_row[3] = "N/A";
                	table_row[4] = "N/A";
                	
                	Map<String, Object> params_time = new HashMap<>();
                    params_time.put("time", Integer.parseInt(timer_num.getText()));
                    BrickBean timeBrick = new BrickBean();
                    timeBrick.setProperty("time");
                    timeBrick.setParams(params_time);
                    
                	int i = casetable.getSelectedRow();
                    if(i >= 0){
                    	model.insertRow(i+1, table_row);
                    	caseList.add(i+1, timeBrick);
                    }else{
                    	model.addRow(table_row);
                    	caseList.add(timeBrick);
                    }
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

                popup_frame.dispatchEvent(new WindowEvent(popup_frame, WindowEvent.WINDOW_CLOSING));
            }
        });
		
		buttonTimer_re.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					// i = the index of the selected row
					int i = casetable.getSelectedRow();
            
					if(i >= 0) 
					{
						model.setValueAt("timer_re", i, 1);
					}
					else{
						System.out.println("Update Act Error");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		timer_pane.add(timer_label);
		timer_pane.add(timer_num);
		timer_btn_pane.add(buttonTimer_add);
		timer_btn_pane.add(buttonTimer_re);
		popup_frame.add(timer_pane, BorderLayout.NORTH);
		popup_frame.add(timer_btn_pane, BorderLayout.SOUTH);
		popup_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
		popup_frame.setLocationRelativeTo(MainEntry.frame);
		popup_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup_frame.setVisible(true);
    }
    
    private void actionSetText(BrickBean brick) {
    	popup_frame.setSize(270, 130);
    	popup_frame.setTitle("Text Setting");
		popup_frame.setLayout(new BorderLayout());
		JPanel text_set_pane = new JPanel();
		text_set_pane.setPreferredSize(new Dimension(270, 50));
		JLabel text_set_label = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.btntip.textset"));
		text_set_label.setFont(ConstantsUI.FONT_NORMAL);
		JTextField text_content = new JTextField();
		text_content.setPreferredSize(new Dimension(80,20));
		JPanel text_btn_pane = new JPanel();
		MyIconButton buttonText_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.textset"));
		MyIconButton buttonText_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.textret"));
		
		buttonText_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.act");
					table_row[2] = PropertyUtil.getProperty("bricks.ui.casecre.btntip.textset");
					table_row[3] = text_content.getText();
			    	table_row[4] = "N/A";
                	
                	Map<String, Object> params_text = new HashMap<>();
                    params_text.put("inputText", text_content.getText());
                    if (brick != null) {
                    	brick.setParams(params_text);
                    }
                    
                    int i = casetable.getSelectedRow();
        	        if(i >= 0){
        	        	model.insertRow(i+1, table_row);
        	        	caseList.add(i+1, brick);
        	        }else{
        	        	model.addRow(table_row);
        	        	caseList.add(brick);
        	        }
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

                popup_frame.dispatchEvent(new WindowEvent(popup_frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		text_set_pane.add(text_set_label);
		text_set_pane.add(text_content);
		text_btn_pane.add(buttonText_add);
		text_btn_pane.add(buttonText_re);
		popup_frame.add(text_set_pane, BorderLayout.NORTH);
		popup_frame.add(text_btn_pane, BorderLayout.SOUTH);
		popup_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
		popup_frame.setLocationRelativeTo(MainEntry.frame);
		popup_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup_frame.setVisible(true);
    }
    
    private void actionScroll() {
    	popup_frame.setSize(300, 500);
		popup_frame.setTitle("Scroll to exact element in container");
		popup_frame.setLayout(new GridLayout(8,1));
		JPanel target_app_name = new JPanel();
		JPanel target_app_view = new JPanel();
		JPanel target_ele_name = new JPanel();
		JPanel container_app_name = new JPanel();
		JPanel container_app_view = new JPanel();
		JPanel container_ele_name = new JPanel();
		JPanel button_pane = new JPanel();
		JLabel app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appname"));
		app_name.setFont(ConstantsUI.FONT_NORMAL);
		JLabel app_view = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appview"));
		app_view.setFont(ConstantsUI.FONT_NORMAL);
		JLabel ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elename"));
		ele_name.setFont(ConstantsUI.FONT_NORMAL);
    }
    
    private void addEleCombox() {
		comboxAppName.addItem("DJI GO3");
		comboxAppName.addItem("DJI GO4");
		comboxAppName.addItem("DJI Pilot");
		comboxAppName.addItem("RM500 Launcher");
		comboxAppName.addItem("RM500 Settings");
		comboxAppName.addItem("MG 1A/P");
		comboxAppName.addItem("MG 1S");
		comboxAppName.addItem("DJI GO4 Pad");
		comboxAppName.setEditable(false);
		comboxAppName.setSelectedItem(null);
		comboxAppName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxViewName.setEditable(false);
		comboxViewName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxEleName.setEditable(false);
		comboxEleName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxAppName.addItemListener(new ItemListener() {
			// combobox item changed method
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ResultSet rs = null;
					try {
						appName.delete(0, appName.length()).append(new String(((String) e.getItem()).getBytes(), "UTF-8"));
    					rs = sql.queryElement("ACTIVITY", appName.toString());
    					
    					comboxViewName.removeAllItems();
    					comboxEleName.removeAllItems();
    					comboxViewName.removeItemListener(vlisten);
					
						while (rs.next()) {
							String viewName = new String(rs.getBytes("ACTIVITY_NAME"), "UTF-8");
							comboxViewName.addItem(viewName);
						}
						comboxViewName.setSelectedItem(null);
						comboxViewName.addItemListener(vlisten);
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					} finally {
						if (rs != null) {
							try {
								rs.close();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
	}
}
