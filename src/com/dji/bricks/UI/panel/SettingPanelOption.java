package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.tools.PropertyUtil;

/**
 * 
 * @author DraLastat
 */
public class SettingPanelOption extends JPanel {

    private static final long serialVersionUID = 1L;

    private static MyIconButton buttonSave;

    private static JTextField textField;

    private static Logger logger = Logger.getLogger(SettingPanelOption.class);

    public SettingPanelOption() {
        initialize();
        addComponent();
    }

    private void initialize() {
        this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        this.setLayout(new BorderLayout());
    }

    private void addComponent() {

        this.add(getCenterPanel(), BorderLayout.CENTER);
        this.add(getDownPanel(), BorderLayout.SOUTH);

    }

    private JPanel getCenterPanel() {
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelCenter.setLayout(new GridLayout(1, 1));

        JPanel panelGridOption = new JPanel();
        panelGridOption.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelGridOption.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));

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

        JLabel label = new JLabel(PropertyUtil.getProperty("bricks.ui.setting.JSONPath"));
        textField = new JTextField();
        label.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        label.setFont(ConstantsUI.FONT_RADIO);
        textField.setFont(ConstantsUI.FONT_RADIO);
        Dimension dm = new Dimension(334, 26);
        textField.setPreferredSize(dm);
        panelItem5.add(label);
        panelItem5.add(textField);

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

    private JPanel getDownPanel() {
        JPanel panelDown = new JPanel();
        panelDown.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelDown.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));

        buttonSave = new MyIconButton(ConstantsUI.ICON_SAVE, ConstantsUI.ICON_SAVE_ENABLE,
                ConstantsUI.ICON_SAVE_DISABLE, "");
        panelDown.add(buttonSave);

        return panelDown;
    }

}
