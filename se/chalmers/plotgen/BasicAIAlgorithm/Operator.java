package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Action;

/**
 * An Operator is the BasicAIAlgorithm counterpart of a plotgen Action.
 * These are actions that the agents perform in the BasicAIAlgorithm 
 * plot generating simulation.
 * 
 * An Operator has four lists of conditions:
 * 
 * beTrue
 * 	Conditions that should be true
 * beFalse
 * 	Conditions that should be false
 * setTrue
 * 	Conditions that will be set true
 * setFalse
 * 	Conditions that will be set false
 */
public class Operator {

	// I had a hard time choosing between ArrayList or HashSet for these...

	// All the conditions that must be true before the operator is used
	private ArrayList<ICondition> beTrue;
	// All the conditions that must be false before the operator is used
	private ArrayList<ICondition> beFalse;
	// All the conditions that will be set true when the operator is used
	private ArrayList<ICondition> setTrue;
	// All the conditions that will be set true when the operator is used
	private ArrayList<ICondition> setFalse;

	// I guess it seems less elegant, but the operators need to have an action
	// coupled with it
	private Action action;

	// How likely it is for the operator to be performed (higher number -> more
	// likely)
	private int weight;

	public Operator(ArrayList<ICondition> beTrue,
			ArrayList<ICondition> beFalse, ArrayList<ICondition> setTrue,
			ArrayList<ICondition> setFalse, Action action, int weight) {
		this.beTrue = beTrue;
		this.beFalse = beFalse;
		this.setTrue = setTrue;
		this.setFalse = setFalse;
		this.action = action;
		this.weight = weight;
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

	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
