package se.chalmers.plotgen.BasicAIAlgorithm;

import se.chalmers.plotgen.PlotData.Actor;

public class LivesCondition implements ICondition {

	private Actor actor;
	
	public LivesCondition(Actor actor) {
		this.actor = actor;
	}
	
	@Override
	public boolean get() {
		return actor.isAlive();
	}

	@Override
	public void set(boolean bool) {
		actor.setAlive(bool);		
	}
}
