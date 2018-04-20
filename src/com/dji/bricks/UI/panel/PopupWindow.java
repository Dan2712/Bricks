package com.dji.bricks.UI.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.UI.MyIconButton;
import com.dji.bricks.tools.PopupUtils;
import com.dji.bricks.tools.PropertyUtil;

/**
*
* @author DraLastat
*/

public class PopupWindow extends Thread{
	 private Map<String,String> feaMap=null;
	 private static int type;
	 private static MyIconButton buttonClose;
	 
	 public PopupWindow(int type){
		    this.type = type;
		 	feaMap=new HashMap<String,String>();
		 	feaMap.put("name", "popup test");
		 	feaMap.put("release", "time check");
		 	feaMap.put("feature", "input test");
		 	super.start();
		 	addListener();
	 		}

	 private PopupUtils popwindow;
	 public void run(){
	        popwindow = new PopupUtils(450, 585);          
	        popwindow.setTitle("popuptest");
	        popwindow.setLayout(new GridLayout(3, 1));
	        //分成上中下三大快，最后根据type传入的值不同来给三大块内容做添加
	        JPanel UpPanel = new JPanel();
	        JPanel CenterPanel = new JPanel();
	        JPanel DownPanel = new JPanel();
	        JPanel UpleftPanel = new JPanel();
	        JPanel UprightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        JLabel text_ver_name = new JLabel(PropertyUtil.getProperty("bricks.ui.casecre.textvername"));
	        buttonClose = new MyIconButton(ConstantsUI.ICON_CLOSE_POPWINDOW, ConstantsUI.ICON_CLOSE_POPWINDOW,
	                ConstantsUI.ICON_CLOSE_POPWINDOW, "");
	        JTextArea text_ver_field = new JTextArea();
	        JScrollPane text_scroll = new JScrollPane(text_ver_field);
	        
	        UpPanel.setLayout(new GridLayout(1, 2));
	        
	        JPanel headPan=new JPanel();
	        JPanel feaPan=new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        JPanel btnPan=new JPanel();
	        JButton update=new JButton("OK");
	        	 
	        JLabel head=new JLabel(feaMap.get("name")+",type anything");
	        head.setPreferredSize(new Dimension(250,30));
	        head.setForeground(Color.black);
	        JTextArea feature=new JTextArea(feaMap.get("feature"));
	        feature.setEditable(false);
	        feature.setForeground(Color.red);
	   
	        feature.setPreferredSize(new Dimension(280,60));
	   
	        JScrollPane jfeaPan=new JScrollPane(feature);
	        jfeaPan.setPreferredSize(new Dimension(283,80));
	        jfeaPan.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	        
	        feaPan.add(jfeaPan);
	        headPan.add(head);
	        btnPan.add(update);
	        
	        UpPanel.add(UpleftPanel);
	        UpPanel.add(UprightPanel);
	        
	        if(type == 1){
	        	UpleftPanel.add(text_ver_name);
	        	UprightPanel.add(buttonClose);
//	        	popwindow.add(headPan,BorderLayout.NORTH);
//	        	popwindow.add(feaPan,BorderLayout.CENTER);
	        }else if(type == 2){
//	        	popwindow.add(btnPan,BorderLayout.SOUTH);
	        }
	        popwindow.add(UpPanel);
	        popwindow.add(CenterPanel);
	        popwindow.add(DownPanel);
	        
	        update.addMouseListener(new MouseAdapter(){
	        	public void mouseClicked(MouseEvent e){
	        		JOptionPane.showMessageDialog(popwindow, "???");
	   		}
	        });
	        
	        popwindow.setAlwaysOnTop(true);
	        popwindow.setUndecorated(true);
	        popwindow.setResizable(false);
	        popwindow.setVisible(true);  
	        popwindow.run();
	 		}
	 public void addListener() {
		  buttonClose.addActionListener(new ActionListener() {
			  @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
//	                	popwindow.close();
	                } catch (Exception e1) {
	                	e1.printStackTrace();
	                }

	            }
	        });
		  }
	 public static void main(String args[]){
		 	new PopupWindow(type);
	 		}
	}
