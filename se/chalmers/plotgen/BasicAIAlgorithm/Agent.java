package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Actor;

/**
 * This models a character for the BasicAIAlgorithm.
 * An Agent has different conditions that it wants to be true/false as goals.
 */
public class Agent {

	private Actor self;
	private ArrayList<ICondition> trueGoals;
	private ArrayList<ICondition> falseGoals;

	public Agent(Actor self) {
		this.self = self;
	}

	/**
	 * @return the Actor equivalent of the agent
	 */
	public Actor getSelf() {
		return self;
	}

	/**
	 * @return the goals that the Actor wants to be true
	 */
	public ArrayList<ICondition> getTrueGoals() {
		return trueGoals;
	}

	/**
	 * @param trueGoals the goals that the actor should want to be true
	 */
	public void setTrueGoals(ArrayList<ICondition> trueGoals) {
		this.trueGoals = trueGoals;
	}

	/**
	 * @return the goals that the Actor wants to be false
	 */
	public ArrayList<ICondition> getFalseGoals() {
		return falseGoals;
	}

	/**
	 * @param trueGoals the goals that the actor should want to be false
	 */
	public void setFalseGoals(ArrayList<ICondition> falseGoals) {
		this.falseGoals = falseGoals;
	}

	/**
	 * @return if the trueGoals are true and the falseGoals are false
	 */
	public boolean goalsMet() {
		for (ICondition cond : trueGoals) {
			if (!cond.get()) {
				return false;
			}
		}
		for (ICondition cond : falseGoals) {
			if (cond.get()) {
				return false;
			}
		}
		return true;
	}
}
