package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

public class Operator {

	// I have a hard time choosing between ArrayList or HashSet for these...
	
	// All the conditions that must be true before the operator is used
	ArrayList<ICondition> beTrue;
	// All the conditions that must be false before the operator is used
	ArrayList<ICondition> beFalse;
	// All the conditions that will be set true when the operator is used
	ArrayList<ICondition> setTrue;
	// All the conditions that will be set true when the operator is used
	ArrayList<ICondition> setFalse;
	
	public Operator(ArrayList<ICondition> beTrue, ArrayList<ICondition> beFalse,
			ArrayList<ICondition> setTrue, ArrayList<ICondition> setFalse) {
		this.beTrue = beTrue;
		this.beFalse = beFalse;
		this.setTrue = setTrue;
		this.setFalse = setFalse;
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
}
