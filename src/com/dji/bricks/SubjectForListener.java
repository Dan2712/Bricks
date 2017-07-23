package com.dji.bricks;

/**
 * @author Dan
 * @Description listener subject
 */
public interface SubjectForListener {
	
	public void registerObserver(GlobalObserver o);

	public void removeObserver(GlobalObserver o);

	public void notifyObservers(Object obj);
}
