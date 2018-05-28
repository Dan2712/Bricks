package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.IDevice;
import com.dji.bricks.MainEntry;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.backgrounder.ExecutionMain;
import com.dji.bricks.node_selection.RealTimeScreenUI;
import com.dji.bricks.tools.FileUtils;
import com.dji.bricks.tools.PropertyUtil;

public class CaserunResultPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private static MyIconButton buttonCaseFind;
	private static MyIconButton buttonCaseDelte;
	private static MyIconButton buttonStart;
	private static MyIconButton buttonChart;
	private static int run_time = 1;
	private Object[] table_row = new Object[2];

	private static Logger logger = Logger.getLogger(CaserunResultPanel.class);
	
	private String filepath;
	private JTextArea logprint;
	private IDevice device;
	private JSONArray jsonFile;
	private String appName;
	private List<String> case_list;
	private String pkg = "";
	
	/**
	 * 
	 */
	public CaserunResultPanel(IDevice device) {
		this.device = device;
		initialize();
		addComponent();
		addListener();
	}

	/**
	 * 
	 */
	private void initialize() {
		this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
	}

	/**
	 * 
	 */
	private void addComponent() {
		this.add(getCenterPanel(), BorderLayout.CENTER);

	}

	/**
	 * 
	 * @return
	 */
	private JTextField case_name;
	private JTable List_table;
	private DefaultTableModel model;
	private JTextField run_num;
	private JPanel getCenterPanel() {
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new GridLayout(2, 1));
		
		/**
		 *
		 */
		JPanel panelGridCaseFind = new JPanel();
		panelGridCaseFind.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridCaseFind.setLayout(new GridLayout(2, 1));

        JPanel panelGrid1 = new JPanel();
        panelGrid1.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelGrid1.setLayout(new BorderLayout());
        JPanel panelGrid2 = new JPanel();
        panelGrid2.setBackground(ConstantsUI.MAIN_BACK_COLOR);
