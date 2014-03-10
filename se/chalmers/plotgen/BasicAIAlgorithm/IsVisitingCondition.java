package se.chalmers.plotgen.BasicAIAlgorithm;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Scene;

public class IsVisitingCondition implements ICondition {

	private Actor actor;
	private Scene scene;

	public IsVisitingCondition(Actor actor, Scene scene) {
		this.actor = actor;
		this.scene = scene;
	}

	@Override
	public boolean get() {
		return (actor.getLocation() == scene);
	}

	@Override
	public void set(boolean bool) {
		if (bool) {
			actor.setLocation(scene);
		} else {
			actor.setLocation(null); // TODO: Ska det göras såhär? Kanske
										// istället, gör ingenting? Då det
										// antagligen sköts ändå
		}
	}

}
