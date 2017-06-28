package UI.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import UI.ConstantsUI;
import UI.MyIconButton;
import tools.PropertyUtil;

public class CaserunResultPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static MyIconButton buttonCaseFind;
	private static MyIconButton buttonStart;
	private static MyIconButton buttonStop;

	private static Logger logger = Logger.getLogger(CaserunResultPanel.class);

	/**
	 * ����
	 */
	public CaserunResultPanel() {
		initialize();
		addComponent();
		//setContent();
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
		panelCenter.setLayout(new GridLayout(2, 1));
		
		/**
		 * ѡ������
		 * ��ʼִ��/ִֹͣ��
		 *
		 */
		// ����Grid
		JPanel panelGridCaseFind = new JPanel();
		panelGridCaseFind.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridCaseFind.setLayout(new GridLayout(2, 1));

        JPanel panelGrid1 = new JPanel();
        panelGrid1.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelGrid1.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 15));
        JPanel panelGrid2 = new JPanel();
        panelGrid2.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelGrid2.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));

        buttonCaseFind = new MyIconButton(ConstantsUI.ICON_START_SCHEDULE, ConstantsUI.ICON_START_SCHEDULE_ENABLE,
                ConstantsUI.ICON_START_SCHEDULE_DISABLE, "");
        buttonStart = new MyIconButton(ConstantsUI.ICON_START, ConstantsUI.ICON_START_ENABLE,
                ConstantsUI.ICON_START_DISABLE, "");
        buttonStart.setEnabled(false);
        buttonStop = new MyIconButton(ConstantsUI.ICON_STOP, ConstantsUI.ICON_STOP_ENABLE,
                ConstantsUI.ICON_STOP_DISABLE, "");
        buttonStop.setEnabled(false);
        panelGrid1.add(buttonCaseFind);
        panelGrid2.add(buttonStart);
        panelGrid2.add(buttonStop);

        panelGridCaseFind.add(panelGrid1);
        panelGridCaseFind.add(panelGrid2);
		
		/**
		 * ִ�н����ӡ
		 * 
		 *
		 */
		// ����Grid
		JPanel panelGridLog = new JPanel();
		panelGridLog.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridLog.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));

		// ��ʼ�����
		JLabel labelRunLogTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.caserun.logprint"));
		JLabel labellogprintNull = new JLabel();
		JTextArea logprint = new JTextArea(12, 76);
		logprint.setBorder(null);
		logprint.setBorder(new EmptyBorder(0,0,0,0));
		logprint.setLineWrap(true);
        logprint.setWrapStyleWord(true);
        logprint.setForeground(ConstantsUI.MAIN_BACK_COLOR);
        logprint.setBackground(ConstantsUI.TABLE_BACK_COLOR);
		// ����
		labelRunLogTitle.setFont(ConstantsUI.FONT_NORMAL);
		// ��С
		labelRunLogTitle.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		labellogprintNull.setPreferredSize(ConstantsUI.LABLE_SIZE_CASE_NULL_ITEM);
		// ���Ԫ��
		panelGridLog.add(labelRunLogTitle);
		panelGridLog.add(labellogprintNull);
		panelGridLog.add(new JScrollPane(logprint));


		panelCenter.add(panelGridCaseFind);
		panelCenter.add(panelGridLog);
		return panelCenter;
	}


}
