package com.dji.bricks.UI.panel;

import com.dji.bricks.MainEntry;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
//import logic.ConstantsLogic;
import org.apache.log4j.Logger;
import com.dji.bricks.tools.ConstantsUtils;
import com.dji.bricks.tools.PropertyUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * �߼�ѡ�����
 *
 * @author DraLastat
 */
public class SettingPanelOption extends JPanel {

    private static final long serialVersionUID = 1L;

    private static MyIconButton buttonSave;

    private static MyIconButton buttionTableFiled;

    private static MyIconButton buttionClearLogs;

    private static MyIconButton buttionClearBaks;

    private static JCheckBox checkBoxAutoBak;

    private static JCheckBox checkBoxDebug;

    private static JCheckBox checkBoxStrict;

    private static JTextField textField;

    private static Logger logger = Logger.getLogger(SettingPanelOption.class);

    /**
     * ����
     */
    public SettingPanelOption() {
        initialize();
        addComponent();
        //setCurrentOption();
        //addListener();
    }

    /**
     * ��ʼ��
     */
    private void initialize() {
        this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        this.setLayout(new BorderLayout());
    }

    /**
     * ������
     */
    private void addComponent() {

        this.add(getCenterPanel(), BorderLayout.CENTER);
        this.add(getDownPanel(), BorderLayout.SOUTH);

    }

    /**
     * �в����
     *
     * @return
     */
    private JPanel getCenterPanel() {
        // �м����
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelCenter.setLayout(new GridLayout(1, 1));

        // ����Grid
        JPanel panelGridOption = new JPanel();
        panelGridOption.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelGridOption.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));

        // ��ʼ�����
        JPanel panelItem1 = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem2 = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem3 = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem4 = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem5 = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem6 = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem7 = new JPanel(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));

        panelItem1.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem2.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem3.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem4.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem5.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem6.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem7.setBackground(ConstantsUI.MAIN_BACK_COLOR);

        panelItem1.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem2.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem3.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem4.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem5.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem6.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem7.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);

/*        buttionTableFiled = new MyIconButton(ConstantsUI.ICON_TABLE_FIELD, ConstantsUI.ICON_TABLE_FIELD_ENABLE,
                ConstantsUI.ICON_TABLE_FIELD_DISABLE, "");
        panelItem1.add(buttionTableFiled);

        buttionClearLogs = new MyIconButton(ConstantsUI.ICON_CLEAR_LOG, ConstantsUI.ICON_CLEAR_LOG_ENABLE,
                ConstantsUI.ICON_CLEAR_LOG_DISABLE, "");
        panelItem2.add(buttionClearLogs);

        buttionClearBaks = new MyIconButton(ConstantsUI.ICON_CLEAR_ALL_BAKS, ConstantsUI.ICON_CLEAR_ALL_BAKS_ENABLE,
                ConstantsUI.ICON_CLEAR_ALL_BAKS_DISABLE, "");
        panelItem3.add(buttionClearBaks);

        checkBoxAutoBak = new JCheckBox(PropertyUtil.getProperty("ds.ui.setting.autoBackUp"));
        checkBoxAutoBak.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        checkBoxAutoBak.setFont(ConstantsUI.FONT_RADIO);
        panelItem4.add(checkBoxAutoBak);*/

        JLabel label = new JLabel(PropertyUtil.getProperty("bricks.ui.setting.JSONPath"));
        textField = new JTextField();
        label.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        label.setFont(ConstantsUI.FONT_RADIO);
        textField.setFont(ConstantsUI.FONT_RADIO);
        Dimension dm = new Dimension(334, 26);
        textField.setPreferredSize(dm);
        panelItem5.add(label);
        panelItem5.add(textField);

