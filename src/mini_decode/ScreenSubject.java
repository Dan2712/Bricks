package mini_decode;
import java.awt.image.BufferedImage;

public interface ScreenSubject {
	
	public void registerObserver(AndroidScreenObserver o);

	public void removeObserver(AndroidScreenObserver o);

	public void notifyObservers(BufferedImage image);
}
