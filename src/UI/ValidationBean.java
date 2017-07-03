package UI;

import java.util.HashMap;
import java.util.Map;

public class ValidationBean {
	
	private int validation_name;
	private Map<String, String> params = new HashMap<>();
	
	public Map<String, String> getParams() {
		return params;
	}
	public int getValidation_name() {
		return validation_name;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public void setValidation_name(int validation_name) {
		this.validation_name = validation_name;
	}
}
