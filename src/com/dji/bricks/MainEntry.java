package com.dji.bricks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

import com.android.ddmlib.CollectingOutputReceiver;
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
import com.dji.bricks.tools.DeviceConnection;
import com.dji.bricks.tools.SQLUtils;

/**
 *
 * @author DraLastat
 */

public class MainEntry implements GlobalObserver {
    private static Logger LOG = Logger.getLogger(MainEntry.class);

    public static JFrame frame;
    private ToolBarPanel toolbar;
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
	private DeviceConnection adb;
	public static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(100);
	private VariableChangeObserve obs = new VariableChangeObserve();
	
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
        
		adb = new DeviceConnection();
		adb.registerObserver(MainEntry.this);

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
        
        //init appium
    	Thread appium = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Runtime.getRuntime().exec("cmd /c start appium --relaxed-security");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
    	cachedThreadPool.submit(appium);
			
        frame = new JFrame();
        frame.setBounds(ConstantsUI.MAIN_WINDOW_X, ConstantsUI.MAIN_WINDOW_Y, ConstantsUI.MAIN_WINDOW_WIDTH,
                ConstantsUI.MAIN_WINDOW_HEIGHT);
        frame.setTitle(ConstantsUI.APP_NAME);
        frame.setIconImage(ConstantsUI.IMAGE_ICON);
        frame.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        
        frame.setResizable(false);
           
        mainPanel = new JPanel(true);
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        toolbar = new ToolBarPanel();
        statusPanel = new StatusPanel();
        elecrePanel = new ElecrePanel(obs, sql);
        adb.registerObserver(elecrePanel);
        casecrePanel = new CasecrePanel(sql);
        adb.registerObserver(casecrePanel);
        caserunPanel = new CaserunPanel();
        adb.registerObserver(caserunPanel);
        settingPanel = new SettingPanel();

        mainPanel.add(toolbar, BorderLayout.WEST);

        mainPanelCenter = new JPanel(true);
        mainPanelCenter.setLayout(new BorderLayout());
        mainPanelCenter.add(statusPanel, BorderLayout.CENTER);

        mainPanel.add(mainPanelCenter, BorderLayout.CENTER);

        frame.add(mainPanel);

        obs.addObserver(elecrePanel);
        adb.init();
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
            	
//            		Runtime.getRuntime().exec("cmd start " + System.getProperty("user.dir") + "/close.bat");
            		Runtime.getRuntime().exec("taskkill /F /IM node.exe");
            		
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

	@Override
	public void frameImageChange(BufferedImage image) {
	}

	@Override
	public void ADBChange(IDevice[] devices) {
	}
}
