package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.MainEntry;
import com.dji.bricks.UI.BrickBean;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.backgrounder.ExecutionMain;
import com.dji.bricks.node_selection.RealTimeScreenUI;
import com.dji.bricks.tools.PropertyUtil;
import com.dji.bricks.tools.SQLUtils;

/**
 * Case create page 
 * @author DraLastat
 */
public class CasecrePanel extends JPanel implements Observer, GlobalObserver{
	
	private static final long serialVersionUID = 1L;
	
	private static MyIconButton buttonEleAdd;
	private static MyIconButton buttonActAdd;
	private static MyIconButton buttonVerAdd;
	private static MyIconButton buttonRowDelete;
	private static MyIconButton buttonEleRefresh;
	private static MyIconButton buttonActRefresh;
	private static MyIconButton buttonSave;
	private static MyIconButton buttonScrshot;
	private static MyIconButton buttonDocRead;
	private static MyIconButton buttonJsonLoad;
	private static MyIconButton buttonPlayList;
	private static MyIconButton buttonRTChart;
	private static JPanel popuppanel;
//	private static PopupWindow popupwindow;
	private JTextArea logArea;
	private static MyIconButton buttonTimer;
	private static MyIconButton buttonVersetTX_add;
	private static MyIconButton buttonVersetTX_re;
	private static MyIconButton buttonVersetEE_add;
	private static MyIconButton buttonVersetEE_re;
	private static MyIconButton buttonVersetTimer_add;
	private static MyIconButton buttonVersetTimer_re;
	private int ver_type;
	private LinkedList<BrickBean> caseList = new LinkedList<>();
	
	private String xpath = "";
	private String cus_name = "";
	private String act_name = "";
	private String val = "";
	private int action;
	private IDevice device;
	
	/**
	 * Initialize
	 */
	public CasecrePanel(SQLUtils sql) {
		this.sql = sql;
		initialize();
		addComponent();
		addListener();
	}

	private void initialize() {
		this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
	}

	private JPanel CaseCre;
	private void addComponent() {
		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(getDownPanel(), BorderLayout.SOUTH);
	}
	
