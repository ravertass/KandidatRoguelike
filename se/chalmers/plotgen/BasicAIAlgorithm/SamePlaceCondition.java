package se.chalmers.plotgen.BasicAIAlgorithm;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.IPlotBody;

public class SamePlaceCondition implements ICondition {

	private Actor actor;
	private IPlotBody plotBody;

	public SamePlaceCondition(Actor actor, IPlotBody plotBody) {
		this.actor = actor;
		this.plotBody = plotBody;
	}

	@Override
	public boolean get() {
		return (actor.getLocation() == plotBody.getLocation());
	}

	@Override
	public void set(boolean bool) {
		// This condition will probably usually be used as a preconditon,
		// rather than something that will be set. Anyway, I decided that
		// setting this to true or false will change the location of the
		// acting actor. If it's used, this will probably be changed anyway.
		if (bool) {
			actor.setLocation(plotBody.getLocation());
		} else {
			actor.setLocation(null);
		}
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof SamePlaceCondition) {
			return (actor == (((SamePlaceCondition) object).actor) & 
					plotBody == (((SamePlaceCondition) object).plotBody));
		}
		return false;
	}

	@Override
	public String toString() {
		return "SamePlaceCondition " + actor + " " + plotBody + " " + get();
	}
}
