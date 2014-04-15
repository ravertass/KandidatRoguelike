package se.chalmers.plotgen.BasicAIAlgorithm;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;

/**
 * A condition that checks if a certain prop belongs to a certain actor.
 */
public class BelongsToCondition implements ICondition {

	private Prop prop;
	private Actor actor;

	public BelongsToCondition(Prop prop, Actor actor) {
		this.prop = prop;
		this.actor = actor;
	}

	@Override
	public boolean get() {
		return (prop.getOwner() == actor);
	}

	@Override
	public void set(boolean bool) {
		if (bool) {
			prop.setOwner(actor);
		} else {
			prop.setOwner(null); // TODO: Ska det göras såhär? Kanske
									// istället, gör ingenting? Då det
									// antagligen sköts ändå
		}
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof BelongsToCondition) {
			return (prop == (((BelongsToCondition) object).prop) && actor == (((BelongsToCondition) object).actor));
		}
		return false;
	}

	@Override
	public String toString() {
		return "BelongsToCondition " + actor + " " + prop + " " + get();
	}
}
