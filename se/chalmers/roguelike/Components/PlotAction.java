package se.chalmers.roguelike.Components;

import se.chalmers.plotgen.PlotData.Action;

public class PlotAction implements IComponent {

	Action action;
	String plotText;
	Boolean actionPerformed;  
	
	public PlotAction() {
		actionPerformed = false;
		action = null;
		plotText = null;
	}
	
	public Action getAction() {
		return action;
	}
	
	public String getPlotText() {
		return plotText;
	}
	
	public boolean getActionPerformed() {
		return actionPerformed;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public void setPlotText(String plotText) {
		this.plotText = plotText;
	}
	
	public void setActionPerformed(boolean actionPerformed) {
		this.actionPerformed = actionPerformed;
	}
}
