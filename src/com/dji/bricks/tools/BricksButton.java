package com.dji.bricks.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
/*
 * 砖块添加方法
 * 以后再做这个优化
 */

public class BricksButton {
	
	public void ButtonAdd(JButton addbtn, int type) {
		JButton bricksbtn = new JButton();
		bricksbtn.setBackground(Color.DARK_GRAY);
		bricksbtn.setPreferredSize(new Dimension(50, 20));
		bricksbtn.setBorder(null);
		bricksbtn.addMouseListener(new PopClickListener(bricksbtn));
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
	                } catch (Exception e1) {
	                    	                }

	            }
	        });
		    delete_bricks.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	            
	            		try {
	            		} catch (Exception e1) {
	                    	                }

	            }
	            
	        });
		    modify_bricks.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {

	                try {
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
