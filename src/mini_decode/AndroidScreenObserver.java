package mini_decode;
import java.awt.image.BufferedImage;

/**
 * @author Dan
 * @Description listener for refreshing screen
 */
public interface AndroidScreenObserver {
	public void frameImageChange(BufferedImage image);
}
