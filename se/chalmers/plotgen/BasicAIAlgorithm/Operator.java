package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Action;

public class Operator {

	// I had a hard time choosing between ArrayList or HashSet for these...
	
	// All the conditions that must be true before the operator is used
	ArrayList<ICondition> beTrue;
	// All the conditions that must be false before the operator is used
	ArrayList<ICondition> beFalse;
	// All the conditions that will be set true when the operator is used
	ArrayList<ICondition> setTrue;
	// All the conditions that will be set true when the operator is used
	ArrayList<ICondition> setFalse;
	
	// I guess it seems less elegant, but the operators need to have an action
	// coupled with it
	Action action;
	
	public Operator(ArrayList<ICondition> beTrue, ArrayList<ICondition> beFalse,
			ArrayList<ICondition> setTrue, ArrayList<ICondition> setFalse, Action action) {
		this.beTrue = beTrue;
		this.beFalse = beFalse;
		this.setTrue = setTrue;
		this.setFalse = setFalse;
		this.action = action;
	}
	
	public ArrayList<ICondition> getBeTrue() {
		return beTrue;
	}
	
	public ArrayList<ICondition> getBeFalse() {
		return beFalse;
	}
	
	public ArrayList<ICondition> getSetTrue() {
		return setTrue;
	}
	
	public ArrayList<ICondition> getSetFalse() {
		return setFalse;
	}
	
	public Action getAction() {
		return action;
	}
}