//        panelGrid2.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));
        
		case_name = new JTextField();
		case_name.setEditable(false);
		case_name.setFont(ConstantsUI.FONT_NORMAL);
		case_name.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		case_list = new ArrayList();
		
		JPanel panelList = new JPanel();
		JPanel panelListBtn = new JPanel();
		panelList.setBackground(Color.WHITE);
		panelListBtn.setBackground(Color.WHITE);
		panelListBtn.setLayout(new GridLayout(2,1));
		List_table = new JTable();
		Object[] columns = {"JSON Name","Path"};
		model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        List_table.setModel(model);
        List_table.setBackground(Color.LIGHT_GRAY);
        List_table.setForeground(Color.black);
        Font font = new Font("",1,10);
        List_table.setFont(font);
        List_table.setRowHeight(30);
        List_table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        
        JLabel multirun_label1 = new JLabel(PropertyUtil.getProperty("bricks.ui.caserun.runtimes1"));
        JLabel multirun_label2 = new JLabel(PropertyUtil.getProperty("bricks.ui.caserun.runtimes2"));
        JLabel label_null = new JLabel();
        run_num = new JTextField();
        
        multirun_label1.setFont(ConstantsUI.FONT_NORMAL);
        multirun_label2.setFont(ConstantsUI.FONT_NORMAL);
        run_num.setPreferredSize(new Dimension(25,20));
        run_num.setBackground(Color.WHITE);
        run_num.setText("1");
        label_null.setPreferredSize(new Dimension(400,20));
		
		buttonCaseFind = new MyIconButton(ConstantsUI.ICON_DOCREAD, ConstantsUI.ICON_DOCREAD_ENABLE,
                ConstantsUI.ICON_DOCREAD_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.docread"));
		buttonCaseDelte = new MyIconButton(ConstantsUI.ICON_ROW_DELETE, ConstantsUI.ICON_ROW_DELETE_ENABLE,
                ConstantsUI.ICON_ROW_DELETE_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.docdelte"));
        buttonStart = new MyIconButton(ConstantsUI.ICON_PLAY_LIST, ConstantsUI.ICON_PLAY_LIST_ENABLE,
				ConstantsUI.ICON_PLAY_LIST_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.playlist"));
        //buttonStart.setEnabled(false);
        buttonChart = new MyIconButton(ConstantsUI.ICON_RTCHART, ConstantsUI.ICON_RTCHART_ENABLE,
				ConstantsUI.ICON_RTCHART_DISABLE, PropertyUtil.getProperty("bricks.ui.casecre.btntip.rtchart"));
        //buttonStop.setEnabled(false);
        
        panelList.add(new JScrollPane(List_table));
        panelListBtn.add(buttonCaseFind);
        panelListBtn.add(buttonCaseDelte);
        panelGrid1.add(panelList, BorderLayout.WEST);
        panelGrid1.add(panelListBtn, BorderLayout.CENTER);
        panelGrid2.add(multirun_label1);
        panelGrid2.add(run_num);
        panelGrid2.add(multirun_label2);
        panelGrid2.add(label_null);
        panelGrid2.add(buttonChart);
        panelGrid2.add(buttonStart);

        panelGridCaseFind.add(panelGrid1);
        panelGridCaseFind.add(panelGrid2);
		
		/**
		 * 
		 */
		JPanel panelGridLog = new JPanel();
		panelGridLog.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridLog.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));

		JLabel labelRunLogTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.caserun.logprint"));
		JLabel labellogprintNull = new JLabel();
		logprint = new JTextArea(12, 68);
		logprint.setBorder(null);
		logprint.setBorder(new EmptyBorder(0,0,0,0));
		logprint.setLineWrap(true);
        logprint.setWrapStyleWord(true);
        logprint.setForeground(ConstantsUI.MAIN_BACK_COLOR);
        logprint.setBackground(ConstantsUI.TABLE_BACK_COLOR);
		labelRunLogTitle.setFont(ConstantsUI.FONT_NORMAL);
		labelRunLogTitle.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		labellogprintNull.setPreferredSize(ConstantsUI.LABLE_SIZE_CASE_NULL_ITEM);
		panelGridLog.add(labelRunLogTitle);
		panelGridLog.add(labellogprintNull);
		panelGridLog.add(new JScrollPane(logprint));


		panelCenter.add(panelGridCaseFind);
		panelCenter.add(panelGridLog);
		return panelCenter;
	}
	
	public void addListener() {
		
		buttonCaseFind.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jfc = new JFileChooser(new File(System.getProperty("user.dir") + "/json"));  
                    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
                    jfc.showDialog(new JLabel(), "选择");  
                    filepath = jfc.getSelectedFile().getPath();
                    
                    String filename = filepath.substring(filepath.lastIndexOf("\\")+1);
                    appName = filename.substring(0, filename.lastIndexOf("_"));
                    jsonFile = FileUtils.loadJson(filepath);
                    table_row[0] = filename;
                    table_row[1] = filepath;
                    model.addRow(table_row);
                    case_list.add(filepath);
//                    System.out.println(case_list);
                } catch (Exception e1) {
                    logger.error("open table_field file fail:" + e1.toString());
                    e1.printStackTrace();
                }
            }
        });
		
		buttonCaseDelte.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
            	try {
                    // i = the index of the selected row
                    int i = List_table.getSelectedRow();
                    if(i >= 0){
                        model.removeRow(i);
                        case_list.remove(i);
                        System.out.println(case_list);
                    }
                    else{
                        System.out.println("Delete Error");
                    }
                } catch (Exception e1) {
                	e1.printStackTrace();
                }

            }
	  	});
		
		buttonStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						switch (appName) {
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
						
						logprint.setText("");
						ExecutionMain.getInstance().RunTestCase(jsonFile, logprint, device, pkg, Integer.parseInt(run_num.getText()));
						RealTimeScreenUI.isRuncase = true;
					}
				}).start();
			}
		});
	}

}
