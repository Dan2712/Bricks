package UI;

public class BrickBean {
	private String ele_xpath;
	private String custom_name;
	private int action_name;
	private boolean isEle;
	
	public int getAction_name() {
		return action_name;
	}
	public String getCustom_name() {
		return custom_name;
	}
	public String getEle_xpath() {
		return ele_xpath;
	}
	public boolean isEle() {
		return isEle;
	}
	public void setAction_name(int action_name) {
		this.action_name = action_name;
	}
	public void setCustom_name(String custom_name) {
		this.custom_name = custom_name;
	}
	public void setEle(boolean isEle) {
		this.isEle = isEle;
	}
	public void setEle_xpath(String ele_xpath) {
		this.ele_xpath = ele_xpath;
	}
}
