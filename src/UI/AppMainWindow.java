package UI;

import UI.panel.*;
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

    /**
     * �������main
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
     * ���죬����APP
     */
    public AppMainWindow() {
        initialize();
        //StatusPanel.buttonStartSchedule.doClick();
    }

    /**
     * ��ʼ��frame����
     */
    private void initialize() {
        PropertyConfigurator
                .configure(ConstantsUI.CURRENT_DIR + File.separator + "config" + File.separator + "log4j.properties");
        logger.info("==================BricksInitStart====================");
        // ����ϵͳĬ����ʽ
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // ��ʼ��������
        frame = new JFrame();
        frame.setBounds(ConstantsUI.MAIN_WINDOW_X, ConstantsUI.MAIN_WINDOW_Y, ConstantsUI.MAIN_WINDOW_WIDTH,
                ConstantsUI.MAIN_WINDOW_HEIGHT);
        frame.setTitle(ConstantsUI.APP_NAME);
        frame.setIconImage(ConstantsUI.IMAGE_ICON);
        frame.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        
        //���ڴ�С���ɵ� 
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


    }
}
