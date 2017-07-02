package UI.panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import UI.AppMainWindow;
import UI.ConstantsUI;
import UI.MyIconButton;
import tools.BricksDrag;
import tools.ConstantsUtils;
import tools.PropertyUtil;

/**
 *
 * @author DraLastat
 */
public class CasecrePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static MyIconButton buttonEleAdd;
	private static MyIconButton buttonActAdd;
	private static MyIconButton buttonVerAdd;
	private int id;
	private int type;
	
	/**
	 * 
	 */
	public CasecrePanel() {
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
	JPanel CaseCre;
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
		JComboBox<String> comboxAppName = new JComboBox<String>();
		comboxAppName.addItem("DJI GO3");
		comboxAppName.addItem("DJI GO4");
		comboxAppName.setEditable(false);
		JComboBox<String> comboxViewName = new JComboBox<String>();
		comboxViewName.addItem("View 1");
		comboxViewName.addItem("View 2");
		comboxViewName.setEditable(false);
		JComboBox<String> comboxEleName = new JComboBox<String>();
		comboxEleName.addItem("Ele 1");
		comboxEleName.addItem("Ele 2");
		comboxEleName.setEditable(false);

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
		JComboBox<String> comboxActName = new JComboBox<String>();
		comboxActName.addItem("ACT1");
		comboxActName.addItem("ACT2");
		comboxActName.setEditable(false);
		
		JLabel labelVerPick = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.verpick"));
        buttonVerAdd = new MyIconButton(ConstantsUI.ICON_ELE_ADD, ConstantsUI.ICON_ELE_ADD_ENABLE,
                ConstantsUI.ICON_ELE_ADD_DISABLE, "");
		JLabel labelVerNull = new JLabel();
		JLabel labelVerName = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.vername"));
		JComboBox<String> comboxVerName = new JComboBox<String>();
		comboxVerName.addItem("VER1");
		comboxVerName.addItem("VER2");
		comboxVerName.setEditable(false);
		
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
			
		
		panelCenter.add(panelGridElePick);
		panelCenter.add(panelGridActPick);
		panelCenter.add(panelGridVerPick);
		
		return panelCenter;
		
		

	}
	
	/**
	 * 
	 * @return
	 */
	JPanel panelDown ;
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

	JButton butele;
	JButton butact;
	JButton butver;
	int butclick = 1;
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
	                	setId(id);
	                	butele.addMouseListener(new PopClickListener(butele));
	                	panelDown.add(butele);
	                	panelDown.updateUI();
	                	System.out.println("ele" + id);
	                } catch (Exception e1) {
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
	                	setId(id);
	                	butact.addMouseListener(new PopClickListener(butact));
	                	panelDown.add(butact);
	                	panelDown.updateUI();
	                	System.out.println("act" + id);
	                } catch (Exception e1) {
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
	                	//btn3.setForeground(Color.pink);
	                	butver.setPreferredSize(new Dimension(50, 20));
	                	butver.setBorder(null);
	                	setId(id);
	                	butver.addMouseListener(new PopClickListener(butver));
	            		panelDown.add(butver);
	                	panelDown.updateUI();
	                	System.out.println("ver" + id);
	                } catch (Exception e1) {
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
		                    	                }

		            }
		            
		        });
			    modify_bricks.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent e) {

		                try {
		                	System.out.println("modify event");
		                } catch (Exception e1) {
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
	  
	
}
