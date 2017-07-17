package com.dji.bricks;
import java.awt.image.BufferedImage;

import com.dji.bricks.mini_decode.ScreenObserver;

/**
 * @author Dan
 * @Description listener subject
 */
public interface SubjectForListener {
	
	public void registerObserver(ScreenObserver o);

	public void removeObserver(ScreenObserver o);

	public void notifyObservers(Object obj);
}
