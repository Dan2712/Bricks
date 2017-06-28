package tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author DraLastat
 *
 */
public class PropertyUtil {

    /**
     * ╗˝╚íproperty
     * @param key
     * @return
     */
    public static String getProperty(String key){
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(ConstantsTools.PATH_PROPERTY));
            pps.load(in);
            String value = pps.getProperty(key);
            return value;

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
