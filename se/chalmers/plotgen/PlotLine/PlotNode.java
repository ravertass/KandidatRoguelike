package se.chalmers.plotgen.PlotLine;

import se.chalmers.plotgen.PlotData.Action;

public class PlotNode {

	private Action action;
	private String text;
	
	public PlotNode(Action action) {
		this.action = action;
	}
	
	public Action getAction() {
		return action;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
