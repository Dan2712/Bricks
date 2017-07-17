package com.dji.bricks.UI.panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.alibaba.fastjson.JSON;

import com.dji.bricks.UI.BrickBean;
import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.tools.PropertyUtil;
import com.dji.bricks.tools.SQLUtils;

/**
 *
 * @author DraLastat
 */
public class CasecrePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static MyIconButton buttonEleAdd;
	private static MyIconButton buttonActAdd;
	private static MyIconButton buttonVerAdd;
	private static MyIconButton buttonSave;
	private int id;
	private int type;
	private LinkedList<BrickBean> caseList = new LinkedList<>();
	
	private String xpath = "";
	private String cus_name = "";
	private String val = "";
	private int action;
	/**
	 * 
	 */
	public CasecrePanel(SQLUtils sql) {
		this.sql = sql;
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
	}

	/**
	 * 
	 */
	private JPanel CaseCre;
	private void addComponent() {
		CaseCre = getDownPanel();
		this.add(getUpPanel(), BorderLayout.NORTH);
		//this.add(getLeftPanel(), BorderLayout.WEST);
		//this.add(getRightPanel(), BorderLayout.EAST);
		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(getDownPanel(), BorderLayout.SOUTH);
	}
	/**
	 * 
	 * @return
	 */
	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 5));

		JLabel labelTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.title"));
		labelTitle.setFont(ConstantsUI.FONT_TITLE);
		labelTitle.setForeground(ConstantsUI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);

		return panelUp;
	}
	
	private JComboBox<String> comboxAppName;
	private JComboBox<String> comboxViewName;
	private JComboBox<String> comboxEleName;
	private JComboBox<String> comboxActName;
	private JComboBox<String> comboxVerName;
	private String appName = "";
	
	private SQLUtils sql = null;
	private ResultSet xpathSet = null;
	
	private ViewListener vlisten = new ViewListener();
	private EleListener elisten = new EleListener();
	/**
	 * 
	 * @return
	 */
	private JPanel getCenterPanel() {
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new GridLayout(1, 3));
		
		/**
		 * 
		 * 
		 * @return
		 */
		
		JPanel panelGridElePick = new JPanel();
		panelGridElePick.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridElePick.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
		
		
		JLabel labelElePick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.elepick"));
        buttonEleAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, "");
		JLabel labelEleNull = new JLabel();
		JLabel labelEleSave_app = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.app"));
		JLabel labelEleSave_view = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.view"));
		JLabel labelEleSave_name = new JLabel(PropertyUtil.getProperty("bricks.ui.elecre.name"));
		
		comboxAppName = new JComboBox<String>();
		comboxAppName.addItem("DJI GO3");
		comboxAppName.addItem("DJI GO4");
		comboxAppName.setEditable(false);
		comboxAppName.setSelectedItem(null);
		
		comboxViewName = new JComboBox<String>();
		comboxViewName.setEditable(false);
		
		comboxEleName = new JComboBox<String>();
		comboxEleName.setEditable(false);
		
		comboxActName = new JComboBox<String>();
		comboxActName.setEditable(false);
		comboxActName.setSelectedItem(null);
		comboxActName.addItem("click");
		comboxActName.addItem("longPress");
		comboxActName.addItem("setText");
		comboxActName.addItem("dragBar");
		comboxActName.setSelectedItem(null);
		comboxActName.addItemListener(new ActListener());
		
		// App selection change listener
		comboxAppName.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					appName = (String) e.getItem();
					ResultSet rs = sql.queryElement("ACTIVITY", appName);
					
					comboxViewName.removeAllItems();
					comboxEleName.removeAllItems();
					comboxViewName.removeItemListener(vlisten);
					try {
						while (rs.next()) {
							String viewName = rs.getString("ACTIVITY_NAME");
							comboxViewName.addItem(viewName);
						}
						comboxViewName.setSelectedItem(null);
						comboxViewName.addItemListener(vlisten);
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						if (rs != null) {
							try {
								rs.close();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		// 
		labelElePick.setFont(ConstantsUI.SEC_TITLE);
		labelEleSave_app.setFont(ConstantsUI.FONT_NORMAL);
		labelEleSave_view.setFont(ConstantsUI.FONT_NORMAL);
		labelEleSave_name.setFont(ConstantsUI.FONT_NORMAL);
		comboxAppName.setFont(ConstantsUI.FONT_NORMAL);
		comboxViewName.setFont(ConstantsUI.FONT_NORMAL);
		comboxEleName.setFont(ConstantsUI.FONT_NORMAL);
		

		// 
		labelElePick.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		labelEleNull.setPreferredSize(ConstantsUI.LABLE_SIZE_CASECRE_NULL_ITEM);
		labelEleSave_app.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		labelEleSave_view.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		labelEleSave_name.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		comboxAppName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		comboxViewName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		comboxEleName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		
		panelGridElePick.add(labelElePick);
		panelGridElePick.add(buttonEleAdd);
		panelGridElePick.add(labelEleNull);
		panelGridElePick.add(labelEleSave_app);
		panelGridElePick.add(comboxAppName);
		panelGridElePick.add(labelEleSave_view);
		panelGridElePick.add(comboxViewName);
		panelGridElePick.add(labelEleSave_name);
		panelGridElePick.add(comboxEleName);
		
		/**
		 * 
		 * 
		 * @return
		 */
		JPanel panelGridActPick = new JPanel();
		panelGridActPick.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridActPick.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));

		/**
		 *
		 * 
		 * @return
		 */
		JPanel panelGridVerPick = new JPanel();
		panelGridVerPick.setBackground(ConstantsUI.MAIN_BACK_COLOR);
		panelGridVerPick.setLayout(new FlowLayout(FlowLayout.LEFT, ConstantsUI.MAIN_H_GAP, 0));
		
		JLabel labelActPick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.actpick"));
        buttonActAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, "");
		JLabel labelActNull = new JLabel();
		JLabel labelActName = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.actname"));
		
		JLabel labelVerPick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.verpick"));
        buttonVerAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, "");
		JLabel labelVerNull = new JLabel();
		JLabel labelVerName = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.vername"));
        buttonSave = new MyIconButton(ConstantsUI.ICON_SAVE, ConstantsUI.ICON_SAVE_ENABLE,
                ConstantsUI.ICON_SAVE_DISABLE, "");
        JLabel labelNull_2 = new JLabel();
		
		comboxVerName = new JComboBox<String>();
		comboxVerName.addItem("Text Validation");
		comboxVerName.addItem("Image Validation");
		comboxVerName.addItem("Element Exist Validation");
		comboxVerName.setEditable(false);
		comboxVerName.setSelectedItem(null);
		comboxVerName.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					switch ((String) e.getItem()) {
					case "Text Validation":
						val = "TV";
						String ele_path = "//android.widget.TextView[@resource-id='dji.go.v4:id/fpv_error_pop_item_title_tv']";
						String expect_text = "自动起飞操作失败";
						
						BrickBean brick = new BrickBean();
						brick.setProperty("val");
						brick.setValidation_name(1);
						Map<String, String> params = new HashMap<>();
						params.put("ele_path", ele_path);
						params.put("expect_text", expect_text);
						brick.setParams(params);
						caseList.add(brick);
						break;
					case "Image Validation":
						val = "IV";
						break;
					case "Element Exist Validation":
						val = "EEV";
						break;
					}
				}
			}
		});
		
		labelActPick.setFont(ConstantsUI.SEC_TITLE);
		labelActName.setFont(ConstantsUI.FONT_NORMAL);
		comboxActName.setFont(ConstantsUI.FONT_NORMAL);
		labelVerPick.setFont(ConstantsUI.SEC_TITLE);
		labelVerName.setFont(ConstantsUI.FONT_NORMAL);
		comboxVerName.setFont(ConstantsUI.FONT_NORMAL);
		
		labelActPick.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		labelActNull.setPreferredSize(ConstantsUI.LABLE_SIZE_CASECRE_NULL_ITEM);
		labelActName.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		comboxActName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		labelVerPick.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		labelVerNull.setPreferredSize(ConstantsUI.LABLE_SIZE_CASECRE_NULL_ITEM);
		labelVerName.setPreferredSize(ConstantsUI.LABLE_SIZE_ITEM);
		comboxVerName.setPreferredSize(ConstantsUI.TEXT_COMBOX_SIZE_ITEM);
		labelNull_2.setPreferredSize(ConstantsUI.LABLE_SIZE_CASECRE_NULL_SEC_ITEM);
		
		panelGridVerPick.add(labelActPick);
		panelGridVerPick.add(buttonActAdd);
		panelGridVerPick.add(labelActNull);
		panelGridVerPick.add(labelActName);
		panelGridVerPick.add(comboxActName);
		panelGridVerPick.add(labelVerPick);
		panelGridVerPick.add(buttonVerAdd);
		panelGridVerPick.add(labelVerNull);
		panelGridVerPick.add(labelVerName);
		panelGridVerPick.add(comboxVerName);
		panelGridVerPick.add(labelNull_2);
		panelGridVerPick.add(buttonSave);
		
		panelCenter.add(panelGridElePick);
		panelCenter.add(panelGridActPick);
		panelCenter.add(panelGridVerPick);
		
		return panelCenter;
	}
	
	/**
	 * 
	 * @return
	 */
	private JPanel panelDown ;
	private JPanel getDownPanel() {
		if(panelDown == null){
			panelDown = new JPanel();
			/*JLabel labelTitle = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.title"));
			labelTitle.setFont(ConstantsUI.FONT_TITLE);
			labelTitle.setForeground(ConstantsUI.TOOL_BAR_BACK_COLOR);
			
			panelDown.add(labelTitle);*/
		}
		
		Dimension preferredSize = new Dimension(245, 350);
		panelDown.setPreferredSize(preferredSize);
		panelDown.setBackground(Color.DARK_GRAY);
		panelDown.setLayout(new FlowLayout(FlowLayout.LEFT));
		
	/*	JButton btn = new JButton();
		btn.setPreferredSize(new Dimension(50, 20));
		btn.setBorder(null);
		btn.setLocation(20, 20);
		btn.addMouseMotionListener(new BricksDrag());*/

		
		//panelDown.add(btn);
		panelDown.updateUI();
		return panelDown;
	}

	private JButton butele;
	private JButton butact;
	private JButton butver;
	private int butclick = 1;
	//private int id;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
    }
    public void setType(int id) {
        this.type = type;
    }
	public void addListener() {
		  buttonEleAdd.addActionListener(new ActionListener() {
			  @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                	
	                	id = butclick ++;
	                	butele = new JButton();
	                	butele.setBackground(Color.DARK_GRAY);
	                	butele.setPreferredSize(new Dimension(50, 20));
	                	butele.setBorder(null);
	                	butele.setText(cus_name);
	                	setId(id);
	                	butele.addMouseListener(new PopClickListener(butele));
	                	panelDown.add(butele);
	                	panelDown.updateUI();
	                	
	                	BrickBean brick = new BrickBean();
	                	brick.setEle_xpath(xpath);
	                	brick.setCustom_name(cus_name);
	                	brick.setProperty("ele");
	                	
	                	caseList.add(brick);
	                } catch (Exception e1) {
	                	e1.printStackTrace();
	                }

	            }
	        });
		  	buttonActAdd.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {

	                try {
	                	id = butclick ++;
	                	butact = new JButton();
	                	butact.setBackground(Color.DARK_GRAY);
	                	//btn2.setForeground(Color.blue);
	                	butact.setPreferredSize(new Dimension(50, 20));
	                	butact.setBorder(null);
	                	if(action == 1){
	                		butact.setText("CK");
	                	}else if(action == 2){
	                		butact.setText("LP");
	                	}else if(action == 3){
	                		butact.setText("ST");
	                	}else if(action == 10){
	                		butact.setText("DB");
	                	}
	                	setId(id);
	                	butact.addMouseListener(new PopClickListener(butact));
	                	panelDown.add(butact);
	                	panelDown.updateUI();
	                	
	                	BrickBean brick = new BrickBean();
	                	brick.setAction_name(action);
	                	brick.setProperty("act");
	                	
	                	caseList.add(brick);
	                } catch (Exception e1) {
	                	e1.printStackTrace();
	                }

	            }
	        });
		  	buttonVerAdd.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {

	                try {
	                	id = butclick ++;
	                	butver = new JButton();
	                	butver.setBackground(Color.DARK_GRAY);
	                	//btn2.setForeground(Color.blue);
	                	butver.setPreferredSize(new Dimension(50, 20));
	                	butver.setBorder(null);
	                	if(val.equals("TV")){
	                		butver.setText("TV");
	                	}else if(val.equals("IV")){
	                		butver.setText("IV");
	                	}else if(val.equals("EEV")){
	                		butver.setText("EEV");
	                	}
	                	setId(id);
	                	butver.addMouseListener(new PopClickListener(butver));
	                	panelDown.add(butver);
	                	panelDown.updateUI();
	                } catch (Exception e1) {
	                	e1.printStackTrace();
	                }

	            }
	        });
		  	buttonSave.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {

	                try {
	                	JDialog savewindow = new JDialog();
	                	
	                	File json = new File("json/file1.json");
	                	if (!json.getParentFile().exists())
	                		json.getParentFile().mkdirs();
	                	
	                	String str = JSON.toJSONString(caseList);
	                	PrintWriter pw = new PrintWriter(new FileWriter(json));
	                    pw.print(str);
	                    pw.flush();
	                    pw.close();
	                } catch (Exception e1) {
	                	e1.printStackTrace();
	                }

	            }
	        });
	  }

	  
	  class RightMenu extends JPopupMenu {
		    JMenuItem add_bricks;
		    JMenuItem delete_bricks;
		    JMenuItem modify_bricks;
		    JButton jbtn;
		    public RightMenu(JButton jbtn){
		    	this.jbtn = jbtn;
		    	add_bricks = new JMenuItem("Add");
		    	delete_bricks = new JMenuItem("Delete");
		    	modify_bricks = new JMenuItem("Modify");
		        add(add_bricks);
		        add(delete_bricks);
		        add(modify_bricks);
		        
			    add_bricks.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent e) {

		                try {
		                	panelDown.add(jbtn);
		                	panelDown.updateUI();
		                	System.out.println("add event");
		                } catch (Exception e1) {
		                    	                }

		            }
		        });
			    delete_bricks.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent e) {
		            
		            		try {
		            			panelDown.remove(jbtn);
		            			panelDown.updateUI();
		            			System.out.println("delete event");
		            		} catch (Exception e1) {
		            			e1.printStackTrace();
		            		}

		            }
		            
		        });
			    modify_bricks.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent e) {

		                try {
		                	System.out.println("modify event");
		                } catch (Exception e1) {
		                	e1.printStackTrace();
		                }
		            }
		        });
		    }

		}
	  
	  
	  class PopClickListener extends MouseAdapter {
		    public void mousePressed(MouseEvent e){
		        if (e.isPopupTrigger())
		            doPop(e);
		    }
		    private JButton jbtn;
		    public PopClickListener(JButton jbtn){
		    	super();
		    	this.jbtn =jbtn;
		    }
		    public void mouseReleased(MouseEvent e){
		        if (e.isPopupTrigger())
		            doPop(e);
		    }

		    private void doPop(MouseEvent e){
		    	RightMenu menu = new RightMenu(jbtn);
		        menu.show(e.getComponent(), e.getX(), e.getY());
		    }
		}
	  
	class ViewListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String viewName = (String) e.getItem();
				ResultSet rs = sql.queryElement("ELEMENT", appName, viewName);
				
				comboxEleName.removeAllItems();
				comboxEleName.removeItemListener(elisten);
				try {
					rs.next();
					String eleFirst = rs.getString(1);
					comboxEleName.addItem(eleFirst);
					while ((rs.next())) {
						String eleName = rs.getString(1);
						comboxEleName.addItem(eleName);
					}
					comboxEleName.setSelectedItem(null);
					comboxEleName.addItemListener(elisten);
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					if (rs != null) {
						try {
							rs.close();
							rs = null;
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		
	}
	
	class EleListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String ele_cus = (String) e.getItem();
				xpathSet = sql.queryElement("ELEMENT", ele_cus);
			}
			
			try {
				while (xpathSet.next()) {
					xpath = xpathSet.getString("XPATH");
					cus_name = xpathSet.getString("CUSTOM_NAME");
//					String state = xpathSet.getString(5);
//					for (int i = 0; i < state.length(); i++) {
//						if (state.charAt(i) == '1') {
//					        switch (i) {
//					        case 0:
//					        	comboxActName.addItem("click");
//					        	break;
//					        case 1:
//					        	comboxActName.addItem("scroll");
//					        	break;
//					        case 2:
//					        	comboxActName.addItem("check");
//					        	break;
//					        case 3:
//					        	comboxActName.addItem("focus");
//					        	break;
//					        case 4:
//					        	comboxActName.addItem("long-click");
//					        	break;
//					        }
//						}
//				    }
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				
			}
		}
		
	}
	
	class ActListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String action_name = (String) e.getItem();
				switch (action_name) {
				case "click":
					action = 1;
					break;
				case "longPress":
					action = 2;
					break;
				case "setText":
					action = 3;
					break;
				case "dragBar":
					action = 10;
					break;
				}
			}
		}
	}
}
