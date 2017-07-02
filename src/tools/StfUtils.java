package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StfUtils {
	private final String URL = "http://10.81.3.34:7100/api/v1/devices";
	private final String TOKEN = "f372dbecfb434ce69c95d9394d110192d0e5d9d6a6d5430181e34aa669435900";
	
	private ArrayList<JSONObject> devices_pre = null;
	
	public ArrayList<JSONObject> getPresentDevices() {
		try {
			URL url = new URL(URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
			
			InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in   = 
                new BufferedReader (new InputStreamReader (content));
            String line;
            StringBuilder str = new StringBuilder();
            while ((line = in.readLine()) != null) 
            	str.append(line);
            
            JSONObject device_json = JSONObject.parseObject(str.toString());
            JSONArray info_json = JSONArray.parseArray((device_json.getString("devices")));
            for(int i=0; i<info_json.size(); i++) {
            	JSONObject device = info_json.getJSONObject(i);
            	if (device.getBooleanValue("present") == true)
            		devices_pre.add(device);
            }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return devices_pre;
	}
}
