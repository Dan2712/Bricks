package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.android.ddmlib.IDevice;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.MainEntry;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.tools.PropertyUtil;

/**
 * 
 * @author DraLastat
 *
 */
public class ToolBarPanel extends JPanel implements GlobalObserver {

	private static final long serialVersionUID = 1L;

	private static MyIconButton buttonStatus;
	private static MyIconButton buttonElecre;
	private static MyIconButton buttonCasecre;
	private static MyIconButton buttonCaserun;
	private static MyIconButton buttonSetting;

	/**
	 * 
	 */
	public ToolBarPanel() {
		initialize();
		addButton();
		addListener();
	}

	/**
	 * 
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
	 * 
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
	 * 
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

				MainEntry.mainPanelCenter.removeAll();
				//StatusPanel.setContent();
				MainEntry.mainPanelCenter.add(MainEntry.statusPanel, BorderLayout.CENTER);

				MainEntry.mainPanelCenter.updateUI();
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

				MainEntry.mainPanelCenter.removeAll();
				MainEntry.mainPanelCenter.add(MainEntry.elecrePanel, BorderLayout.CENTER);

				MainEntry.mainPanelCenter.updateUI();
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

				MainEntry.mainPanelCenter.removeAll();
				MainEntry.mainPanelCenter.add(MainEntry.casecrePanel, BorderLayout.CENTER);

				MainEntry.mainPanelCenter.updateUI();

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

				MainEntry.mainPanelCenter.removeAll();
				MainEntry.mainPanelCenter.add(MainEntry.caserunPanel, BorderLayout.CENTER);

				MainEntry.mainPanelCenter.updateUI();

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

				MainEntry.mainPanelCenter.removeAll();
				MainEntry.mainPanelCenter.add(MainEntry.settingPanel, BorderLayout.CENTER);

				MainEntry.mainPanelCenter.updateUI();

			}
		});
	}

	@Override
	public void frameImageChange(BufferedImage image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ADBChange(IDevice[] devices) {
		// TODO Auto-generated method stub
		
	}
}
