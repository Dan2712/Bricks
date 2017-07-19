package com.dji.bricks;
import java.awt.image.BufferedImage;

/**
 * @author Dan
 * @Description listener subject
 */
public interface SubjectForListener {
	
	public void registerObserver(GlobalObserver o);

	public void removeObserver(GlobalObserver o);

	public void notifyObservers(Object obj);
}
