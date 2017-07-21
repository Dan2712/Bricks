package com.dji.bricks;
import java.awt.image.BufferedImage;

import com.android.ddmlib.IDevice;
import com.hp.hpl.sparta.Document.Observer;

/**
 * @author Dan
 * @Description listener for refreshing screen
 */
public interface GlobalObserver {
	
	public void frameImageChange(BufferedImage image);
	
	public void ADBChange(IDevice[] devices);
}
