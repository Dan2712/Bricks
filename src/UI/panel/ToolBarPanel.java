package UI.panel;

import UI.AppMainWindow;
import UI.ConstantsUI;
import UI.MyIconButton;
import tools.PropertyUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 工具栏面板
 * 
 * @author DraLastat
 *
 */
public class ToolBarPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static MyIconButton buttonStatus;
	private static MyIconButton buttonElecre;
	private static MyIconButton buttonCasecre;
	private static MyIconButton buttonCaserun;
	private static MyIconButton buttonSetting;

	/**
	 * 构造
	 */
	public ToolBarPanel() {
		initialize();
		addButton();
		addListener();
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		Dimension preferredSize = new Dimension(48, ConstantsUI.MAIN_WINDOW_HEIGHT);
		this.setPreferredSize(preferredSize);
		this.setMaximumSize(preferredSize);
		this.setMinimumSize(preferredSize);
		this.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		this.setLayout(new GridLayout(2, 1));
	}

	/**
	 * 添加工具按钮
	 */
	private void addButton() {

		JPanel panelUp = new JPanel();
		panelUp.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(-2, -2, -4));
		JPanel panelDown = new JPanel();
		panelDown.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelDown.setLayout(new BorderLayout(0, 0));

		buttonStatus = new MyIconButton(ConstantsUI.ICON_STATUS_ENABLE, ConstantsUI.ICON_STATUS_ENABLE,
				ConstantsUI.ICON_STATUS, PropertyUtil.getProperty("bricks.ui.status.title"));
		buttonElecre = new MyIconButton(ConstantsUI.ICON_DATABASE, ConstantsUI.ICON_DATABASE_ENABLE,
				ConstantsUI.ICON_DATABASE, PropertyUtil.getProperty("bricks.ui.elecre.title"));
		buttonCasecre = new MyIconButton(ConstantsUI.ICON_SCHEDULE, ConstantsUI.ICON_SCHEDULE_ENABLE,
				ConstantsUI.ICON_SCHEDULE, PropertyUtil.getProperty("bricks.ui.casecre.title"));
		buttonCaserun = new MyIconButton(ConstantsUI.ICON_BACKUP, ConstantsUI.ICON_BACKUP_ENABLE,
				ConstantsUI.ICON_BACKUP, PropertyUtil.getProperty("bricks.ui.caserun.title"));
		buttonSetting = new MyIconButton(ConstantsUI.ICON_SETTING, ConstantsUI.ICON_SETTING_ENABLE,
				ConstantsUI.ICON_SETTING, PropertyUtil.getProperty("bricks.ui.setting.title"));

		panelUp.add(buttonStatus);
		panelUp.add(buttonElecre);
		panelUp.add(buttonCasecre);
		panelUp.add(buttonCaserun);

		panelDown.add(buttonSetting, BorderLayout.SOUTH);
		this.add(panelUp);
		this.add(panelDown);

	}

	/**
	 * 为各按钮添加事件动作监听
	 */
	private void addListener() {
		buttonStatus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(ConstantsUI.ICON_STATUS_ENABLE);
				buttonElecre.setIcon(ConstantsUI.ICON_DATABASE);
				buttonCasecre.setIcon(ConstantsUI.ICON_SCHEDULE);
				buttonCaserun.setIcon(ConstantsUI.ICON_BACKUP);
				buttonSetting.setIcon(ConstantsUI.ICON_SETTING);

				AppMainWindow.mainPanelCenter.removeAll();
				//StatusPanel.setContent();
				AppMainWindow.mainPanelCenter.add(AppMainWindow.statusPanel, BorderLayout.CENTER);

				AppMainWindow.mainPanelCenter.updateUI();
			}
		});

		buttonElecre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(ConstantsUI.ICON_STATUS);
				buttonElecre.setIcon(ConstantsUI.ICON_DATABASE_ENABLE);
				buttonCasecre.setIcon(ConstantsUI.ICON_SCHEDULE);
				buttonCaserun.setIcon(ConstantsUI.ICON_BACKUP);
				buttonSetting.setIcon(ConstantsUI.ICON_SETTING);

				AppMainWindow.mainPanelCenter.removeAll();
				AppMainWindow.mainPanelCenter.add(AppMainWindow.elecrePanel, BorderLayout.CENTER);

				AppMainWindow.mainPanelCenter.updateUI();
			}
		});

		buttonCasecre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(ConstantsUI.ICON_STATUS);
				buttonElecre.setIcon(ConstantsUI.ICON_DATABASE);
				buttonCasecre.setIcon(ConstantsUI.ICON_SCHEDULE_ENABLE);
				buttonCaserun.setIcon(ConstantsUI.ICON_BACKUP);
				buttonSetting.setIcon(ConstantsUI.ICON_SETTING);

				AppMainWindow.mainPanelCenter.removeAll();
				AppMainWindow.mainPanelCenter.add(AppMainWindow.casecrePanel, BorderLayout.CENTER);

				AppMainWindow.mainPanelCenter.updateUI();

			}
		});

		buttonCaserun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(ConstantsUI.ICON_STATUS);
				buttonElecre.setIcon(ConstantsUI.ICON_DATABASE);
				buttonCasecre.setIcon(ConstantsUI.ICON_SCHEDULE);
				buttonCaserun.setIcon(ConstantsUI.ICON_BACKUP_ENABLE);
				buttonSetting.setIcon(ConstantsUI.ICON_SETTING);

				AppMainWindow.mainPanelCenter.removeAll();
				AppMainWindow.mainPanelCenter.add(AppMainWindow.caserunPanel, BorderLayout.CENTER);

				AppMainWindow.mainPanelCenter.updateUI();

			}
		});

		buttonSetting.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStatus.setIcon(ConstantsUI.ICON_STATUS);
				buttonElecre.setIcon(ConstantsUI.ICON_DATABASE);
				buttonCasecre.setIcon(ConstantsUI.ICON_SCHEDULE);
				buttonCaserun.setIcon(ConstantsUI.ICON_BACKUP);
				buttonSetting.setIcon(ConstantsUI.ICON_SETTING_ENABLE);

				AppMainWindow.mainPanelCenter.removeAll();
				AppMainWindow.mainPanelCenter.add(AppMainWindow.settingPanel, BorderLayout.CENTER);

				AppMainWindow.mainPanelCenter.updateUI();

			}
		});
	}
}
