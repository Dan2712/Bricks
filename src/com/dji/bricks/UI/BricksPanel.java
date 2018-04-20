package com.dji.bricks.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;  
  
/** 
 * 
 *  
 * @author DraLastat
 */  
public class BricksPanel implements Border {  
    private Color color;  
  
//    public BricksPanel(Color color) {
//        this.color = color;  
//    }  
  
    public BricksPanel() {
        this.color = Color.BLACK; // 默认是黑色边框  
    }  
  
    public Insets getBorderInsets(Component c) {  
        return new Insets(0, 0, 0, 0);  
    }  
  
    public boolean isBorderOpaque() {  
        return false;  
    }  
  
    @Override  
    public void paintBorder(Component c, Graphics g, int x, int y, int width,  
            int height) {
        g.setColor(color);
        g.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 15, 15);  

    }  
    
}  

