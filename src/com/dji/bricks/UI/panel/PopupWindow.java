package com.dji.bricks.UI.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.dji.bricks.UI.ConstantsUI;
import com.dji.bricks.tools.PopupUtils;
import com.sun.jna.platform.unix.X11.Font;
/**
*
* @author DraLastat
*/
public class PopupWindow extends Thread{
	 private Map<String,String> feaMap=null;
	 
	 public PopupWindow(){
		 	feaMap=new HashMap<String,String>();
		 	feaMap.put("name", "popup test");
		 	feaMap.put("release", "time check");
		 	feaMap.put("feature", "input test");
		 	super.start();
	 		}
	 public void run(){
	        final PopupUtils tw=new PopupUtils(450, 585);          
	        tw.setTitle("popuptest");
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
	        tw.add(headPan,BorderLayout.NORTH);
	        tw.add(feaPan,BorderLayout.CENTER);
	        tw.add(btnPan,BorderLayout.SOUTH);
	   
	        update.addMouseListener(new MouseAdapter(){
	        	public void mouseClicked(MouseEvent e){
	        		JOptionPane.showMessageDialog(tw, "???");
	   		}
	        });
	        
	        tw.setAlwaysOnTop(true);
	        tw.setUndecorated(true);
	        tw.setResizable(false);
	        tw.setVisible(true);  
	        tw.run();  
	 		}
	 public static void main(String args[]){
		 	new PopupWindow();
	 		}
	}
