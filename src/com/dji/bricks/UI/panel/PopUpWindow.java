package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

public class PopUpWindow extends JFrame {
	
	private JFrame popup_frame;
	
	private Object[] table_row;
	private DefaultTableModel model;
	private JTable casetable;
	private SQLUtils sql;
	private ArrayList<BrickBean> caseList;
	private int headingTag = 0;
	private int scrollTag = 0;
	private ResultSet xpathSet;
	
    public PopUpWindow(Object[] table_row, DefaultTableModel model, JTable casetable, SQLUtils sql, ArrayList<BrickBean> caseList) {
    	this.table_row = table_row;
    	this.model = model;
    	this.casetable = casetable;
    	this.sql = sql;
    	this.caseList = caseList;
    	
    	init();
    }
    
    private void init() {
    	popup_frame = new JFrame();
    	popup_frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				popup_frame.getContentPane().removeAll();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    
    public void popSelect(int type, BrickBean brick, int winType) {
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
    			cmpVer();
    			break;
    		case 4:
    			addTime();
    			break;
    		case 5:
    			actionTextWin(brick, winType);
    			break;
    		case 6:
    			actionScroll(brick);
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
    	StringBuilder xpath = new StringBuilder();
    	StringBuilder cus_name = new StringBuilder();
    	StringBuilder scrshot_pathname = new StringBuilder();
    	StringBuilder appName = new StringBuilder();
    	StringBuilder viewText = new StringBuilder();
    	
    	// Text verification method
		popup_frame.setSize(400, 280);
		popup_frame.setTitle("Text Verification");
		popup_frame.setLayout(new BorderLayout());
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
		JComboBox<String> comboxAppName = new JComboBox<String>();
    	JComboBox<String> comboxViewName = new JComboBox<String>();
    	JComboBox<String> comboxEleName = new JComboBox<String>();
    	JPanel text_pane = new JPanel();
		JPanel text_btn_pane = new JPanel();
		JTextArea ver_text_input = new JTextArea(8,30);
		ver_text_input.setLineWrap(true);
		
		addEleCombox(comboxAppName, comboxViewName, comboxEleName, xpath, cus_name, scrshot_pathname, appName, ver_text_input);
		
		MyIconButton buttonVersetTX_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
		MyIconButton buttonVersetTX_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
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
                	table_row[3] = (String)comboxViewName.getSelectedItem();
                	table_row[4] = (String)comboxEleName.getSelectedItem();
                	
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
		popup_frame.setVisible(true);
    }
    
    private void eleVer() {
    	StringBuilder xpath = new StringBuilder();
    	StringBuilder cus_name = new StringBuilder();
    	StringBuilder scrshot_pathname = new StringBuilder();
    	StringBuilder appName = new StringBuilder();
    	
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
		JComboBox<String> comboxAppName = new JComboBox<String>();
    	JComboBox<String> comboxViewName = new JComboBox<String>();
    	JComboBox<String> comboxEleName = new JComboBox<String>();
		addEleCombox(comboxAppName, comboxViewName, comboxEleName, xpath, cus_name, scrshot_pathname, appName, null);
		
		MyIconButton buttonVersetEE_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
		MyIconButton buttonVersetEE_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
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
		popup_frame.setVisible(true);
    }
    
    private void actionTextWin(BrickBean brick, int winType) {
    	popup_frame.setSize(270, 130);
    	popup_frame.setTitle("Text Window");
		popup_frame.setLayout(new BorderLayout());
		JPanel text_set_pane = new JPanel();
		text_set_pane.setPreferredSize(new Dimension(270, 50));
		JLabel text_set_label = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.btntip.textset"));
		text_set_label.setFont(ConstantsUI.FONT_NORMAL);
		JTextField text_content = new JTextField();
		text_content.setPreferredSize(new Dimension(160,20));
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
					if (winType == 1) {
						table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.act");
						table_row[2] = PropertyUtil.getProperty("bricks.ui.casecre.btntip.textset");
						table_row[3] = text_content.getText();
				    	table_row[4] = "N/A";
	                	
	                	Map<String, Object> params_set_text = new HashMap<>();
	                    params_set_text.put("inputText", text_content.getText());
	                    if (brick != null) {
	                    	brick.setParams(params_set_text);
	                    }
					} else if (winType == 2) {
						table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.act");
						table_row[2] = PropertyUtil.getProperty("bricks.ui.casecre.act.spinner");
						table_row[3] = text_content.getText();
						table_row[4] = "N/A";
						
						Map<String, Object> params_spinner = new HashMap<>();
						params_spinner.put("choose", text_content.getText());
						if (brick != null) {
							brick.setParams(params_spinner);
						}
					} else if (winType == 3) {
						table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.act");
						table_row[2] = PropertyUtil.getProperty("bricks.ui.casecre.adbcmd");
						table_row[3] = text_content.getText();
						table_row[4] = "N/A";
						
						Map<String, Object> params_adb = new HashMap<>();
						params_adb.put("cmd", text_content.getText());
						if (brick != null) {
							brick.setParams(params_adb);
						}
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
		popup_frame.setVisible(true);
    }
    
    private void actionScroll(BrickBean brick) {
    	StringBuilder scrollPos = new StringBuilder();
    	
    	StringBuilder tarXpath = new StringBuilder();
    	StringBuilder tarCus_name = new StringBuilder();
    	StringBuilder tarScrshot_pathname = new StringBuilder();
    	StringBuilder tarAppName = new StringBuilder();
    	
    	StringBuilder conXpath = new StringBuilder();
    	StringBuilder conCus_name = new StringBuilder();
    	StringBuilder conScrshot_pathname = new StringBuilder();
    	StringBuilder conAppName = new StringBuilder();
    	
    	popup_frame.setSize(300, 500);
		popup_frame.setTitle("Scroll in container");
		popup_frame.setLayout(new GridLayout(10,1));
		
		JPanel scroll_position_pane = new JPanel();
		JPanel target_app_name = new JPanel();
		JPanel target_app_view = new JPanel();
		JPanel target_ele_name = new JPanel();
		JPanel container_app_name = new JPanel();
		JPanel container_app_view = new JPanel();
		JPanel container_ele_name = new JPanel();
		JPanel scroll_way_pane = new JPanel();
		JPanel heading_pane = new JPanel();
		JPanel button_pane = new JPanel();
		
		JLabel scroll_position = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.position"));
		scroll_position.setFont(ConstantsUI.FONT_NORMAL);
		JLabel tar_app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.tarappname"));
		tar_app_name.setFont(ConstantsUI.FONT_NORMAL);
		JLabel tar_app_view = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.tarappview"));
		tar_app_view.setFont(ConstantsUI.FONT_NORMAL);
		JLabel tar_ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.tarelename"));
		tar_ele_name.setFont(ConstantsUI.FONT_NORMAL);
		JLabel con_app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.conappname"));
		con_app_name.setFont(ConstantsUI.FONT_NORMAL);
		JLabel con_app_view = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.conappview"));
		con_app_view.setFont(ConstantsUI.FONT_NORMAL);
		JLabel con_ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.conelename"));
		con_ele_name.setFont(ConstantsUI.FONT_NORMAL);
		JLabel heading = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.heading"));
		heading.setFont(ConstantsUI.FONT_NORMAL);
		JLabel scroll_way = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.scroll.way"));
		scroll_way.setFont(ConstantsUI.FONT_NORMAL);
		
		JComboBox<String> comboxScrollPos = new JComboBox<>();
		comboxScrollPos.addItem("From quarter");
		comboxScrollPos.addItem("From half");
		comboxScrollPos.addItem("From Bottom");
		comboxScrollPos.setEditable(false);
		comboxScrollPos.setSelectedItem(null);
		comboxScrollPos.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		comboxScrollPos.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				try {
					scrollPos.delete(0, scrollPos.length()).append(new String(((String) e.getItem()).getBytes(), "UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JComboBox<String> comboxTarAppName = new JComboBox<String>();
    	JComboBox<String> comboxTarViewName = new JComboBox<String>();
    	JComboBox<String> comboxTarEleName = new JComboBox<String>();
    	addEleCombox(comboxTarAppName, comboxTarViewName, comboxTarEleName, tarXpath, tarCus_name, tarScrshot_pathname, tarAppName, null);
    	
    	JComboBox<String> comboxConAppName = new JComboBox<String>();
    	JComboBox<String> comboxConViewName = new JComboBox<String>();
    	JComboBox<String> comboxConEleName = new JComboBox<String>();
    	addEleCombox(comboxConAppName, comboxConViewName, comboxConEleName, conXpath, conCus_name, conScrshot_pathname, conAppName, null);
    	
    	JComboBox<String> comboxHeading = new JComboBox<>();
    	comboxHeading.addItem("UP");
    	comboxHeading.addItem("DOWN");
    	comboxHeading.setEditable(false);
    	comboxHeading.setSelectedItem(null);
    	comboxHeading.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
    	comboxHeading.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				try {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (new String(((String) e.getItem()).getBytes(), "UTF-8").equals("UP"))
							headingTag = 1;
						else if (new String(((String) e.getItem()).getBytes(), "UTF-8").equals("DOWN"))
							headingTag = 0;
					}
				} catch(UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		});
    	
    	JComboBox<String> comboxWay = new JComboBox<>();
    	comboxWay.addItem("To exact element");
    	comboxWay.addItem("From specific position");
    	comboxWay.setEditable(false);
    	comboxWay.setSelectedItem(null);
    	comboxWay.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
    	comboxWay.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				try {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (new String(((String) e.getItem()).getBytes(), "UTF-8").equals("To exact element"))
							scrollTag = 0;
						else if (new String(((String) e.getItem()).getBytes(), "UTF-8").equals("From specific position"))
							scrollTag = 1;
					}
				} catch(UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		});
    	
    	MyIconButton buttonScroll_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
    	MyIconButton buttonScroll_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
		
    	buttonScroll_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.btntip.act.scroll");
					table_row[2] = "N/A";
                	table_row[3] = "N/A";
                	table_row[4] = "N/A";
                	
                	if (scrollTag == 0) {
	                    String ele_path_tar = tarXpath.toString();
	                    String ele_path_con = conXpath.toString();
	                    Map<String, Object> params_scroll_xpath = new HashMap<>();
	                    params_scroll_xpath.put("elePath", ele_path_tar);
	                    params_scroll_xpath.put("containerPath", ele_path_con);
	                    params_scroll_xpath.put("heading", headingTag);
	                    if (brick != null)
	                    	brick.setParams(params_scroll_xpath);
                	} else if (scrollTag == 1) {
                		String scroll_position = scrollPos.toString();
                		String ele_path_con = conXpath.toString();
                		Map<String, Object> params_scroll_pos = new HashMap<>();
                		params_scroll_pos.put("scrollPos", scroll_position);
                		params_scroll_pos.put("containerPath", ele_path_con);
                		params_scroll_pos.put("heading", headingTag);
                		if (brick != null)
                			brick.setParams(params_scroll_pos);
                	}
                	
                    int i = casetable.getSelectedRow();
        	        if(i >= 0){
        	        	model.insertRow(i+1, table_row);
        	        	caseList.add(i+1, brick);
        	        }else{
        	        	model.addRow(table_row);
        	        	caseList.add(brick);
        	        }
				} catch(Exception e1) {
					e1.printStackTrace();
				}
				popup_frame.dispatchEvent(new WindowEvent(popup_frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
    	buttonScroll_re.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
    	scroll_way_pane.add(scroll_way);
    	scroll_way_pane.add(comboxWay);
    	scroll_position_pane.add(scroll_position);
    	scroll_position_pane.add(comboxScrollPos);
		target_app_name.add(tar_app_name);
		target_app_name.add(comboxTarAppName);
		target_app_view.add(tar_app_view);
		target_app_view.add(comboxTarViewName);
		target_ele_name.add(tar_ele_name);
		target_ele_name.add(comboxTarEleName);
		container_app_name.add(con_app_name);
		container_app_name.add(comboxConAppName);
		container_app_view.add(con_app_view);
		container_app_view.add(comboxConViewName);
		container_ele_name.add(con_ele_name);
		container_ele_name.add(comboxConEleName);
		heading_pane.add(heading);
		heading_pane.add(comboxHeading);
		button_pane.add(buttonScroll_add);
		button_pane.add(buttonScroll_re);
		popup_frame.add(scroll_way_pane);
		popup_frame.add(scroll_position_pane);
		popup_frame.add(target_app_name);
		popup_frame.add(target_app_view);
		popup_frame.add(target_ele_name);
		popup_frame.add(container_app_name);
		popup_frame.add(container_app_view);
		popup_frame.add(container_ele_name);
		popup_frame.add(heading_pane);
		popup_frame.add(button_pane);
		popup_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
		popup_frame.setLocationRelativeTo(MainEntry.frame);
		popup_frame.setVisible(true);
    }
    
    private void addEleCombox(JComboBox<String> comboxAppName, JComboBox<String> comboxViewName, JComboBox<String> comboxEleName,
    		StringBuilder xpath, StringBuilder cus_name, StringBuilder scrshot_pathname, StringBuilder appName, JTextArea textArea) {
    	
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
		
		comboxViewName.setEditable(true);
		comboxViewName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxEleName.setEditable(true);
		comboxEleName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		EventList<String> actEventList = new BasicEventList<>();
		EventList<String> eleEventList = new BasicEventList<>();
		AutoCompleteSupport.install(comboxViewName, actEventList);
		AutoCompleteSupport.install(comboxEleName, eleEventList);
		JTextField textView = (JTextField) comboxViewName.getEditor().getEditorComponent();
		JTextField textEle = (JTextField) comboxEleName.getEditor().getEditorComponent();
		
		textView.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				actEventList.clear();
				comboxEleName.setSelectedItem(null);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ResultSet rs = null;
				try {
					appName.delete(0, appName.length()).append(new String(((String) comboxAppName.getSelectedItem()).getBytes(), "UTF-8"));
					rs = sql.queryElement("ACTIVITY", appName.toString());
					
					ArrayList<String> actList = new ArrayList<>();
					
					while (rs.next()) {
						actList.add(new String(rs.getBytes("ACTIVITY_NAME"), "UTF-8"));
					}
					actEventList.addAll(actList);
				} catch (UnsupportedEncodingException | SQLException e1) {
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
		});
		
		textEle.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				eleEventList.clear();
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				String viewName = textView.getText();
				ResultSet rs = sql.queryElement("ELEMENT", appName.toString(), viewName);
				
				ArrayList<String> eleList = new ArrayList<>();
				try {
					rs.next();
					String eleFirst = new String(rs.getBytes(1), "UTF-8");
					eleEventList.add(eleFirst);
					while ((rs.next())) {
						String eleName = rs.getString(1);
						eleList.add(eleName);
					}
					eleEventList.addAll(eleList);
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
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
		});
		
		comboxViewName.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				eleEventList.clear();
			}
		});
		
		comboxEleName.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String ele_cus = (String) e.getItem();
					xpathSet = sql.queryElement("ELEMENT", comboxAppName.getSelectedItem().toString(), comboxViewName.getSelectedItem().toString(), ele_cus);
				}
				
				try {
					while (xpathSet.next()) {
						xpath.delete(0, xpath.length()).append(xpathSet.getString("XPATH"));
						cus_name.delete(0, cus_name.length()).append(xpathSet.getString("CUSTOM_NAME"));
						scrshot_pathname.delete(0, scrshot_pathname.length()).append(xpathSet.getString("SCREEN_PATH"));
						String state = xpathSet.getString(5);
						if (textArea != null)
							textArea.setText(xpathSet.getString("VIEW_TEXT"));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					
				}
			}
		});
	}
    
    private void cmpVer() {
    	StringBuilder xpath = new StringBuilder();
    	StringBuilder cus_name = new StringBuilder();
    	StringBuilder scrshot_pathname = new StringBuilder();
    	StringBuilder appName = new StringBuilder();
    	
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
		JComboBox<String> comboxAppName = new JComboBox<String>();
    	JComboBox<String> comboxViewName = new JComboBox<String>();
    	JComboBox<String> comboxEleName = new JComboBox<String>();
		addEleCombox(comboxAppName, comboxViewName, comboxEleName, xpath, cus_name, scrshot_pathname, appName, null);
		
		MyIconButton buttonVersetEE_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
		MyIconButton buttonVersetEE_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
		
		// inside-button method
		buttonVersetEE_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.cmpver");
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
					BrickBean brick_valCmp = new BrickBean();
					brick_valCmp.setProperty("val");
					brick_valCmp.setValidation_name(3);
					Map<String, Object> params_eleCmp = new HashMap<>();
					params_eleCmp.put("ele_path", ele_path_eleVal);
					brick_valCmp.setParams(params_eleCmp);
					caseList.add(brick_valCmp);
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
		popup_frame.setVisible(true);
    }
}
