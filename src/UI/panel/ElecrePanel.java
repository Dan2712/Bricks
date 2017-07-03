package UI.panel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.android.ddmlib.IDevice;

import UI.ConstantsUI;
import UI.MyIconButton;
import node_selection.RealTimeScreenUI;
import node_selection.VariableChangeObserve;
import tools.ADB;
import tools.PropertyUtil;

/**
 *
 * @author DraLastat
 */
public class ElecrePanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("ElecrePanel.class");
	private static MyIconButton CheckStatus;
	private static MyIconButton buttonSave;
	
	private IDevice device;
	private VariableChangeObserve obs;
	
	/**
	 * 
	 */
	public ElecrePanel(VariableChangeObserve obs) {
		this.obs = obs;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
        ADB adb = new ADB();
        IDevice[] devices = adb.getDevices();
        if (devices.length <= 0) {
			LOG.error("ADB not connected, please check");
			return;
		}
		device = devices[0];
		
		this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		addComponent();
	}

	/**
	 * 
	 */
	private JPanel LiveviewPanel;
	private void addComponent() {
		LiveviewPanel = getCenterPanel();
		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(LiveviewPanel, BorderLayout.CENTER);
		this.add(getRightPanel(), BorderLayout.EAST);
	}
	public Map<String, String> getNode_info;
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

		RealTimeScreenUI mp = new RealTimeScreenUI(device, obs, this);
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
	private JTextField textFieldEleItem_1;
	private JTextField textFieldEleItem_4;
	private JPanel getRightPanel() {
		
		JPanel panelRight = new JPanel();
		Dimension preferredSize = new Dimension(280, 20);
		panelRight.setPreferredSize(preferredSize);
		panelRight.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelRight.setLayout(new GridLayout(3, 1));
		
		JPanel panelinfo = new JPanel();
/*		Dimension preferredSizeUP = new Dimension(10, 5);
		panelup.setPreferredSize(preferredSizeUP);*/
		panelinfo.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelinfo.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));
		
		JLabel ele_xpath = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item1"));
		JLabel checkable = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item2"));
		textFieldEleItem_1 = new JTextField();
		JTextField textFieldEleItem_2 = new JTextField();
		textFieldEleItem_1.setEditable(false);
		
		ele_xpath.setFont(ConstantsUI.SEC_TITLE);
		checkable.setFont(ConstantsUI.SEC_TITLE);
		textFieldEleItem_1.setFont(ConstantsUI.FONT_NORMAL);
		textFieldEleItem_2.setFont(ConstantsUI.FONT_NORMAL);
		
		ele_xpath.setPreferredSize(ConstantsUI.LABLE_SIZE_ELE_ITEM);				
		checkable.setPreferredSize(ConstantsUI.LABLE_SIZE_ELE_ITEM);
		textFieldEleItem_1.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		textFieldEleItem_2.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		
		panelinfo.add(ele_xpath);
		panelinfo.add(textFieldEleItem_1);
		panelinfo.add(checkable);
		panelinfo.add(textFieldEleItem_2);

		JPanel panelGridSetting = new JPanel();
		panelGridSetting.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridSetting.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));

		JLabel app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item4"));
		JLabel view_name = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item5"));
		JLabel ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item6"));
		textFieldEleItem_4 = new JTextField();
		JTextField textFieldEleItem_5 = new JTextField();
		JTextField textFieldEleItem_6 = new JTextField();

		app_name.setFont(ConstantsUI.SEC_TITLE);
		view_name.setFont(ConstantsUI.SEC_TITLE);
		ele_name.setFont(ConstantsUI.SEC_TITLE);
		textFieldEleItem_4.setFont(ConstantsUI.FONT_NORMAL);
		textFieldEleItem_5.setFont(ConstantsUI.FONT_NORMAL);
		textFieldEleItem_6.setFont(ConstantsUI.FONT_NORMAL);

		app_name.setPreferredSize(ConstantsUI.LABLE_SIZE_ELE_ITEM);
		view_name.setPreferredSize(ConstantsUI.LABLE_SIZE_ELE_ITEM);				
		ele_name.setPreferredSize(ConstantsUI.LABLE_SIZE_ELE_ITEM);
		textFieldEleItem_4.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		textFieldEleItem_5.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		textFieldEleItem_6.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		
		panelGridSetting.add(app_name);
		panelGridSetting.add(textFieldEleItem_4);
		panelGridSetting.add(view_name);
		panelGridSetting.add(textFieldEleItem_5);
		panelGridSetting.add(ele_name);
		panelGridSetting.add(textFieldEleItem_6);

		JPanel panelGridSave = new JPanel();
		panelGridSave.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridSave.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
		JLabel labelEleItemNull_1 = new JLabel();
		labelEleItemNull_1.setPreferredSize(ConstantsUI.LABLE_SIZE_NULL_ITEM);
        buttonSave = new MyIconButton(ConstantsUI.ICON_SAVE, ConstantsUI.ICON_SAVE_ENABLE,
                ConstantsUI.ICON_SAVE_DISABLE, "");
        panelGridSave.add(labelEleItemNull_1);
        panelGridSave.add(buttonSave);

		panelRight.add(panelinfo);
		panelRight.add(panelGridSetting);
		panelRight.add(panelGridSave);
		return panelRight;
	}

	public void observe(Observable o) {
	    o.addObserver(this);
	    }

	@Override
	public void update(Observable o, Object arg) {
		Map<String, String> node_info = ((VariableChangeObserve) o).getInfo();
		textFieldEleItem_1.setText(node_info.get("xpath"));
		textFieldEleItem_4.setText(node_info.get("package"));
	}
}
