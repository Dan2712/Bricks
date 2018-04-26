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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.alibaba.fastjson.JSON;
import com.dji.bricks.UI.BrickBean;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.tools.PropertyUtil;
import com.dji.bricks.tools.SQLUtils;


/**
 * Case create page 
 * @author DraLastat
 */
public class CasecrePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private static MyIconButton buttonEleAdd;
	private static MyIconButton buttonActAdd;
	private static MyIconButton buttonVerAdd;
	private static MyIconButton buttonRowDelete;
	private static MyIconButton buttonEleRefresh;
	private static MyIconButton buttonActRefresh;
	private static MyIconButton buttonVerRefresh;
	private static MyIconButton buttonSave;
	private static MyIconButton buttonScrshot;
	private static MyIconButton buttonDocRead;
	private static MyIconButton buttonJsonLoad;
	private static MyIconButton buttonPlayList;
	private static MyIconButton buttonRTChart;
	private static JPanel popuppanel;
//	private static PopupWindow popupwindow;
	private int id;
	private int type;
	private LinkedList<BrickBean> caseList = new LinkedList<>();
	
	private String xpath = "";
	private String cus_name = "";
	private String act_name = "";
	private String val = "";
	private int action;
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
	private SQLUtils sql = null;
	private ResultSet xpathSet = null;
	private PrintStream standardOut;
	private Object[] table_row = new Object[5];
	
	private ViewListener vlisten = new ViewListener();
	private EleListener elisten = new EleListener();
	private JPanel getCenterPanel(){
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(ConstantsUI.TABLE_LOG_COLOR);
		JPanel TablePanel = new JPanel();
		TablePanel.setPreferredSize(new Dimension(810, 250));
        casetable = new JTable(); 
        // create a table model and set a Column Identifiers to this model 
        Object[] columns = {"Id","Type","SPEC 1","SPEC 2","SPEC 3"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        
        // set the model to the table
        casetable.setModel(model);
        
        // Change A JTable Background Color, Font Size, Font Color, Row Height
        casetable.setBackground(Color.LIGHT_GRAY);
        casetable.setForeground(Color.black);
        Font font = new Font("",1,10);
        casetable.setFont(font);
        casetable.setRowHeight(30);
        casetable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        // create an array of objects to set the row data
        TablePanel.add(new JScrollPane(casetable));
        
		// Adding component to panel
        panelCenter.add(TablePanel);
        panelCenter.updateUI();
		
		return  panelCenter;
	}
	
	/**
	 * Log print Panel 
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
		DataGrid.setBackground(ConstantsUI.TABLE_LOG_COLOR);
		JTextField DataFrom = new JTextField();
		DataFrom.setPreferredSize(new Dimension(150, 24));
		buttonDocRead = new MyIconButton(ConstantsUI.ICON_DOCREAD, ConstantsUI.ICON_DOCREAD_ENABLE,
                ConstantsUI.ICON_DOCREAD_DISABLE, "");
		buttonJsonLoad = new MyIconButton(ConstantsUI.ICON_JSONLOAD, ConstantsUI.ICON_JSONLOAD_ENABLE,
                ConstantsUI.ICON_JSONLOAD_DISABLE, "");
		DataGrid.add(DataSrc);
		DataGrid.add(DataFrom);
		DataGrid.add(buttonDocRead);
		DataGrid.add(buttonJsonLoad);
		
		//Bricks adding panel
		JPanel ElePanel_APP = new JPanel();
		ElePanel_APP.setPreferredSize(new Dimension(420, 40));
		ElePanel_APP.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		ElePanel_APP.setBackground(ConstantsUI.TABLE_LOG_COLOR);
		JPanel ElePanel_View = new JPanel();
		ElePanel_View.setPreferredSize(new Dimension(420, 40));
		ElePanel_View.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		ElePanel_View.setBackground(ConstantsUI.TABLE_LOG_COLOR);
		JPanel ElePanel_Name = new JPanel();
		ElePanel_Name.setPreferredSize(new Dimension(420, 40));
		ElePanel_Name.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		ElePanel_Name.setBackground(ConstantsUI.TABLE_LOG_COLOR);
		JLabel Ele_Null = new JLabel();
		Ele_Null.setPreferredSize(new Dimension(50,35));
		JLabel Ele_Null2 = new JLabel();
		Ele_Null2.setPreferredSize(new Dimension(50,35));
		JLabel ElePick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elepick"));
		ElePick.setFont(ConstantsUI.FONT_NORMAL);
		buttonScrshot = new MyIconButton(ConstantsUI.ICON_SCRSHOT, ConstantsUI.ICON_SCRSHOT_ENABLE,
                ConstantsUI.ICON_SCRSHOT_DISABLE, "");
		buttonEleAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, "");
		buttonEleRefresh = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, "");

		comboxAppName = new JComboBox<String>();
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
		ActPanel.setBackground(ConstantsUI.TABLE_LOG_COLOR);
		JLabel ActPick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.actpick"));
		ActPick.setFont(ConstantsUI.FONT_NORMAL);
		comboxActName = new JComboBox<String>();
		comboxActName.setEditable(false);
		comboxActName.setSelectedItem(null);
		comboxActName.addItem("click");
		comboxActName.addItem("longPress");
		comboxActName.addItem("setText");
		comboxActName.addItem("dragBar");
		comboxActName.setSelectedItem(null);
		comboxActName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		comboxActName.addItemListener(new ActListener());
		JLabel ActNull = new JLabel();
		ActNull.setPreferredSize(new Dimension(40, 40));
		buttonActAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, "");
		buttonActRefresh = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, "");
		ActPanel.add(ActPick);
		ActPanel.add(comboxActName);
		ActPanel.add(ActNull);
		ActPanel.add(buttonActAdd);
		ActPanel.add(buttonActRefresh);
		
		JPanel VerPanel = new JPanel();
		VerPanel.setPreferredSize(new Dimension(420, 40));
		VerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		VerPanel.setBackground(ConstantsUI.TABLE_LOG_COLOR);
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
						val = "TV";
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
						break;
					case "Image Validation":
						val = "IV";
						break;
					case "Element Exist Validation":
						val = "EEV";
						String ele_path_eleVal = "//android.widget.TextView[@resource-id='com.dji.industry.pilot:id/home_connect_cameras']";
						BrickBean brick_valEle = new BrickBean();
						brick_valEle.setProperty("val");
						brick_valEle.setValidation_name(2);
						Map<String, String> params_eleVal = new HashMap<>();
						params_eleVal.put("ele_path", ele_path_eleVal);
						brick_valEle.setParams(params_eleVal);
						caseList.add(brick_valEle);
						break;
					}
				}
			}
		});
		JLabel VerNull = new JLabel();
		VerNull.setPreferredSize(new Dimension(40, 40));
		buttonVerAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, "");
		buttonVerRefresh = new MyIconButton(ConstantsUI.ICON_ROW_REFRESH, ConstantsUI.ICON_ROW_REFRESH_ENABLE,
                ConstantsUI.ICON_ROW_REFRESH_DISABLE, "");
		VerPanel.add(VerPick);
		VerPanel.add(comboxVerName);
		VerPanel.add(VerNull);
		VerPanel.add(buttonVerAdd);
		VerPanel.add(buttonVerRefresh);
		
		JPanel RunPanel = new JPanel();
		RunPanel.setPreferredSize(new Dimension(420, 40));
		RunPanel.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		RunPanel.setBackground(ConstantsUI.TABLE_LOG_COLOR);
		buttonRowDelete = new MyIconButton(ConstantsUI.ICON_ROW_DELETE, ConstantsUI.ICON_ROW_DELETE_ENABLE,
                ConstantsUI.ICON_ROW_DELETE_DISABLE, "");
		buttonSave = new MyIconButton(ConstantsUI.ICON_LIST_SAVE, ConstantsUI.ICON_LIST_SAVE_ENABLE,
				ConstantsUI.ICON_LIST_SAVE_DISABLE, "");
		buttonPlayList = new MyIconButton(ConstantsUI.ICON_PLAY_LIST, ConstantsUI.ICON_PLAY_LIST_ENABLE,
				ConstantsUI.ICON_PLAY_LIST_DISABLE, "");
		buttonRTChart = new MyIconButton(ConstantsUI.ICON_RTCHART, ConstantsUI.ICON_RTCHART_ENABLE,
				ConstantsUI.ICON_RTCHART_DISABLE, "");
		JLabel TableNull = new JLabel();
		TableNull.setPreferredSize(new Dimension(355, 40));
		RunPanel.add(buttonRowDelete);
		RunPanel.add(buttonSave);
		RunPanel.add(buttonPlayList);
		RunPanel.add(buttonRTChart);
		
		JPanel panelLog = new JPanel();
		panelLog.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelLog.setLayout(new BorderLayout());
		JTextArea LogArea = new JTextArea(17, 50);
		LogArea.setBackground(ConstantsUI.LOG_COLOR);
		LogArea.setForeground(ConstantsUI.MAIN_BACK_COLOR);
		LogArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(LogArea));
		standardOut = System.out;
        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
        
        panelLog.add(new JScrollPane(LogArea));
		
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
	
	// Add Bricks button listener
	public void addListener() {
		buttonEleAdd.addActionListener(new ActionListener() {
			  @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                	table_row[1] = cus_name;
	                	table_row[2] = cus_name;
	                	table_row[3] = cus_name;
	                	table_row[4] = cus_name;
	            	    model.addRow(table_row);
	                	BrickBean brick = new BrickBean();
	                	brick.setEle_xpath(xpath);
	                	brick.setCustom_name(cus_name);
	                	brick.setProperty("ele");
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
	                	if(action == 1){
	                		act_name = "CK";
	                	}else if(action == 2){
	                		act_name = "LP";
	                	}else if(action == 3){
	                		act_name = "ST";
	                	}else if(action == 10){
	                		act_name = "DB";
	                	}
	                	table_row[1] = act_name;
	                	table_row[2] = "N/A";
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
	                	table_row[1] = "Ver";
	                	table_row[2] = "N/A";
	                	table_row[3] = "N/A";
	                	table_row[4] = "N/A";
	                	model.addRow(table_row);
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
//		  	buttonVerRefresh.addActionListener(new ActionListener() {
//	            @Override
//	            public void actionPerformed(ActionEvent e) {
//	            	try{
//	                // i = the index of the selected row
//	                int i = casetable.getSelectedRow();
//	                
//	                if(i >= 0) 
//	                {
//	                   model.setValueAt(act_name, i, 1);
//	                }
//	                else{
//	                    System.out.println("Update Act Error");
//	                	}
//	                } catch (Exception e1) {
//	                	e1.printStackTrace();
//	                }
//
//	            }
//	        });
		  	buttonPlayList.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {

	                try {
	                	printLog();
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
	                	
	                	File json = new File("json/file1.json");
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
//	 class PopClickListener extends MouseAdapter {
//		    public void mousePressed(MouseEvent e){
//		        if (e.isPopupTrigger())
//		            doPop(e);
//		    }
//		    private JButton jbtn;
//		    public PopClickListener(JButton jbtn){
//		    	super();
//		    	this.jbtn =jbtn;
//		    }
//		    public void mouseReleased(MouseEvent e){
//		        if (e.isPopupTrigger())
//		            doPop(e);
//		    }
//
//		    private void doPop(MouseEvent e){
//		    	RightMenu menu = new RightMenu(jbtn);
//		        menu.show(e.getComponent(), e.getX(), e.getY());
//		    }
//		}
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
					action = 1;
					break;
				case "longPress":
					action = 2;
					break;
				case "setText":
					action = 3;
					break;
				case "dragBar":
					action = 10;
					break;
				}
			}
		}
	}
	}
	
