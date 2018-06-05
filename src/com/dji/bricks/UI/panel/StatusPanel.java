package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.tools.PropertyUtil;

/**
 *
 * @author DraLastat
 */

public class StatusPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(StatusPanel.class);
  
    public StatusPanel() {
        super(true);
        initialize();
        addComponent();
    }

    private void initialize() {
        this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        this.setLayout(new BorderLayout());
    }

    private void addComponent() {
        this.add(getUpPanel(), BorderLayout.NORTH);
        this.add(getDownPanel(), BorderLayout.SOUTH);

    }
    
    private JPanel getUpPanel() {
        JPanel panelUp = new JPanel();
        panelUp.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));

        JLabel labelTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.status.name"));
        labelTitle.setFont(ConstantsUI.FONT_TITLE);
        labelTitle.setForeground(ConstantsUI.TOOL_BAR_BACK_COLOR);
        panelUp.add(labelTitle);

        return panelUp;
    }

	private JPanel getDownPanel() {
		JPanel panelDown = new JPanel();
		panelDown.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelDown.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));

		JLabel labelInfo = new JLabel(PropertyUtil.getProperty("bricks.ui.app.info"));
		labelInfo.setFont(ConstantsUI.FONT_NORMAL);
		labelInfo.setForeground(Color.gray);

		panelDown.add(labelInfo);

		return panelDown;
	}


}
