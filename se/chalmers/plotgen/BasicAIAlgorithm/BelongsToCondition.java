package se.chalmers.plotgen.BasicAIAlgorithm;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;

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

}
