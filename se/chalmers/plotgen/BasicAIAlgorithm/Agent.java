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
}
