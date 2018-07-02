package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import com.alibaba.fastjson.JSONObject;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.MainEntry;
import com.dji.bricks.UI.BrickBean;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.EleListener;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.UI.ViewListener;
import com.dji.bricks.backgrounder.ExecutionMain;
import com.dji.bricks.node_selection.RealTimeScreenUI;
import com.dji.bricks.tools.FileUtils;
import com.dji.bricks.tools.PropertyUtil;
import com.dji.bricks.tools.SQLUtils;
import com.dji.bricks.tools.TimeSeriesChart;

/**
 * Case create page 
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
	private static MyIconButton buttonDragAdd;
	private static MyIconButton buttonScrshot;
	private static MyIconButton buttonDocRead;
	private static MyIconButton buttonJsonLoad;
	private static MyIconButton buttonPlayList;
	private static MyIconButton buttonRTChart;
	private static MyIconButton buttonLogSave;
	private JTextArea logArea;
	private static MyIconButton buttonTimer;
	private static MyIconButton buttonVersetTX_add;
	private static MyIconButton buttonVersetTX_re;
	private static MyIconButton buttonVersetEE_add;
	private static MyIconButton buttonVersetEE_re;
	private static MyIconButton buttonVersetTimer_add;
	private static MyIconButton buttonVersetTimer_re;
	private int ver_type;
	private ArrayList<BrickBean> caseList = null;
	private final static String CURRENT_DIR = System.getProperty("user.dir");
	private StringBuilder xpath;
	private StringBuilder cus_name;
	private String act_name = "";
	private StringBuilder scrshot_pathname;
	private int action;
	private int scrshot_X;
	private int scrshot_Y;
	private IDevice device;
	private JSONArray tmpJson;
	private String tmp;
	private int CPUValue;
	private int MemValue;
	
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
	private StringBuilder appName;
	private String appStartName = "";
	private String pkg = "";
	private String filepath;
	private SQLUtils sql = null;
	private ResultSet xpathSet = null;
	private PrintStream standardOut;
	private Object[] table_row = new Object[5];
	private int[] speAddList = {2, 4, 7};
	
	private EleListener elisten;
	private ViewListener vlisten;
	
	private PopUpWindow popWin;
	
	/**
	 * Initialize
	 */
	public CasecrePanel(SQLUtils sql) {
		this.sql = sql;
		this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		addComponent();
		addListener();
		initialize();
	}

	private void initialize() {
		caseList = new ArrayList<>();
		xpath = new StringBuilder();
		cus_name = new StringBuilder();
		scrshot_pathname = new StringBuilder();
		appName = new StringBuilder();
		
		elisten = new EleListener(sql, xpath, cus_name, scrshot_pathname);
		vlisten = new ViewListener(sql, comboxEleName, elisten, appName);
		
		popWin = new PopUpWindow(table_row, model, casetable, sql, caseList);
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
        casetable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
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
	private JTextField DataFrom;
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
		DataFrom = new JTextField();
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

		addEleCombox();
		
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
		comboxActName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		comboxActName.addItem("Single-Click");
    	comboxActName.addItem("Long-Press");
    	comboxActName.addItem("Point Drag");
    	comboxActName.addItem("Set Text");
    	comboxActName.addItem("SeekBar Drag");
    	comboxActName.addItem("Push To Device");
    	comboxActName.addItem("Reboot Device");
    	comboxActName.addItem("Scroll To Exact Ele");
    	comboxActName.addItem("Key HOME");
    	comboxActName.addItem("Key BACK");
    	comboxActName.setSelectedItem(null);
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
		comboxVerName.addItem(PropertyUtil.getProperty("bricks.ui.casecre.textver"));
		comboxVerName.addItem(PropertyUtil.getProperty("bricks.ui.casecre.imgver"));
		comboxVerName.addItem(PropertyUtil.getProperty("bricks.ui.casecre.extver"));
		comboxVerName.setEditable(false);
		comboxVerName.setSelectedItem(null);
		comboxVerName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		comboxVerName.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String item = (String) e.getItem();
				if (PropertyUtil.getProperty("bricks.ui.casecre.textver").equals(item)) {
					ver_type = 1;
				} else if (PropertyUtil.getProperty("bricks.ui.casecre.imgver").equals(item)) {
				} else if (PropertyUtil.getProperty("bricks.ui.casecre.extver").equals(item)) {
					ver_type = 2;
				} else if (item == null){
					ver_type = 0;
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
		buttonLogSave = new MyIconButton(ConstantsUI.ICON_LOGSAVE, ConstantsUI.ICON_LOGSAVE_ENABLE,
				ConstantsUI.ICON_LOGSAVE_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.logsave"));
		JLabel TableNull = new JLabel();
		TableNull.setPreferredSize(new Dimension(27, 40));
		RunPanel.add(buttonTimer);
		RunPanel.add(buttonRowDelete);
		RunPanel.add(TableNull);
		RunPanel.add(buttonPlayList);
		RunPanel.add(buttonRTChart);
		RunPanel.add(buttonSave);
		RunPanel.add(buttonLogSave);
		
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
		
		buttonDocRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jfc = new JFileChooser(new File(CURRENT_DIR + "/json"));  
                    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
                    jfc.showDialog(new JLabel(), "Choose");  
                    File selectedFile = jfc.getSelectedFile();
                    if (selectedFile == null) {
                        appStartName = "";
                    }
                    else {
                    	filepath = jfc.getSelectedFile().getPath();
                        DataFrom.setText(filepath.substring(filepath.lastIndexOf("\\")+1));
                        tmpJson = FileUtils.loadJson(filepath);
                        String filename = filepath.substring(filepath.lastIndexOf("\\")+1);
                        appStartName = filename.substring(0, filename.indexOf("_"));
                        
//                        String str = JSONObject.toJSONString(tmp, SerializerFeature.WriteClassName);
//                        caseList = JSONArray.parseArray(str, BrickBean.class);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

		buttonJsonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                	model.getDataVector().clear();
                	for (int i=0; i<tmpJson.size(); i++) {
                    	String str = JSONObject.toJSONString(tmpJson.get(i));
                    	BrickBean brick = JSON.parseObject(str, BrickBean.class);
                   
                		if (brick.getProperty().equals("ele")) {
                			table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.ele");
                        	table_row[2] = filepath.substring(filepath.lastIndexOf("\\")+1, filepath.lastIndexOf("_"));
                        	table_row[3] = brick.getEle_page();
                        	table_row[4] = brick.getCustom_name();
                        	model.addRow(table_row);
                		} else if (brick.getProperty().equals("act")) {
                			actionLoad(brick.getAction_name(), brick, true);
                        	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.act");
                        	table_row[2] = act_name;
                        	table_row[3] = "N/A";
                        	table_row[4] = "N/A";
                        	model.addRow(table_row);
                		} else if (brick.getProperty().equals("val")) {
                			switch (brick.getValidation_name()) {
                				case 1:
                					table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.textver");
                					table_row[2] = brick.getParams().get("expect_text");
                					table_row[3] = "N/A";
                					table_row[4] = "N/A";
                 					break;
                				case 2:
                					table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.extver");
                					table_row[2] = "N/A";
                					table_row[3] = "N/A";
                					table_row[4] = "N/A";
                					break;
                			}
                			model.addRow(table_row);
                		} else if (brick.getProperty().equals("time")) {
                			table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.timer");
        					table_row[2] = brick.getParams().get("time");
        					table_row[3] = "N/A";
        					table_row[4] = "N/A";
        					model.addRow(table_row);
                		}
                		
                		caseList.add(brick);
                	}
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
		
		buttonEleAdd.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
                try {
                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.ele");
                	table_row[2] = comboxAppName.getSelectedItem();
                	table_row[3] = comboxViewName.getSelectedItem();
                	table_row[4] = comboxEleName.getSelectedItem();
                	
                	BrickBean brick = new BrickBean();
                	brick.setEle_xpath(xpath.toString());
                	brick.setCustom_name(cus_name.toString());
                	brick.setProperty("ele");
                	brick.setEle_page(comboxViewName.getSelectedItem().toString());
                	if (caseList.size() == 0)
                		appStartName = appName.toString();
                	
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
            }
        });
		
	  	buttonActAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	BrickBean brick = new BrickBean();
            	brick.setAction_name(action);
            	brick.setProperty("act");
            	
                try {
                	actionLoad(action, brick, false);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
            }
        });
	  	
	  	buttonVerAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	popWin.popSelect(ver_type, null);
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
	  	
	  	buttonScrshot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	screenPointGet(null);
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
                        caseList.remove(i);
                    } else {
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
                	popWin.popSelect(4, null);
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
            		case "MG 1A/P":
            			pkg = "dji.prof.mg";
            			break;
            		case "MG 1S":
            			pkg = "dji.prof.args.tiny";
            			break;
            		case "DJI GO4 Pad":
            			pkg = "dji.pilot.pad";
            			break;
            	}
            	
            	try {
            		MainEntry.cachedThreadPool.submit(new Runnable() {
						public void run() {
							ExecutionMain.getInstance().RunTestCase(jsonFile, logArea, device, pkg);
		            		RealTimeScreenUI.isRuncase = true;
						}
					});
            	}catch (Exception e1) {
            		e1.printStackTrace();
            	}
        	}
        });
	  	
	  	buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	JFrame cus_save_frame = new JFrame("SAVE");
                	JLabel cus_save_label = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.cussave"));
                	JTextField cus_save_text = new JTextField();
                	JButton cus_save_btn = new JButton("SAVE");
                	JPanel cus_type_pane = new JPanel();
                	JPanel cus_btn_pane = new JPanel();
                	cus_save_frame.setSize(250,120);
                	cus_save_frame.setVisible(true);
                	cus_save_frame.setLayout(new BorderLayout());
                	cus_save_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
                	cus_save_frame.setLocationRelativeTo(MainEntry.frame);
                	cus_save_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            		cus_save_label.setFont(ConstantsUI.FONT_NORMAL);
            		cus_save_text.setPreferredSize(new Dimension(100,30));
            		
            		cus_type_pane.add(cus_save_label);
            		cus_type_pane.add(cus_save_text);
            		cus_btn_pane.add(cus_save_btn);
            		cus_save_frame.add(cus_type_pane, BorderLayout.NORTH);
            		cus_save_frame.add(cus_btn_pane, BorderLayout.SOUTH);
            		
            		cus_save_btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        	try {
                            	SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
//                            	String time = timeFormat.format(Calendar.getInstance().getTime());
                            	String cus_name = cus_save_text.getText();
                            	
                            	File json = new File("json/" + appStartName + "_" + cus_name + ".json");
                            	if (!json.getParentFile().exists())
                            		json.getParentFile().mkdirs();
                            	
                            	String str = JSON.toJSONString(caseList);
                            	PrintWriter pw = new PrintWriter(new FileWriter(json));
                                pw.print(str);
                                pw.flush();
                                pw.close();
                                cus_save_frame.dispose();
                        	}catch (Exception e1) {
                        		e1.printStackTrace();
                        	}
                    	}
                    });

                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
	
	  	buttonRTChart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainEntry.cachedThreadPool.submit(new Runnable() {
					
					@Override
					public void run() {
						TimeSeriesChart chart = new TimeSeriesChart();
						CollectingOutputReceiver receiver = new CollectingOutputReceiver();
						
						MultiLineReceiver mReceiver = new MultiLineReceiver() {
							
							@Override
							public boolean isCancelled() {
								return false;
							}
							
							@Override
							public void processNewLines(String[] lines) {
								for(String line : lines) { 
						            if(line.contains("TOTAL")) {
						            	tmp = line;
						            }
						        }
							}
						};
						
						String pid = "";
						while (device != null && !pkg.equals("")) {
							try {
//								device.executeShellCommand(String.format("push %s %s", System.getProperty("user.dir") + "/rm500_0042.bin", "/sdcard"), receiver, 0);
//								device.pushFile(System.getProperty("user.dir") + File.separator + "rm500_0042.bin", "/sdcard");
//								if (pid.equals("")) {
//									device.executeShellCommand("ps | grep " + pkg, receiver);
//									receiver.flush();
//									pid = receiver.getOutput().split("\\s+")[1];
//								}
//								device.executeShellCommand("dumpsys meminfo " + pid, mReceiver);
//								System.out.println(tmp);
//								MemValue = Integer.parseInt(tmp.split("\\s+")[1]);
////								chart.addCPUValue(CPUValue);
//								chart.addMemValue(MemValue/10240);
//								chart.repaint();
//								Thread.sleep(1000);
//							} catch (ShellCommandUnresponsiveException ignore) {
//								continue;
							} catch (Exception e) {
								e.printStackTrace();
								break;
							}
						}
					}
				});
			}
		});
	  	
	  	buttonLogSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                	JFrame log_save_frame = new JFrame("SAVE");
                	JLabel log_save_label = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.logsave"));
                	JTextField log_save_text = new JTextField();
                	JButton log_save_btn = new JButton("SAVE");
                	JPanel log_type_pane = new JPanel();
                	JPanel log_btn_pane = new JPanel();
                	log_save_frame.setSize(250,120);
                	log_save_frame.setVisible(true);
                	log_save_frame.setLayout(new BorderLayout());
                	log_save_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
                	log_save_frame.setLocationRelativeTo(MainEntry.frame);
                	log_save_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            		log_save_label.setFont(ConstantsUI.FONT_NORMAL);
            		log_save_text.setPreferredSize(new Dimension(100,30));
            		
            		log_type_pane.add(log_save_label);
            		log_type_pane.add(log_save_text);
            		log_btn_pane.add(log_save_btn);
            		log_save_frame.add(log_type_pane, BorderLayout.NORTH);
            		log_save_frame.add(log_btn_pane, BorderLayout.SOUTH);
            		
            		log_save_btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        	try {
                            	String log_name = log_save_text.getText();
                            	File logstream = new File("report/" + appStartName + "_" + log_name + ".txt");
                            	if (!logstream.getParentFile().exists())
                            		logstream.getParentFile().mkdirs();
                            	
                            	String str = logArea.getText();
                            	PrintWriter pw = new PrintWriter(new FileWriter(logstream));
                                pw.print(str);
                                pw.flush();
                                pw.close();
                                log_save_frame.dispose();
                        	}catch (Exception e1) {
                        		e1.printStackTrace();
                        	}
                    	}
                    });

                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
        });
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
    
