package node_selection;

import java.util.Map;
import java.util.Observable;

public class VariableChangeObserve extends Observable {
	private Map<String, String> node_info;
	
	public void setInfo(Map<String, String> node_info) {
		synchronized (this) {
			this.node_info = node_info;
		}
		setChanged();
		notifyObservers();
	}

	public synchronized Map getInfo() {
		return node_info;
	}
}
