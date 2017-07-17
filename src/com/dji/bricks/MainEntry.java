package com.dji.bricks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.android.ddmlib.IDevice;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.panel.CasecrePanel;
import com.dji.bricks.UI.panel.CaserunPanel;
import com.dji.bricks.UI.panel.ElecrePanel;
import com.dji.bricks.UI.panel.SettingPanel;
import com.dji.bricks.UI.panel.StatusPanel;
import com.dji.bricks.UI.panel.ToolBarPanel;
import com.dji.bricks.backgrounder.execution.AppiumInit;
import com.dji.bricks.node_selection.VariableChangeObserve;
import com.dji.bricks.tools.ADB;
import com.dji.bricks.tools.SQLUtils;

/**
 *
 * @author DraLastat
 */

public class MainEntry {
    private static Logger LOG = Logger.getLogger(MainEntry.class);

    private JFrame frame;

    private static JPanel mainPanel;
    public static JPanel mainPanelCenter;
    public static StatusPanel statusPanel;
    public static ElecrePanel elecrePanel;
    public static CasecrePanel casecrePanel;
    public static CaserunPanel caserunPanel;
    public static SettingPanel settingPanel;
    public static JDialog dialog;
	private Connection connection;
	
	private SQLUtils sql;
	private IDevice device;
	public static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(500);
	
	/**
     * 
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainEntry window = new MainEntry();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 
     */
    public MainEntry() {
        initialize();
    }

    /**
     * 
     */
    private void initialize() {
        PropertyConfigurator
                .configure(ConstantsUI.CURRENT_DIR + File.separator + "config" + File.separator + "log4j.properties");
        LOG.info("==================BricksInitStart====================");
        
        ADB adb = new ADB();
        IDevice[] devices = adb.getDevices();
        if (devices.length <= 0) {
			LOG.error("ADB not connected, please check");
			return;
		}
		device = devices[0];
		
        // 
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        //init sqlite
        try {
        	Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:ElementInfo.db");
            connection.setAutoCommit(true);
            
            sql = new SQLUtils(connection);
            sql.creatTable();
        } catch (Exception e) {
        	e.printStackTrace();
        	System.exit(0);
        }
        
        // 
        frame = new JFrame();
        frame.setBounds(ConstantsUI.MAIN_WINDOW_X, ConstantsUI.MAIN_WINDOW_Y, ConstantsUI.MAIN_WINDOW_WIDTH,
                ConstantsUI.MAIN_WINDOW_HEIGHT);
        frame.setTitle(ConstantsUI.APP_NAME);
        frame.setIconImage(ConstantsUI.IMAGE_ICON);
        frame.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        
        // 
        frame.setResizable(false);
           
        mainPanel = new JPanel(true);
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        ToolBarPanel toolbar = new ToolBarPanel();
        VariableChangeObserve obs = new VariableChangeObserve();
        statusPanel = new StatusPanel();
        elecrePanel = new ElecrePanel(obs, sql, device);
        casecrePanel = new CasecrePanel(sql);
        caserunPanel = new CaserunPanel(device);
        settingPanel = new SettingPanel();

        mainPanel.add(toolbar, BorderLayout.WEST);

        mainPanelCenter = new JPanel(true);
        mainPanelCenter.setLayout(new BorderLayout());
        mainPanelCenter.add(statusPanel, BorderLayout.CENTER);

        mainPanel.add(mainPanelCenter, BorderLayout.CENTER);

        frame.add(mainPanel);

        obs.addObserver(elecrePanel);
        
        frame.addWindowListener(new WindowListener() {

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
            	try {
            		if (AppiumInit.driver != null)
            			AppiumInit.driver.quit();
            	
                    connection.close();
                }catch(Exception e1) {
                	e1.printStackTrace();
                } 
            	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                LOG.info("==================BricksEnd==================");
                
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }
        });
        
    }
}