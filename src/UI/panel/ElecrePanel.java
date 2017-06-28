package UI.panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.android.ddmlib.IDevice;

import UI.ConstantsUI;
import UI.MyIconButton;
import mini_decode.AndroidScreenObserver;
import mini_decode.MiniCapUtil;
import node_selection.RealTimeScreenUI;
import tools.ADB;
import tools.BricksDrag;
import tools.PropertyUtil;
/**
 * 
 *
 * @author DraLastat
 */
public class ElecrePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("ElecrePanel.class");
	
	
	private IDevice device;
	/**
	 * 
	 */
	public ElecrePanel() {
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
        ADB adb = new ADB();
        if (adb.getDevices().length <= 0) {
			LOG.error("无连接设备,请检查");
			return;
		}
		device = adb.getDevices()[0];
		
		this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		addComponent();
	}

	/**
	 * 
	 */
	JPanel LiveviewPanel ;
	private void addComponent() {
		LiveviewPanel = getCenterPanel();
		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(LiveviewPanel, BorderLayout.CENTER);
		this.add(getRightPanel(), BorderLayout.EAST);
	}
	/**
	 *
	 * @return
	 */
	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));

		JLabel labelTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.title"));
		labelTitle.setFont(ConstantsUI.FONT_TITLE);
		labelTitle.setForeground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);
		
		return panelUp;
	}
	/**
	 * 
	 * @return
	 */
	private JPanel getCenterPanel() {
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new BorderLayout());

		RealTimeScreenUI mp = new RealTimeScreenUI(device);
		mp.addMouseListener(mp);
		mp.addMouseMotionListener(mp);
		
		panelCenter.add(mp, BorderLayout.CENTER);
		panelCenter.updateUI();

		return panelCenter;
	}
	
	/**
	 * 
	 * @return
	 */
	private JPanel getRightPanel() {
		JPanel panelRight = new JPanel();
		panelRight.setBackground(Color.BLACK);
		panelRight.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		return panelRight;
	}
	
	
}
