package UI.panel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.android.ddmlib.IDevice;

import UI.CheckButton;
import UI.ConstantsUI;
import UI.MyIconButton;
import node_selection.RealTimeScreenUI;
import node_selection.VariableChangeObserve;
import tools.ADB;
import tools.PropertyUtil;
import tools.SQLUtils;

/**
 *
 * @author DraLastat
 */
public class ElecrePanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("ElecrePanel.class");
	private static MyIconButton ClickStatus;
	private static MyIconButton CheckStatus;
	private static MyIconButton ScrollStatus;
	private static MyIconButton Focustatus;
	private static MyIconButton LongClickStatus;
	private static MyIconButton buttonSave;
	private static ImageIcon chktrue;
	private static ImageIcon chkfalse;
	
	private static CheckButton chkbtn;
	
	private IDevice device;
	private VariableChangeObserve obs;
	public final static String CURRENT_DIR = System.getProperty("user.dir");
	
	private SQLUtils sql;
	/**
	 * 
	 */
	public ElecrePanel(VariableChangeObserve obs, SQLUtils sql) {
		this.obs = obs;
		this.sql = sql;
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
		addListener();
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
	private JTextField textFieldEleItem_5;
	private JTextField textFieldEleItem_6;
	private ImageIcon chkimage;
	private ImageIcon icon;
	//private String click_chkstatus = null;
	private JPanel panelRight;
	


	private JPanel getRightPanel() {
		
		panelRight = new JPanel();
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
		JLabel clickchk = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.clickchk"));
		JLabel scrollchk = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.scrollchk"));
		JLabel checkchk = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.checkchk"));
		JLabel focuschk = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.focuschk"));
		JLabel long_clickchk = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.longclickchk"));
		JLabel label_null = new JLabel();
		ClickStatus = new MyIconButton(ConstantsUI.ICON_ELE_CHK, ConstantsUI.ICON_ELE_CHK_TRUE,
                ConstantsUI.ICON_ELE_CHK_FALSE, "");
		CheckStatus = new MyIconButton(ConstantsUI.ICON_ELE_CHK, ConstantsUI.ICON_ELE_CHK_TRUE,
                ConstantsUI.ICON_ELE_CHK_FALSE, "");
		ScrollStatus = new MyIconButton(ConstantsUI.ICON_ELE_CHK, ConstantsUI.ICON_ELE_CHK_TRUE,
                ConstantsUI.ICON_ELE_CHK_FALSE, "");
		Focustatus = new MyIconButton(ConstantsUI.ICON_ELE_CHK, ConstantsUI.ICON_ELE_CHK_TRUE,
                ConstantsUI.ICON_ELE_CHK_FALSE, "");
		LongClickStatus = new MyIconButton(ConstantsUI.ICON_ELE_CHK, ConstantsUI.ICON_ELE_CHK_TRUE,
                ConstantsUI.ICON_ELE_CHK_FALSE, "");
		
		chkbtn = new CheckButton(ConstantsUI.ICON_ELE_CHK_TRUE,ConstantsUI.ICON_ELE_CHK_FALSE,"");
		JLabel chbt = new JLabel();
		ImageIcon image = new ImageIcon(CURRENT_DIR + File.separator + "icon" + File.separator + "true.png");
		
		
		if(node_info != null && node_info.containsKey("clickable")){
			if(node_info.get("clickable") == "true"){
				System.out.println("click" + node_info.get("clickable"));
				panelRight.updateUI();
			}else{
				System.out.println("click" + node_info.get("clickable"));
				
			}
		}

		
		ele_xpath.setFont(ConstantsUI.SEC_TITLE);
		checkable.setFont(ConstantsUI.SEC_TITLE);
		textFieldEleItem_1.setFont(ConstantsUI.FONT_NORMAL);
		textFieldEleItem_2.setFont(ConstantsUI.FONT_NORMAL);
		clickchk.setFont(ConstantsUI.FONT_NORMAL);
		scrollchk.setFont(ConstantsUI.FONT_NORMAL);
		checkchk.setFont(ConstantsUI.FONT_NORMAL);
		focuschk.setFont(ConstantsUI.FONT_NORMAL);
		long_clickchk.setFont(ConstantsUI.FONT_NORMAL);
		
		ele_xpath.setPreferredSize(ConstantsUI.LABLE_SIZE_ELE_ITEM);				
		checkable.setPreferredSize(ConstantsUI.LABLE_SIZE_ELE_ITEM);
		label_null.setPreferredSize(ConstantsUI.LABLE_NULL_ITEM);
		textFieldEleItem_1.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		textFieldEleItem_2.setPreferredSize(ConstantsUI.TEXT_FIELD_SIZE_ITEM);
		
		panelinfo.add(ele_xpath);
		panelinfo.add(textFieldEleItem_1);
		panelinfo.add(checkable);
		panelinfo.add(label_null);
		panelinfo.add(clickchk);
		panelinfo.add(ClickStatus);
		panelinfo.add(scrollchk);
		panelinfo.add(ScrollStatus);
		panelinfo.add(checkchk);
		panelinfo.add(CheckStatus);
		panelinfo.add(focuschk);
		panelinfo.add(Focustatus);
		panelinfo.add(long_clickchk);
		panelinfo.add(LongClickStatus);

		JPanel panelGridSetting = new JPanel();
		panelGridSetting.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridSetting.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));

		JLabel app_name = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item4"));
		JLabel view_name = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item5"));
		JLabel ele_name = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.item6"));
		textFieldEleItem_4 = new JTextField();
		textFieldEleItem_5 = new JTextField();
		textFieldEleItem_6 = new JTextField();

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

	
	Map<String, String> node_info;
	@Override
	public void update(Observable o, Object arg) {
		node_info = ((VariableChangeObserve) o).getInfo();
		textFieldEleItem_1.setText(node_info.get("xpath"));
		if(node_info.get("package").equals("dji.pilot"))
			textFieldEleItem_4.setText("DJI GO3");
		else if (node_info.get("package").equals("dji.go.v4"))
			textFieldEleItem_4.setText("DJI GO4");
		
		if(node_info.get("focusable").equals("1")){
			ClickStatus.setSelected(true);
			Focustatus.setSelectedIcon(ConstantsUI.ICON_ELE_CHK_TRUE);
			System.out.println("click" + node_info.get("clickable"));
			panelRight.updateUI();
		}else{
			Focustatus.setSelectedIcon(ConstantsUI.ICON_ELE_CHK_FALSE);
			ClickStatus.setSelected(false);
			System.out.println("click" + node_info.get("clickable"));
			panelRight.updateUI();
		}
	}
	private String app_name_text;
	private String custom_name_text;
	private String xpath_text;
	private String state_text;
	private String activity_name_text;
	private String screen_text;
	Map<String, String> app_name = new HashMap<String, String>();
	Map<String, String> custom_name = new HashMap<String, String>();
	Map<String, String> xpath = new HashMap<String, String>();
	Map<String, String> state = new HashMap<String, String>();
	Map<String, String> activity_name = new HashMap<String, String>();
	Map<String, String> screen = new HashMap<String, String>();
	
	ArrayList<Map<String, String>> sqllist = new ArrayList<Map<String, String>>();
	
	public void addListener() {
		buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	app_name_text = textFieldEleItem_4.getText();
                	activity_name_text = textFieldEleItem_5.getText();
                	custom_name_text = textFieldEleItem_6.getText();
                	state_text = node_info.get("clickable") + node_info.get("scrollable") + node_info.get("checkable") + node_info.get("focusable") + node_info.get("long-clickable");
                	xpath_text = node_info.get("xpath");
                	screen_text = node_info.get("screenPath");
                	
                	app_name.put("APP_NAME", app_name_text);
                	custom_name.put("CUSTOM_NAME", custom_name_text);
                	activity_name.put("ACTIVITY_NAME", activity_name_text);
                	xpath.put("XPATH", xpath_text);
                	state.put("STATE", state_text);
                	screen.put("SCREEN_PATH", screen_text);
                	sqllist.add(app_name);
                	sqllist.add(custom_name);
                	sqllist.add(activity_name);
                	sqllist.add(xpath);
                	sqllist.add(state);
                	sqllist.add(screen);
                	
                	sql.insertEle("ELEMENT", sqllist);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
	}
}
