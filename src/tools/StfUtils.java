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
	
	private ArrayList<JSONObject> devices_pre = null;
	
	public ArrayList<JSONObject> getPresentDevices(String target_url, String token) {
		if (target_url.equals("") || token.equals("")) {
			System.out.println("info empty");
		}
		
		try {
			URL url = new URL(target_url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
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
