package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Actor;

public class Agent {

	private Actor self;
	private ArrayList<ICondition> trueGoals;
	private ArrayList<ICondition> falseGoals;
	
	public Agent(Actor self) {
		this.self = self;
	}
	
	public Actor getSelf() {
		return self;
	}

	public ArrayList<ICondition> getTrueGoals() {
		return trueGoals;
	}

	public void setTrueGoals(ArrayList<ICondition> trueGoals) {
		this.trueGoals = trueGoals;
	}

	public ArrayList<ICondition> getFalseGoals() {
		return falseGoals;
	}

	public void setFalseGoals(ArrayList<ICondition> falseGoals) {
		this.falseGoals = falseGoals;
	}
	
	public boolean goalsMet() {
		boolean bool = true;
		for (ICondition cond : trueGoals) {
			bool &= cond.get();
		}
		for (ICondition cond : falseGoals) {
			bool &= cond.get();
		}
		return bool;
	}
}
