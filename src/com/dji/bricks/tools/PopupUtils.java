package com.dji.bricks.tools;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;

import com.dji.bricks.MainEntry;
/**
*
* @author DraLastat
*/
public class PopupUtils extends JDialog implements Runnable {  
	  
    private int x, y;  
    private int width,  height;  
    private int windowx, windowy;
  
    public PopupUtils(int width, int height){
        this.width = width;
        this.height = height;
        x = (int) (MainEntry.frame.getWidth());
        y = (int) (MainEntry.frame.getHeight());
        windowx = (int) MainEntry.frame.getLocationOnScreen().getX() + 1005;
        windowy = (int) MainEntry.frame.getLocationOnScreen().getY() + 72;
        initComponents();
    	} 
    
    public void run() {  
        for (int i = 0; i <= height; i += 30) {  
            try {  
                this.setLocation(windowx - i, windowy);
                Thread.sleep(5);  
            } catch (InterruptedException ex) {  
                Logger.getLogger(PopupUtils.class.getName()).log(Level.SEVERE, null, ex);  
            }  
        }
        try {
        	Thread.sleep(3000);
        } catch (Exception e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        	close();
    	}    
    private void initComponents() {  
        this.setSize(width, height);  
        this.setLocation(MainEntry.frame.getLocationOnScreen());  
        this.setLocationRelativeTo(MainEntry.frame);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                close();    
            }
        });        
    }   
    public void close(){
     for (int i = 0; i <= height; i += 300) {  
            try {
                setLocation(windowx -width + i, windowy);
                Thread.sleep(5);                                  
            } catch (Exception ex) {  
                Logger.getLogger(PopupUtils.class.getName()).log(Level.SEVERE, null, ex);  
            }  
        }
     	dispose();
    }

}