//	class ViewListener implements ItemListener {
//		
//		@Override
//		public void itemStateChanged(ItemEvent e) {
//			if (e.getStateChange() == ItemEvent.SELECTED) {
//				String viewName = (String) e.getItem();
//				ResultSet rs = sql.queryElement("ELEMENT", appName, viewName);
//				
//				comboxEleName.removeAllItems();
//				comboxEleName.removeItemListener(elisten);
//				try {
//					rs.next();
//					String eleFirst = new String(rs.getBytes(1), "UTF-8");
//					comboxEleName.addItem(eleFirst);
//					while ((rs.next())) {
//						String eleName = rs.getString(1);
//						comboxEleName.addItem(eleName);
//					}
//					comboxEleName.setSelectedItem(null);
//					comboxEleName.addItemListener(elisten);
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				} catch (UnsupportedEncodingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} finally {
//					if (rs != null) {
//						try {
//							rs.close();
//							rs = null;
//						} catch (SQLException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//	}
	
//	class EleListener implements ItemListener {
//
//		@Override
//		public void itemStateChanged(ItemEvent e) {
//			if (e.getStateChange() == ItemEvent.SELECTED) {
//				String ele_cus = (String) e.getItem();
//				xpathSet = sql.queryElement("ELEMENT", ele_cus);
//			}
//			
//			comboxActName.removeAllItems();
//			try {
//				while (xpathSet.next()) {
//					xpath = xpathSet.getString("XPATH");
//					cus_name = xpathSet.getString("CUSTOM_NAME");
//					scrshot_pathname = xpathSet.getString("SCREEN_PATH");
//					String state = xpathSet.getString(5);
//		        	comboxActName.addItem("Single-Click");
//		        	comboxActName.addItem("Long-Press");
//		        	comboxActName.addItem("Point Drag");
//		        	comboxActName.addItem("Set Text");
//		        	comboxActName.addItem("SeekBar Drag");
//		        	comboxActName.addItem("Push To Device");
//		        	comboxActName.addItem("Reboot Device");
//		        	comboxActName.addItem("Scroll To Exact Ele");
//				}
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			} finally {
//				
//			}
//		}
//		
//	}
	
	class ActListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String action_name = (String) e.getItem();
				switch (action_name) {
					case "Single-Click":
						action = 0;
						break;
					case "Long-Press":
						action = 1;
						break;
					case "Set Text":
						action = 2;
						break;
					case "Point Drag":
						action = 4;
						break;
					case "Push To Device":
						action = 5;
						break;
					case "Reboot Device":
						action = 6;
						break;
					case "Scroll To Exact Ele":
						action = 7;
						break;
					case "Key HOME":
						action = 8;
						break;
					case "Key BACK":
						action = 9;
						break;
					case "SeekBar Drag":
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
		
	private Point point_chosen = null;
	public void screenPointGet(BrickBean brick){
		JFrame scrshot_frame = new JFrame();
		scrshot_frame.setSize(550, 650);
		scrshot_frame.setTitle("ScreenShot View");
		scrshot_frame.setLayout(new BorderLayout());
		scrshot_frame.setVisible(true);
		ImageIcon Scrshot_image =new ImageIcon(
	            CURRENT_DIR + File.separator + scrshot_pathname);
		JLabel picLabel = new JLabel(Scrshot_image);
		JPanel posibtn_pane = new JPanel();
		buttonDragAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.dragpoint"));
		JLabel pointx = new JLabel();
		JLabel pointy = new JLabel();
		posibtn_pane.add(pointx);
		posibtn_pane.add(pointy);
		posibtn_pane.add(buttonDragAdd);
		scrshot_frame.add(picLabel,BorderLayout.NORTH);
		scrshot_frame.add(posibtn_pane,BorderLayout.SOUTH);
		scrshot_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scrshot_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
		scrshot_frame.setLocationRelativeTo(MainEntry.frame);
		
		if(brick != null){
			picLabel.addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			    	scrshot_X = e.getX();
			    	scrshot_Y = e.getY();
			    	if(point_chosen == null){
			    		point_chosen = new Point(scrshot_X, scrshot_Y);
			    	}else{
			    		point_chosen.x = scrshot_X;
			    		point_chosen.y = scrshot_Y;
			    	}
			    	pointx.setText("X:"+ scrshot_X);
			    	pointy.setText("Y:"+ scrshot_Y);
			    	
			    	buttonDragAdd.addActionListener(new ActionListener() {
			            @Override
			            public void actionPerformed(ActionEvent e) {

			                try {
			                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.act");
								table_row[2] = PropertyUtil.getProperty("bricks.ui.casecre.btntip.textset");
								table_row[3] = "X:"+ scrshot_X;
						    	table_row[4] = "Y:"+ scrshot_Y;
						    	
						    	Map point = new HashMap();
				        		point.put("DesPoint", point_chosen);
			                    brick.setParams(point);
			                    
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

			            }
			        });
			    }
			});
		}
	}
	
	private void addEleCombox() {
		comboxAppName = new JComboBox<String>();
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
	
	private void actionLoad(int actionInput, BrickBean brick, boolean caseLoad) {
		boolean contains = false;
		switch (actionInput) {
		case 0:
    		act_name = "Click";
    		break;
		case 1:
    		act_name = "Long Press";
    		break;
		case 2:
    		act_name = "Set Text";
    		if (!caseLoad) {
    			popWin.popSelect(5, brick);
    		}
    		break;
		case 4:
    		act_name = "Point Drag";
    		if (!caseLoad) {
    			screenPointGet(brick);
    		}
    		break;
		case 5:
			act_name = "Push To Device";
			break;
		case 6:
			act_name = "Reboot Device";
			break;
		case 7:
			act_name = "Scroll To Exact Ele";
			if (!caseLoad) {
    			popWin.popSelect(6, brick);
    		}
			break;
		case 8:
			act_name = "Key HOME";
			break;
		case 9:
			act_name = "Key BACK";
			break;
		case 10:
    		act_name = "DB";
    		break;
		}
		
		for (int i=0; i<speAddList.length; i++) {
			if (speAddList[i] == actionInput)
				contains = true;				
		}
		
		if (!contains && !caseLoad) {
			table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.act");
			table_row[2] = act_name;
	    	table_row[3] = "N/A";
	    	table_row[4] = "N/A";
			
	    	int i = casetable.getSelectedRow();
	        if(i >= 0){
	        	model.insertRow(i+1, table_row);
	        	caseList.add(i+1, brick);
	        }else{
	        	model.addRow(table_row);
	        	caseList.add(brick);
	        }
		}
	}
	
//	class VerifiWindow extends JFrame{
//		// init popup window
//		JFrame ver_setting_frame = new JFrame();
//
//	    public VerifiWindow(int type) {
//	    	if(ver_type == 1){
//	    		// Text verification method
//	    		ver_setting_frame.setSize(400, 280);
//	    		ver_setting_frame.setTitle("Text Verification");
//	    		ver_setting_frame.setVisible(true);
//	    		JPanel up_panel = new JPanel();
//	    		JPanel up_left_panel = new JPanel();
//	    		JPanel up_right_panel = new JPanel();
//	    		JPanel down_panel = new JPanel();
//	    		up_panel.setLayout(new BorderLayout());
//	    		up_left_panel.setLayout(new GridLayout(6,1));
//	    		JLabel app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appname"));
//	    		app_name.setFont(ConstantsUI.FONT_NORMAL);
//	    		JLabel app_view = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appview"));
//	    		app_view.setFont(ConstantsUI.FONT_NORMAL);
//	    		JLabel ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elename"));
//	    		ele_name.setFont(ConstantsUI.FONT_NORMAL);
//	    		addEleCombox();
//	    		
//	    		JPanel text_pane = new JPanel();
//	    		JPanel text_btn_pane = new JPanel();
//	    		JTextArea ver_text_input = new JTextArea(8,30);
//	    		ver_text_input.setLineWrap(true);
//	    		buttonVersetTX_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
//	                    ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
//	    		buttonVersetTX_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
//	                    ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
//	    		text_pane.add(new JScrollPane(ver_text_input));
//	    		text_btn_pane.add(buttonVersetTX_add);
//	    		text_btn_pane.add(buttonVersetTX_re);
//	    		
//	    		up_left_panel.add(app_name);
//	    		up_left_panel.add(comboxAppName);
//	    		up_left_panel.add(app_view);
//	    		up_left_panel.add(comboxViewName);
//	    		up_left_panel.add(ele_name);
//	    		up_left_panel.add(comboxEleName);
//	    		up_right_panel.add(text_pane);
//	    		up_panel.add(up_left_panel, BorderLayout.WEST);
//	    		up_panel.add(up_right_panel, BorderLayout.EAST);
//	    		down_panel.add(text_btn_pane);
//	    		ver_setting_frame.add(up_panel, BorderLayout.NORTH);
//	    		ver_setting_frame.add(down_panel, BorderLayout.SOUTH);
//	    		
//	    		// kill the thread, same to the others ver_type case
//	    		ver_setting_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//	    		ver_setting_frame.setVisible(true);
//	    		// set the popup window's position releat to Main frame
//	    		ver_setting_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
//	    		ver_setting_frame.setLocationRelativeTo(MainEntry.frame);
//
//	    		// inside-button method
//	    		buttonVersetTX_add.addActionListener(new ActionListener() {
//		            @Override
//		            public void actionPerformed(ActionEvent e) {
//
//		                try {
//		                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.textver");
//		                	table_row[2] = ver_text_input.getText();
//		                	table_row[3] = "N/A";
//		                	table_row[4] = "N/A";
//		                	
//		                	int i = casetable.getSelectedRow();
//		                    if(i >= 0){
//		                    	model.insertRow(i+1, table_row);
//		                    }else{
//		                    	model.addRow(table_row);
//		                    }
//		                	
//		                	String ele_path_text = xpath.toString();
//							String expect_text = ver_text_input.getText();
//							
//							BrickBean brick_valText = new BrickBean();
//							brick_valText.setProperty("val");
//							brick_valText.setValidation_name(1);
//							Map<String, Object> params_text = new HashMap<>();
//							params_text.put("ele_path", ele_path_text);
//							params_text.put("expect_text", expect_text);
//							brick_valText.setParams(params_text);
//							caseList.add(brick_valText);
//		                } catch (Exception e1) {
//		                	e1.printStackTrace();
//		                }
//
//		            }
//		        });
//	    		
//	    		buttonVersetTX_re.addActionListener(new ActionListener() {
//		            @Override
//		            public void actionPerformed(ActionEvent e) {
//		            	try{
//		                int i = casetable.getSelectedRow();
//		                
//		                if(i >= 0) 
//		                {
//		                   model.setValueAt("ver_text_re", i, 1);
//		                }
//		                else{
//		                    System.out.println("Update Act Error");
//		                	}
//		                } catch (Exception e1) {
//		                	e1.printStackTrace();
//		                }
//	
//		            }
//	    		});
//	    		
//	    	} else if (ver_type == 2) {
//	    		// Element exist verification method
//	    		ver_setting_frame.setSize(300, 200);
//	    		ver_setting_frame.setTitle("Element Picking");
//	    		ver_setting_frame.setLayout(new GridLayout(4,1));
//	    		JPanel app_name_pick = new JPanel();
//	    		JPanel app_view_pick = new JPanel();
//	    		JPanel ele_name_pick = new JPanel();
//	    		JPanel button_pane = new JPanel();
//	    		JLabel app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appname"));
//	    		app_name.setFont(ConstantsUI.FONT_NORMAL);
//	    		JLabel app_view = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.appview"));
//	    		app_view.setFont(ConstantsUI.FONT_NORMAL);
//	    		JLabel ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elename"));
//	    		ele_name.setFont(ConstantsUI.FONT_NORMAL);
//	    		addEleCombox();
//	    		
//	    		buttonVersetEE_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
//	                    ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.addver"));
//	    		buttonVersetEE_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
//	                    ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rever"));
//	    		
//	    		// inside-button method
//	    		buttonVersetEE_add.addActionListener(new ActionListener() {
//		            @Override
//		            public void actionPerformed(ActionEvent e) {
//
//		                try {
//		                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.extver");
//		                	table_row[2] = comboxAppName.getSelectedItem();
//		                	table_row[3] = comboxViewName.getSelectedItem();
//		                	table_row[4] = comboxEleName.getSelectedItem();
//		                	
//		                	int i = casetable.getSelectedRow();
//		                    if(i >= 0){
//		                    	model.insertRow(i+1, table_row);
//		                    }else{
//		                    	model.addRow(table_row);
//		                    }
//		                	
//		                	String ele_path_eleVal = xpath.toString();
//							BrickBean brick_valEle = new BrickBean();
//							brick_valEle.setProperty("val");
//							brick_valEle.setValidation_name(2);
//							Map<String, Object> params_eleVal = new HashMap<>();
//							params_eleVal.put("ele_path", ele_path_eleVal);
//							brick_valEle.setParams(params_eleVal);
//							caseList.add(brick_valEle);
//		                } catch (Exception e1) {
//		                	e1.printStackTrace();
//		                }
//
//		            }
//		        });
//	    		
//	    		buttonVersetEE_re.addActionListener(new ActionListener() {
//	    			@Override
//	    			public void actionPerformed(ActionEvent e) {
//	    				try{
//	    					// i = the index of the selected row
//	    					int i = casetable.getSelectedRow();
//	                
//	    					if(i >= 0) 
//	    					{
//	    						model.setValueAt("ver_re", i, 1);
//	    					}
//	    					else{
//	    						System.out.println("Update Act Error");
//	    					}
//	    				} catch (Exception e1) {
//	    					e1.printStackTrace();
//	    				}
//
//	    			}
//	    		});
//	    		app_name_pick.add(app_name);
//	    		app_name_pick.add(comboxAppName);
//	    		app_view_pick.add(app_view);
//	    		app_view_pick.add(comboxViewName);
//	    		ele_name_pick.add(ele_name);
//	    		ele_name_pick.add(comboxEleName);
//	    		button_pane.add(buttonVersetEE_add);
//	    		button_pane.add(buttonVersetEE_re);
//	    		ver_setting_frame.add(app_name_pick);
//	    		ver_setting_frame.add(app_view_pick);
//	    		ver_setting_frame.add(ele_name_pick);
//	    		ver_setting_frame.add(button_pane);
//	    		ver_setting_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
//	    		ver_setting_frame.setLocationRelativeTo(MainEntry.frame);
//	    		ver_setting_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//	    		ver_setting_frame.setVisible(true);
//	    		
//	    	} else if (ver_type == 4) {
//	    		// Timer adding method
//	    		ver_setting_frame.setSize(270, 130);
//	    		ver_setting_frame.setTitle("WaitTime Setting");
//	    		ver_setting_frame.setLayout(new BorderLayout());
//	    		JPanel timer_pane = new JPanel();
//	    		timer_pane.setPreferredSize(new Dimension(270, 50));
//	    		JLabel timer_label = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.timer"));
//	    		timer_label.setFont(ConstantsUI.FONT_NORMAL);
//	    		JTextField timer_num = new JTextField();
//	    		timer_num.setPreferredSize(new Dimension(80,20));
//	    		JPanel timer_btn_pane = new JPanel();
//	    		buttonVersetTimer_add = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
//	                    ConstantsUI.ICON_ELE_ADD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.timer"));
//	    		buttonVersetTimer_re = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
//	                    ConstantsUI.ICON_ROW_REFRESH_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.retimer"));
//	    		// inside-button method
//	    		buttonVersetTimer_add.addActionListener(new ActionListener() {
//		            @Override
//		            public void actionPerformed(ActionEvent e) {
//
//		                try {
//		                	table_row[1] = PropertyUtil.getProperty("bricks.ui.casecre.timer");
//		                	table_row[2] = timer_num.getText() +"  "+ "S";
//		                	table_row[3] = "N/A";
//		                	table_row[4] = "N/A";
//		                	
//		                	Map<String, Object> params_time = new HashMap<>();
//		                    params_time.put("time", Integer.parseInt(timer_num.getText()));
//		                    BrickBean timeBrick = new BrickBean();
//		                    timeBrick.setProperty("time");
//		                    timeBrick.setParams(params_time);
//		                    
//		                	int i = casetable.getSelectedRow();
//		                    if(i >= 0){
//		                    	model.insertRow(i+1, table_row);
//		                    	caseList.add(i+1, timeBrick);
//		                    }else{
//		                    	model.addRow(table_row);
//		                    	caseList.add(timeBrick);
//		                    }
//		                } catch (Exception e1) {
//		                	e1.printStackTrace();
//		                }
//
//		            }
//		        });
//	    		
//	    		buttonVersetTimer_re.addActionListener(new ActionListener() {
//	    			@Override
//	    			public void actionPerformed(ActionEvent e) {
//	    				try{
//	    					// i = the index of the selected row
//	    					int i = casetable.getSelectedRow();
//	                
//	    					if(i >= 0) 
//	    					{
//	    						model.setValueAt("timer_re", i, 1);
//	    					}
//	    					else{
//	    						System.out.println("Update Act Error");
//	    					}
//	    				} catch (Exception e1) {
//	    					e1.printStackTrace();
//	    				}
//
//	    			}
//	    		});
//	    		timer_pane.add(timer_label);
//	    		timer_pane.add(timer_num);
//	    		timer_btn_pane.add(buttonVersetTimer_add);
//	    		timer_btn_pane.add(buttonVersetTimer_re);
//	    		ver_setting_frame.add(timer_pane, BorderLayout.NORTH);
//	    		ver_setting_frame.add(timer_btn_pane, BorderLayout.SOUTH);
//	    		ver_setting_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
//	    		ver_setting_frame.setLocationRelativeTo(MainEntry.frame);
//	    		ver_setting_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//	    		ver_setting_frame.setVisible(true);
//	    		ver_type = 0;
//	    	} else if(ver_type == 0) {
//	    		// Warning message
//	    		ver_setting_frame.setSize(250, 100);
//	    		ver_setting_frame.setTitle("WARNING");
//	    		ver_setting_frame.setLayout(new BorderLayout());
//	    		ver_setting_frame.setLocation(MainEntry.frame.getLocationOnScreen());  
//	    		ver_setting_frame.setLocationRelativeTo(MainEntry.frame);
//	    		ver_setting_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//	    		ver_setting_frame.setVisible(true);
//	    		JLabel repick_warn = new JLabel();
//	    		ImageIcon warn_icon = new ImageIcon(CURRENT_DIR + File.separator + "icon" + File.separator + "warning.png");
//	    		JLabel icon_label = new JLabel(warn_icon);
//	    		repick_warn.setText(PropertyUtil.getProperty("bricks.ui.casecre.pickwarn"));
//	    		repick_warn.setFont(new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 12));
//	    		ver_setting_frame.add(icon_label, BorderLayout.WEST);
//	    		ver_setting_frame.add(repick_warn, BorderLayout.CENTER);
//	    	}
//	    }
//	}
}
