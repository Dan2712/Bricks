package mini_decode;
import java.awt.image.BufferedImage;

/**
 * @author Dan
 * @Description listener for refreshing screen
 */
public interface ScreenSubject {
	
	public void registerObserver(AndroidScreenObserver o);

	public void removeObserver(AndroidScreenObserver o);

	public void notifyObservers(BufferedImage image);
}
