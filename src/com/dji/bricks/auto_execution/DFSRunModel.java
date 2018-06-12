package com.dji.bricks.auto_execution;

import java.util.ArrayList;
import java.util.Stack;

import com.android.ddmlib.IDevice;
import com.dji.bricks.backgrounder.ExecutionMain;
import com.dji.bricks.node_selection.tree.UiNode;

public class DFSRunModel {
	
	private Stack<UiNode> nodeStack;
	private ExecutionMain exec;
	private IDevice device;
	private AutoCaptureAllClickableNode autoCapture;
	private ArrayList<UiNode> nodeList;

	public DFSRunModel(IDevice device) {
		super();
		this.device = device;
		init();
	}
	
	private void init() {
		nodeStack = new Stack<UiNode>();
		autoCapture = new AutoCaptureAllClickableNode(device);
	}
	
	public void dfsRun() {
		exec = new ExecutionMain(1, device);
		nodeList = autoCapture.captureAll();
		System.out.println(nodeList.size());
		if (nodeList != null) {
			nodeStack.add(nodeList.get(0));
			exec.runTestCase(nodeList.get(0).getAttribute("xpath"));
			nodeList = autoCapture.captureAll();
			exec.runTestCase(nodeList.get(0).getAttribute("xpath"));
		}
	}
}
