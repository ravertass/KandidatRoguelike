package se.chalmers.roguelike.Components;

import se.chalmers.plotgen.PlotData.Action;

public class PlotAction implements IComponent {

	private Action action;
	private String plotText;
	private Boolean actionPerformed;
	private Boolean mainCharacterIsSubject;

	public PlotAction() {
		mainCharacterIsSubject = false;
		actionPerformed = false;
		action = null;
		plotText = null;
	}

	public boolean isMainCharacterSubject() {
		return mainCharacterIsSubject;
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
	
	public void setMainCharacterIsSubject(boolean mainCharacterIsSubject) {
		this.mainCharacterIsSubject = mainCharacterIsSubject;
	}

	public IComponent clone() {
		PlotAction pa = new PlotAction();
		pa.setAction(action);
		pa.setActionPerformed(actionPerformed);
		pa.setPlotText(plotText);
		pa.setMainCharacterIsSubject(mainCharacterIsSubject);
		return pa;
	}
}