package UI.panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.android.ddmlib.IDevice;

import UI.AppMainWindow;
import UI.ConstantsUI;
import tools.PropertyUtil;
/**
 *
 * @author DraLastat
 */
public class CaserunPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static JPanel panelResult;
	private static JPanel panelData;
	public static JPanel caserunPanelMain;
	private static JPanel caserunPanelResult;
	private static JPanel caserunPanelData;
	private IDevice device;
	
	/**
	 * 
	 */
	public CaserunPanel(IDevice device) {
		this.device = device;
		initialize();
		addComponent();
		addListener();
	}

	/**
	 * 
	 */
	private void initialize() {
		this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		caserunPanelResult = new CaserunResultPanel(device);
		caserunPanelData = new CaserunDataPanel();
	}

	/**
	 * 
	 */
	private void addComponent() {

		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);

	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));

		JLabel labelTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.caserun.title"));
		labelTitle.setFont(ConstantsUI.FONT_TITLE);
		labelTitle.setForeground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);

		return panelUp;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private JPanel getCenterPanel() {
		//
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new BorderLayout());

		//
		JPanel panelList = new JPanel();
		Dimension preferredSize = new Dimension(245, ConstantsUI.MAIN_WINDOW_HEIGHT);
		panelList.setPreferredSize(preferredSize);
		panelList.setBackground(new Color(62, 62, 62));
		panelList.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		panelResult = new JPanel();
		panelResult.setBackground(new Color(120, 165, 202));
		panelResult.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 13));
		Dimension preferredSizeListItem = new Dimension(245, 48);
		panelResult.setPreferredSize(preferredSizeListItem);
		panelData = new JPanel();
		panelData.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelData.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 13));
		panelData.setPreferredSize(preferredSizeListItem);

		JLabel labelOption = new JLabel(PropertyUtil.getProperty("bricks.ui.caserun.result"));
		JLabel labelAbout = new JLabel(PropertyUtil.getProperty("bricks.ui.caserun.data"));
		Font fontListItem = new Font(PropertyUtil.getProperty("ds.ui.font.family"), 0, 15);
		labelOption.setFont(fontListItem);
		labelAbout.setFont(fontListItem);
		labelOption.setForeground(Color.white);
		labelAbout.setForeground(Color.white);
		panelResult.add(labelOption);
		panelData.add(labelAbout);

		panelList.add(panelResult);
		panelList.add(panelData);

		//
		caserunPanelMain = new JPanel();
		caserunPanelMain.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		caserunPanelMain.setLayout(new BorderLayout());
		caserunPanelMain.add(caserunPanelResult);

		panelCenter.add(panelList, BorderLayout.WEST);
		panelCenter.add(caserunPanelMain, BorderLayout.CENTER);

		return panelCenter;
	}
	/**
	 *
	 */
	private void addListener() {
		panelResult.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				panelResult.setBackground(new Color(120, 165, 202));
				panelData.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);

				CaserunPanel.caserunPanelMain.removeAll();
				CaserunPanel.caserunPanelMain.add(caserunPanelResult);
				AppMainWindow.caserunPanel.updateUI();

			}
		});

		panelData.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				panelData.setBackground(new Color(120, 165, 202));
				panelResult.setBackground(ConstantsUI.TOOL_BAR_BACK_COLOR);

				CaserunPanel.caserunPanelMain.removeAll();
				CaserunPanel.caserunPanelMain.add(caserunPanelData);
				AppMainWindow.caserunPanel.updateUI();

			}
		});

	}
	
}