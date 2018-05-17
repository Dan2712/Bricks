package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.android.ddmlib.IDevice;

import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.backgrounder.ExecutionMain;
import com.dji.bricks.node_selection.RealTimeScreenUI;
import com.dji.bricks.tools.FileUtils;
import com.dji.bricks.tools.PropertyUtil;

public class CaserunResultPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private static MyIconButton buttonCaseFind;
	private static MyIconButton buttonStart;
	private static MyIconButton buttonStop;

	private static Logger logger = Logger.getLogger(CaserunResultPanel.class);
	
	private String filepath;
	private JTextArea logprint;
	private IDevice device;
	private JSONArray jsonFile;
	private String appName;
	
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
	JTextField case_name;
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
        panelGrid1.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 15));
        JPanel panelGrid2 = new JPanel();
        panelGrid2.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelGrid2.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));
        
		case_name = new JTextField();
		case_name.setEditable(false);
		case_name.setFont(ConstantsUI.FONT_NORMAL);
		case_name.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);

        buttonCaseFind = new MyIconButton(ConstantsUI.ICON_FINDCASE, ConstantsUI.ICON_FINDCASE_ENABLE,
                ConstantsUI.ICON_FINDCASE_DISABLE, "");
        buttonStart = new MyIconButton(ConstantsUI.ICON_START, ConstantsUI.ICON_START_ENABLE,
                ConstantsUI.ICON_START_DISABLE, "");
        //buttonStart.setEnabled(false);
        buttonStop = new MyIconButton(ConstantsUI.ICON_STOP, ConstantsUI.ICON_STOP_ENABLE,
                ConstantsUI.ICON_STOP_DISABLE, "");
        //buttonStop.setEnabled(false);
        panelGrid1.add(buttonCaseFind);
        panelGrid1.add(case_name);
        panelGrid2.add(buttonStart);
        panelGrid2.add(buttonStop);

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
                    JFileChooser jfc = new JFileChooser(new File("/json"));  
                    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
                    jfc.showDialog(new JLabel(), "选择");  
                    filepath = jfc.getSelectedFile().getPath();
                    
                    appName = filepath.substring(filepath.lastIndexOf("\\")+1, filepath.indexOf("_"));
                    System.out.println(appName);
                    jsonFile = FileUtils.loadJson(filepath);
                    case_name.setText(filepath);
                } catch (Exception e1) {
                    logger.error("open table_field file fail:" + e1.toString());
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
						String pkg = "";
						
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
						ExecutionMain.getInstance().RunTestCase(jsonFile, logprint, device, pkg);
						RealTimeScreenUI.isRuncase = true;
					}
				}).start();
			}
		});
	}

}
