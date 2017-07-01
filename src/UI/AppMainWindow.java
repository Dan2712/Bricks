package UI;

import UI.panel.*;
import backgrounder.execution.AppiumInit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import tools.PropertyUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * ������ڣ�������Frame
 *
 * @author DraLastat
 */

public class AppMainWindow {
    private static Logger logger = Logger.getLogger(AppMainWindow.class);

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
	private Statement stmt;
	
    /**
     * 
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AppMainWindow window = new AppMainWindow();
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
    public AppMainWindow() {
        initialize();
        //StatusPanel.buttonStartSchedule.doClick();
    }

    /**
     * 
     */
    private void initialize() {
        PropertyConfigurator
                .configure(ConstantsUI.CURRENT_DIR + File.separator + "config" + File.separator + "log4j.properties");
        logger.info("==================BricksInitStart====================");
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
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
        	e.printStackTrace();
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
        statusPanel = new StatusPanel();
        elecrePanel = new ElecrePanel();
        casecrePanel = new CasecrePanel();
        caserunPanel = new CaserunPanel();
        settingPanel = new SettingPanel();

        mainPanel.add(toolbar, BorderLayout.WEST);

        mainPanelCenter = new JPanel(true);
        mainPanelCenter.setLayout(new BorderLayout());
        mainPanelCenter.add(statusPanel, BorderLayout.CENTER);

        mainPanel.add(mainPanelCenter, BorderLayout.CENTER);

        frame.add(mainPanel);


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
            	
                	stmt.close();
                    connection.close();
                }catch(Exception e1) {
                	e1.printStackTrace();
                } 
            	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                logger.info("==================BricksEnd==================");
                
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