	/**
	 * Title Panel 
	 * @return
	 */
	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		JLabel labelTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.title"));
		labelTitle.setFont(ConstantsUI.FONT_TITLE);
		labelTitle.setForeground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);
		return panelUp;
	}
	
	/**
	 * Element Adding Panel
	 */
	private JComboBox<String> comboxAppName;
	private JComboBox<String> comboxViewName;
	private JComboBox<String> comboxEleName;
	private JComboBox<String> comboxActName;
	private JComboBox<String> comboxVerName;
	private JTable casetable;
	private DefaultTableModel model;
	private String appName = "";
	private String appStartName = "";
	private String pkg = "";
	private SQLUtils sql = null;
	private ResultSet xpathSet = null;
	private PrintStream standardOut;
	private Object[] table_row = new Object[5];
	
	private ViewListener vlisten = new ViewListener();
	private EleListener elisten = new EleListener();
	
	/**
	 * case list set to Jtable
	 * @return
	 */
	private JPanel getCenterPanel(){
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(ConstantsUI.TABLE_LINE_COLOR);
		JPanel TablePanel = new JPanel();
		TablePanel.setPreferredSize(new Dimension(810, 250));
        casetable = new JTable(); 
        Object[] columns = {"Id","Type","SPEC 1","SPEC 2","SPEC 3"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        casetable.setModel(model);
        casetable.setBackground(Color.LIGHT_GRAY);
        casetable.setForeground(Color.black);
        Font font = new Font("",1,10);
        casetable.setFont(font);
        casetable.setRowHeight(30);
        casetable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        TablePanel.add(new JScrollPane(casetable));
        panelCenter.add(TablePanel);
        panelCenter.updateUI();
		
		return  panelCenter;
	}
	
	/**
	 * caselist editing & log print Panel 
	 * @return
	 */
	private JPanel getDownPanel() {
		JPanel panelDown = new JPanel();
		panelDown.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelDown.setPreferredSize(new Dimension(810, 320));
		panelDown.setLayout(new GridLayout(1,2));
		JPanel LeftPanel = new JPanel();
		JPanel RightPanel = new JPanel();

		// Database input panel
		JPanel DataGrid = new JPanel();
		JLabel DataSrc = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.datasrc"));
		DataSrc.setFont(ConstantsUI.FONT_NORMAL);
		DataGrid.setPreferredSize(new Dimension(420, 40));
		DataGrid.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		DataGrid.setBackground(ConstantsUI.TABLE_LINE_COLOR);
		JTextField DataFrom = new JTextField();
		DataFrom.setPreferredSize(new Dimension(180, 24));
		buttonDocRead = new MyIconButton(ConstantsUI.ICON_DOCREAD, ConstantsUI.ICON_DOCREAD_ENABLE,
                ConstantsUI.ICON_DOCREAD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.docread"));
		buttonJsonLoad = new MyIconButton(ConstantsUI.ICON_JSONLOAD, ConstantsUI.ICON_JSONLOAD_ENABLE,
                ConstantsUI.ICON_JSONLOAD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.jsonload"));
		DataGrid.add(DataSrc);
		DataGrid.add(DataFrom);
		DataGrid.add(buttonDocRead);
		DataGrid.add(buttonJsonLoad);
		
		//Bricks adding panel
		JPanel ElePanel_APP = new JPanel();
		ElePanel_APP.setPreferredSize(new Dimension(420, 40));
		ElePanel_APP.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		ElePanel_APP.setBackground(ConstantsUI.TABLE_LINE_COLOR);
		JPanel ElePanel_View = new JPanel();
		ElePanel_View.setPreferredSize(new Dimension(420, 40));
		ElePanel_View.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		ElePanel_View.setBackground(ConstantsUI.TABLE_LINE_COLOR);
		JPanel ElePanel_Name = new JPanel();
		ElePanel_Name.setPreferredSize(new Dimension(420, 40));
		ElePanel_Name.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		ElePanel_Name.setBackground(ConstantsUI.TABLE_LINE_COLOR);
		JLabel Ele_Null = new JLabel();
		Ele_Null.setPreferredSize(new Dimension(50,35));
		JLabel Ele_Null2 = new JLabel();
		Ele_Null2.setPreferredSize(new Dimension(50,35));
		JLabel ElePick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elepick"));
		ElePick.setFont(ConstantsUI.FONT_NORMAL);
		buttonScrshot = new MyIconButton(ConstantsUI.ICON_SCRSHOT, ConstantsUI.ICON_SCRSHOT_ENABLE,
                ConstantsUI.ICON_SCRSHOT_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.screenshot"));
		buttonEleAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addele"));
		buttonEleRefresh = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.reele"));

		comboxAppName = new JComboBox<String>();
		comboxAppName.addItem("RM500 Launcher");
		comboxAppName.addItem("DJI GO3");
		comboxAppName.addItem("DJI GO4");
		comboxAppName.addItem("DJI Pilot");
		comboxAppName.setEditable(false);
		comboxAppName.setSelectedItem(null);
		comboxAppName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxViewName = new JComboBox<String>();
		comboxViewName.setEditable(false);
		comboxViewName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxEleName = new JComboBox<String>();
		comboxEleName.setEditable(false);
		comboxEleName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);

		
		// App_Name selection change listener
		comboxAppName.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					appName = (String) e.getItem();
					ResultSet rs = sql.queryElement("ACTIVITY", appName);
					
					comboxViewName.removeAllItems();
					comboxEleName.removeAllItems();
					comboxViewName.removeItemListener(vlisten);
					try {
						while (rs.next()) {
							String viewName = rs.getString("ACTIVITY_NAME");
							comboxViewName.addItem(viewName);
						}
						comboxViewName.setSelectedItem(null);
						comboxViewName.addItemListener(vlisten);
						comboxViewName.updateUI();
						comboxEleName.updateUI();
					} catch (SQLException e1) {
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
		
		ElePanel_APP.add(ElePick);
		ElePanel_APP.add(comboxAppName);
		ElePanel_View.add(Ele_Null);
		ElePanel_View.add(comboxViewName);
		ElePanel_Name.add(Ele_Null2);
		ElePanel_Name.add(comboxEleName);
		ElePanel_Name.add(buttonScrshot);
		ElePanel_Name.add(buttonEleAdd);
		ElePanel_Name.add(buttonEleRefresh);

		
		JPanel ActPanel = new JPanel();
		ActPanel.setPreferredSize(new Dimension(420, 40));
		ActPanel.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		ActPanel.setBackground(ConstantsUI.TABLE_LINE_COLOR);
		JLabel ActPick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.actpick"));
		ActPick.setFont(ConstantsUI.FONT_NORMAL);
		comboxActName = new JComboBox<String>();
		comboxActName.setEditable(false);
		comboxActName.setSelectedItem(null);
		comboxActName.addItem("Single-Click");
		comboxActName.addItem("Long-Press");
		comboxActName.addItem("SetText");
		comboxActName.addItem("DragBar");
		comboxActName.setSelectedItem(null);
		comboxActName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		comboxActName.addItemListener(new ActListener());
		JLabel ActNull = new JLabel();
		ActNull.setPreferredSize(new Dimension(35, 40));
		buttonActAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addact"));
		buttonActRefresh = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.react"));
		ActPanel.add(ActPick);
		ActPanel.add(comboxActName);
		ActPanel.add(ActNull);
		ActPanel.add(buttonActAdd);
		ActPanel.add(buttonActRefresh);
		
		JPanel VerPanel = new JPanel();
		VerPanel.setPreferredSize(new Dimension(420, 40));
		VerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		VerPanel.setBackground(ConstantsUI.TABLE_LINE_COLOR);
		JLabel VerPick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.verpick"));
		VerPick.setFont(ConstantsUI.FONT_NORMAL);
		comboxVerName = new JComboBox<String>();
		comboxVerName.addItem("Text Validation");
		comboxVerName.addItem("Image Validation");
		comboxVerName.addItem("Element Exist Validation");
		comboxVerName.setEditable(false);
		comboxVerName.setSelectedItem(null);
		comboxVerName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxVerName.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					switch ((String) e.getItem()) {
					case "Text Validation":
						ver_type = 1;
						val = "TV";
						break;
					case "Image Validation":
						ver_type = 2;
						val = "IV";
						break;
					case "Element Exist Validation":
						ver_type = 3;
						val = "EEV";
						break;
					}
				}
			}
		});
		
		JLabel VerNull = new JLabel();
		VerNull.setPreferredSize(new Dimension(35, 40));
		buttonVerAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
		VerPanel.add(VerPick);
		VerPanel.add(comboxVerName);
		VerPanel.add(VerNull);
		VerPanel.add(buttonVerAdd);
		
		JPanel RunPanel = new JPanel();
		RunPanel.setPreferredSize(new Dimension(420, 40));
		RunPanel.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		buttonTimer = new MyIconButton(ConstantsUI.ICON_TIMER, ConstantsUI.ICON_TIMER_ENABLE,
				ConstantsUI.ICON_TIMER_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.timer"));
		buttonRowDelete = new MyIconButton(ConstantsUI.ICON_ROW_DELETE, ConstantsUI.ICON_ROW_DELETE_ENABLE,
                ConstantsUI.ICON_ROW_DELETE_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rowdelete"));
		buttonSave = new MyIconButton(ConstantsUI.ICON_LIST_SAVE, ConstantsUI.ICON_LIST_SAVE_ENABLE,
				ConstantsUI.ICON_LIST_SAVE_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.listsave"));
		buttonPlayList = new MyIconButton(ConstantsUI.ICON_PLAY_LIST, ConstantsUI.ICON_PLAY_LIST_ENABLE,
				ConstantsUI.ICON_PLAY_LIST_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.playlist"));
		buttonRTChart = new MyIconButton(ConstantsUI.ICON_RTCHART, ConstantsUI.ICON_RTCHART_ENABLE,
				ConstantsUI.ICON_RTCHART_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rtchart"));
		JLabel TableNull = new JLabel();
		TableNull.setPreferredSize(new Dimension(75, 40));
		RunPanel.add(buttonTimer);
		RunPanel.add(buttonRowDelete);
		RunPanel.add(TableNull);
		RunPanel.add(buttonPlayList);
		RunPanel.add(buttonRTChart);
		RunPanel.add(buttonSave);
		
		JPanel panelLog = new JPanel();
		panelLog.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelLog.setLayout(new BorderLayout());
		logArea = new JTextArea(17, 50);
		logArea.setBackground(ConstantsUI.LOG_COLOR);
		logArea.setForeground(ConstantsUI.MAIN_BACK_COLOR);
		logArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(logArea));
//		standardOut = System.out;
//        // re-assigns standard output stream and error output stream
//        System.setOut(printStream);
//        System.setErr(printStream);
        
        panelLog.add(new JScrollPane(logArea));
		
        LeftPanel.add(DataGrid);
        LeftPanel.add(ElePanel_APP);
        LeftPanel.add(ElePanel_View);
        LeftPanel.add(ElePanel_Name);
        LeftPanel.add(ActPanel);
        LeftPanel.add(VerPanel);
        LeftPanel.add(RunPanel);
        RightPanel.add(panelLog);
        panelDown.add(LeftPanel);
        panelDown.add(RightPanel);
		
		return panelDown;
	}
	
	// Bricks button listener
	public void addListener() {
		
		buttonEleAdd.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
                try {
                	table_row[1] = "ELE";
                	table_row[2] = comboxAppName.getSelectedItem();
                	table_row[3] = comboxViewName.getSelectedItem();
                	table_row[4] = comboxEleName.getSelectedItem();
            	    model.addRow(table_row);
                	BrickBean brick = new BrickBean();
                	brick.setEle_xpath(xpath);
                	brick.setCustom_name(cus_name);
                	brick.setProperty("ele");
                	if (caseList.size() == 0)
                		appStartName = appName;
                	
                	caseList.add(brick);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
		
	  	buttonActAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	if(action == 0){
                		act_name = "CK";
                	}else if(action == 1){
                		act_name = "LP";
                	}else if(action == 2){
                		act_name = "ST";
                	}else if(action == 10){
                		act_name = "DB";
                	}
                	table_row[1] = "ACT";
                	table_row[2] = act_name;
                	table_row[3] = "N/A";
                	table_row[4] = "N/A";
                	model.addRow(table_row);
            	    BrickBean brick = new BrickBean();
                	brick.setAction_name(action);
                	brick.setProperty("act");
                	caseList.add(brick);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
	  	
	  	buttonVerAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	new VerifiWindow(ver_type);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
	  	
	  	buttonRowDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
                    // i = the index of the selected row
                    int i = casetable.getSelectedRow();
                    if(i >= 0){
                        model.removeRow(i);
                    }
                    else{
                        System.out.println("Delete Error");
                    }
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
	  	});
	  	
	  	buttonTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	new VerifiWindow(ver_type = 4);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });

	  	buttonRowDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            try {
	                // i = the index of the selected row
	                int i = casetable.getSelectedRow();
	                if(i >= 0){
	                    model.removeRow(i);
	                }
	                else{
	                    System.out.println("Delete Error");
	                }
	            } catch (Exception e1) {
	            	e1.printStackTrace();
	            }
            }
        });
	  	
	  	buttonEleRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try{
                // i = the index of the selected row
                int i = casetable.getSelectedRow();
                
                if(i >= 0) 
                {
                   model.setValueAt(cus_name, i, 1);
                   model.setValueAt(cus_name, i, 2);
                   model.setValueAt(cus_name, i, 3);
                   model.setValueAt(cus_name, i, 4);
                }
                else{
                    System.out.println("Update Ele Error");
                	}
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
	  	
	  	buttonActRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try{
                // i = the index of the selected row
                int i = casetable.getSelectedRow();
                
                if(i >= 0) 
                {
                   model.setValueAt(act_name, i, 1);
                }
                else{
                    System.out.println("Update Act Error");
                	}
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
	  	
	  	buttonEleRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try{
                // i = the index of the selected row
                int i = casetable.getSelectedRow();
                if(i >= 0) 
                {
                   model.setValueAt(cus_name, i, 1);
                   model.setValueAt(cus_name, i, 2);
                   model.setValueAt(cus_name, i, 3);
                   model.setValueAt(cus_name, i, 4);
                }
                else{
                    System.out.println("Update Ele Error");
                	}
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
	  	
	  	buttonActRefresh.addActionListener(new ActionListener() {
	  		@Override
	  		public void actionPerformed(ActionEvent e) {
	  			try{
	  				// i = the index of the selected row
	  				int i = casetable.getSelectedRow();
	  				if(i >= 0) 
	  				{
	  					model.setValueAt(act_name, i, 1);
	  				}
	  				else{
	  					System.out.println("Update Act Error");
	  				}
	  			} catch (Exception e1) {
	  				e1.printStackTrace();
                }

            }
        });

	  	buttonPlayList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	String str = JSON.toJSONString(caseList);
            	System.out.println(str);
            	JSONArray jsonFile = JSON.parseArray(str);
        	
            	switch (appStartName) {
            		case "DJI GO4":
            			pkg = "dji.go.v4";
            			break;
            		case "DJI GO3":
            			pkg = "dji.pilot";
            			break;
            		case "RM500 Launcher":
            			pkg = "com.dpad.launcher";
            			break;
            		case "RM500 Settings":
            			pkg = "com.android.settings.Settings";
            			break;
            	}
            	
            	try {
            		Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ExecutionMain.getInstance().RunTestCase(jsonFile, logArea, device, pkg);
		            		RealTimeScreenUI.isRuncase = true;
						}
					});
            		MainEntry.cachedThreadPool.submit(thread);
            		
            	}catch (Exception e1) {
            		e1.printStackTrace();
            	}
        	}
        });
	  	
	  	buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	JDialog savewindow = new JDialog();
                	savewindow.setVisible(true);
                	SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
                	String time = timeFormat.format(Calendar.getInstance().getTime());
                	
                	File json = new File("json/" + appName + "_" + time + ".json");
                	if (!json.getParentFile().exists())
                		json.getParentFile().mkdirs();
                	
                	String str = JSON.toJSONString(caseList);
                	PrintWriter pw = new PrintWriter(new FileWriter(json));
                    pw.print(str);
                    pw.flush();
                    pw.close();
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
	}
	
	// Log print thread
	// TODO adding real time log in here
    private void printLog() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("Time now is " + (new Date()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    // JTextArea output method
    class CustomOutputStream extends OutputStream {
        private JTextArea textArea;
         
        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }
         
        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.append(String.valueOf((char)b));
            // scrolls the text area to the end of data
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }  
    
	class ViewListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String viewName = (String) e.getItem();
				ResultSet rs = sql.queryElement("ELEMENT", appName, viewName);
				
				comboxEleName.removeAllItems();
				comboxEleName.removeItemListener(elisten);
				try {
					rs.next();
					String eleFirst = rs.getString(1);
					comboxEleName.addItem(eleFirst);
					while ((rs.next())) {
						String eleName = rs.getString(1);
						comboxEleName.addItem(eleName);
					}
					comboxEleName.setSelectedItem(null);
					comboxEleName.addItemListener(elisten);
				} catch (SQLException e1) {
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
	
	class EleListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String ele_cus = (String) e.getItem();
				xpathSet = sql.queryElement("ELEMENT", ele_cus);
			}
			
			try {
				while (xpathSet.next()) {
					xpath = xpathSet.getString("XPATH");
					cus_name = xpathSet.getString("CUSTOM_NAME");
					String state = xpathSet.getString(5);
					for (int i = 0; i < state.length(); i++) {
						if (state.charAt(i) == '1') {
					        switch (i) {
					        case 0:
					        	comboxActName.addItem("click");
					        	break;
					        case 1:
					        	comboxActName.addItem("scroll");
					        	break;
					        case 2:
					        	comboxActName.addItem("check");
					        	break;
					        case 3:
					        	comboxActName.addItem("focus");
					        	break;
					        case 4:
					        	comboxActName.addItem("long-click");
					        	break;
					        }
						}
				    }
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				
			}
		}
		
	}
	
	class ActListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String action_name = (String) e.getItem();
				switch (action_name) {
				case "click":
					action = 0;
					break;
				case "longPress":
					action = 1;
					break;
				case "setText":
					action = 2;
					break;
				case "dragBar":
					action = 10;
					break;
				}
			}
		}
	}

	public void observe(Observable o) {
	    o.addObserver(this);
	}
	
	@Override
	public void frameImageChange(BufferedImage image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ADBChange(IDevice[] devices) {
		// TODO Auto-generated method stub
		this.device = devices[0];
	}

	
	class VerifiWindow extends JFrame{
		// init popup window
		JFrame ver_setting_frame = new JFrame();

	    public VerifiWindow(int type) {
	    	if(ver_type == 1){
	    		// Text verification method
	    		ver_setting_frame.setSize(400, 300);
	    		ver_setting_frame.setTitle("Text Verification");
	    		ver_setting_frame.setVisible(true);
	    		ver_setting_frame.setLayout(new BorderLayout());
	    		JPanel text_pane = new JPanel();
//	    		text_pane.setBackground(Color.gray);
	    		JPanel text_btn_pane = new JPanel();
	    		JTextArea ver_text_input = new JTextArea(11,45);
	    		ver_text_input.setLineWrap(true);
	    		buttonVersetTX_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
	                    ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
	    		buttonVersetTX_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
	                    ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
	    		text_pane.add(new JScrollPane(ver_text_input));
	    		text_btn_pane.add(buttonVersetTX_add);
	    		text_btn_pane.add(buttonVersetTX_re);
	    		ver_setting_frame.add(text_pane, BorderLayout.NORTH);
	    		ver_setting_frame.add(text_btn_pane, BorderLayout.SOUTH);
	    		// kill the thread, same to the others ver_type case
	    		ver_setting_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    		ver_setting_frame.setVisible(true);
	    		// set the popup window's position releat to Main frame
	    		ver_setting_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
	    		ver_setting_frame.setLocationRelativeTo(MainEntry.frame);

	    		// inside-button method
	    		buttonVersetTX_add.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {

		                try {
		                	table_row[1] = "Ver_text";
		                	table_row[2] = "Text";
		                	table_row[3] = "N/A";
		                	table_row[4] = "N/A";
		                	model.addRow(table_row);
		                	
		                	String ele_path_text = "//android.widget.TextView[@resource-id='dji.go.v4:id/fpv_error_pop_item_title_tv']";
							String expect_text = "自动起飞操作失败";
							
							BrickBean brick_valText = new BrickBean();
							brick_valText.setProperty("val");
							brick_valText.setValidation_name(1);
							Map<String, String> params_text = new HashMap<>();
							params_text.put("ele_path", ele_path_text);
							params_text.put("expect_text", expect_text);
							brick_valText.setParams(params_text);
							caseList.add(brick_valText);
		                } catch (Exception e1) {
		                	e1.printStackTrace();
		                }

		            }
		        });
	    		buttonVersetTX_re.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		            	try{
		                // i = the index of the selected row
		                int i = casetable.getSelectedRow();
		                
		                if(i >= 0) 
		                {
		                   model.setValueAt("ver_text_re", i, 1);
		                }
		                else{
		                    System.out.println("Update Act Error");
		                	}
		                } catch (Exception e1) {
		                	e1.printStackTrace();
		                }
	
		            }
	    		});
	    	}else if (ver_type == 3){
	    		// Element exist verification method
	    		ver_setting_frame.setSize(300, 200);
	    		ver_setting_frame.setTitle("Element Picking");
	    		ver_setting_frame.setLayout(new GridLayout(4,1));
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
	    		
	    		comboxAppName = new JComboBox<String>();
	    		comboxAppName.addItem("DJI GO3");
	    		comboxAppName.addItem("DJI GO4");
	    		comboxAppName.addItem("DJI Pilot");
	    		comboxAppName.addItem("RM500 Launcher");
	    		comboxAppName.addItem("RM500 Settings");
	    		comboxAppName.setEditable(false);
	    		comboxAppName.setSelectedItem(null);
	    		comboxAppName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
	    		
	    		comboxViewName = new JComboBox<String>();
	    		comboxViewName.setEditable(false);
	    		comboxViewName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
	    		
	    		comboxEleName = new JComboBox<String>();
	    		comboxEleName.setEditable(false);
	    		comboxEleName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
	    		
	    		comboxAppName.addItemListener(new ItemListener() {
	    			// combobox item changed method
	    			@Override
	    			public void itemStateChanged(ItemEvent e) {
	    				if (e.getStateChange() == ItemEvent.SELECTED) {
	    					appName = (String) e.getItem();
	    					ResultSet rs = sql.queryElement("ACTIVITY", appName);
	    					
	    					comboxViewName.removeAllItems();
	    					comboxEleName.removeAllItems();
	    					comboxViewName.removeItemListener(vlisten);
	    					try {
	    						while (rs.next()) {
	    							String viewName = rs.getString("ACTIVITY_NAME");
	    							comboxViewName.addItem(viewName);
	    						}
	    						comboxViewName.setSelectedItem(null);
	    						comboxViewName.addItemListener(vlisten);
	    						
	    					} catch (SQLException e1) {
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
	    		buttonVersetEE_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
	                    ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
	    		buttonVersetEE_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
	                    ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
	    		
	    		// inside-button method
	    		buttonVersetEE_add.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {

		                try {
		                	table_row[1] = "Ver";
		                	table_row[2] = comboxAppName.getSelectedItem();
		                	table_row[3] = comboxViewName.getSelectedItem();
		                	table_row[4] = comboxEleName.getSelectedItem();
		                	model.addRow(table_row);
		                	
		                	String ele_path_eleVal = xpath;
							BrickBean brick_valEle = new BrickBean();
							brick_valEle.setProperty("val");
							brick_valEle.setValidation_name(2);
							Map<String, String> params_eleVal = new HashMap<>();
							params_eleVal.put("ele_path", ele_path_eleVal);
							brick_valEle.setParams(params_eleVal);
							caseList.add(brick_valEle);
		                } catch (Exception e1) {
		                	e1.printStackTrace();
		                }

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
	    		ver_setting_frame.add(app_name_pick);
	    		ver_setting_frame.add(app_view_pick);
	    		ver_setting_frame.add(ele_name_pick);
	    		ver_setting_frame.add(button_pane);
	    		ver_setting_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
	    		ver_setting_frame.setLocationRelativeTo(MainEntry.frame);
	    		ver_setting_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    		ver_setting_frame.setVisible(true);
	    	}else if (ver_type == 4){
	    		// Timer adding method
	    		ver_setting_frame.setSize(270, 130);
	    		ver_setting_frame.setTitle("WaitTime Setting");
	    		ver_setting_frame.setLayout(new BorderLayout());
	    		JPanel timer_pane = new JPanel();
	    		timer_pane.setPreferredSize(new Dimension(270, 50));
	    		JLabel timer_label = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.timer"));
	    		timer_label.setFont(ConstantsUI.FONT_NORMAL);
	    		JTextField timer_num = new JTextField();
	    		timer_num.setPreferredSize(new Dimension(80,20));
	    		JPanel timer_btn_pane = new JPanel();
	    		buttonVersetTimer_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
	                    ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.timer"));
	    		buttonVersetTimer_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
	                    ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.retimer"));
	    		// inside-button method
	    		buttonVersetTimer_add.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {

		                try {
		                	table_row[1] = "Timer";
		                	table_row[2] = timer_num.getText() +"  "+ "S";
		                	table_row[3] = "N/A";
		                	table_row[4] = "N/A";
		                	model.addRow(table_row);
		                } catch (Exception e1) {
		                	e1.printStackTrace();
		                }

		            }
		        });
	    		buttonVersetTimer_re.addActionListener(new ActionListener() {
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
	    		timer_btn_pane.add(buttonVersetTimer_add);
	    		timer_btn_pane.add(buttonVersetTimer_re);
	    		ver_setting_frame.add(timer_pane, BorderLayout.NORTH);
	    		ver_setting_frame.add(timer_btn_pane, BorderLayout.SOUTH);
	    		ver_setting_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
	    		ver_setting_frame.setLocationRelativeTo(MainEntry.frame);
	    		ver_setting_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    		ver_setting_frame.setVisible(true);
	    	}

	    }
	}
}