/*        checkBoxStrict = new JCheckBox(PropertyUtil.getProperty("ds.ui.setting.strict"));
        checkBoxStrict.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        checkBoxStrict.setFont(ConstantsUI.FONT_RADIO);
        panelItem6.add(checkBoxStrict);

        checkBoxDebug = new JCheckBox(PropertyUtil.getProperty("ds.ui.setting.debugMode"));
        checkBoxDebug.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        checkBoxDebug.setFont(ConstantsUI.FONT_RADIO);
        panelItem7.add(checkBoxDebug);*/

        panelGridOption.add(panelItem1);
        panelGridOption.add(panelItem2);
        panelGridOption.add(panelItem3);
        panelGridOption.add(panelItem4);
        panelGridOption.add(panelItem5);
        panelGridOption.add(panelItem6);
        panelGridOption.add(panelItem7);

        panelCenter.add(panelGridOption);
        return panelCenter;
    }

    /**
     * �ײ����
     *
     * @return
     */
    private JPanel getDownPanel() {
        JPanel panelDown = new JPanel();
        panelDown.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelDown.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));

        buttonSave = new MyIconButton(ConstantsUI.ICON_SAVE, ConstantsUI.ICON_SAVE_ENABLE,
                ConstantsUI.ICON_SAVE_DISABLE, "");
        panelDown.add(buttonSave);

        return panelDown;
    }

/*    *//**
     * ���õ�ǰcomboxѡ��״̬
     *//*
    public static void setCurrentOption() {
        checkBoxAutoBak.setSelected(Boolean.parseBoolean(ConstantsTools.CONFIGER.getAutoBak()));
        checkBoxDebug.setSelected(Boolean.parseBoolean(ConstantsTools.CONFIGER.getDebugMode()));
        checkBoxStrict.setSelected(Boolean.parseBoolean(ConstantsTools.CONFIGER.getStrictMode()));
        textField.setText(ConstantsTools.CONFIGER.getMysqlPath());
    }*/

    /**
     * Ϊ����������¼�����
     */
/*    private void addListener() {
        buttonSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    ConstantsTools.CONFIGER.setAutoBak(String.valueOf(checkBoxAutoBak.isSelected()));
                    ConstantsTools.CONFIGER.setDebugMode(String.valueOf(checkBoxDebug.isSelected()));
                    ConstantsTools.CONFIGER.setStrictMode(String.valueOf(checkBoxStrict.isSelected()));
                    ConstantsTools.CONFIGER.setMysqlPath(textField.getText());
                    JOptionPane.showMessageDialog(AppMainWindow.settingPanel, PropertyUtil.getProperty("ds.ui.save.success"),
                            PropertyUtil.getProperty("ds.ui.tips"), JOptionPane.PLAIN_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(AppMainWindow.settingPanel, PropertyUtil.getProperty("ds.ui.save.fail") + e1.getMessage(),
                            PropertyUtil.getProperty("ds.ui.tips"),
                            JOptionPane.ERROR_MESSAGE);
                    logger.error("Write to xml file error" + e1.toString());
                }

            }
        });

        buttionTableFiled.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(ConstantsLogic.TABLE_FIELD_DIR));
                } catch (IOException e1) {
                    logger.error("open table_field file fail:" + e1.toString());
                    e1.printStackTrace();
                }

            }
        });

        buttionClearLogs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int answer = JOptionPane.showConfirmDialog(AppMainWindow.settingPanel,
                        PropertyUtil.getProperty("ds.ui.setting.clean.makeSure"),
                        PropertyUtil.getProperty("ds.ui.tips"), 2);

                if (answer == 0) {
                    FileOutputStream testfile = null;
                    try {
                        testfile = new FileOutputStream(ConstantsTools.PATH_LOG);
                        testfile.write(new String("").getBytes());
                        testfile.flush();
                        JOptionPane.showMessageDialog(AppMainWindow.settingPanel,
                                PropertyUtil.getProperty("ds.ui.setting.clean.success"),
                                PropertyUtil.getProperty("ds.ui.tips"),
                                JOptionPane.PLAIN_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(AppMainWindow.settingPanel,
                                PropertyUtil.getProperty("ds.ui.setting.clean.fail") + e1.getMessage(),
                                PropertyUtil.getProperty("ds.ui.tips"),
                                JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    } finally {
                        if (testfile != null) {
                            try {
                                testfile.close();
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(AppMainWindow.settingPanel,
                                        PropertyUtil.getProperty("ds.ui.setting.clean.fail") + e1.getMessage(),
                                        PropertyUtil.getProperty("ds.ui.tips"), JOptionPane.ERROR_MESSAGE);
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }
*/
}
