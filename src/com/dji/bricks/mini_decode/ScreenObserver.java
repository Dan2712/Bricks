package com.dji.bricks.mini_decode;
import java.awt.image.BufferedImage;

/**
 * @author Dan
 * @Description listener for refreshing screen
 */
public interface ScreenObserver {
	
	public void frameImageChange(BufferedImage image);
}
