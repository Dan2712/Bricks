package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;


public class FileOperation {
	
	public static LinkedHashMap<String, String> loadJson(String path) {
		
		BufferedReader reader;
		LinkedHashMap<String, String> jsonMap = null;
		
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = "";
			String str = "";
			while((line = reader.readLine()) != null) {
				str += line;
			}
			
			jsonMap = JSON.parseObject(str, 
					new TypeReference<LinkedHashMap<String, String>>() {});
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonMap;
	}
}
