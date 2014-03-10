package se.chalmers.plotgen.BasicAIAlgorithm;

import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;

public class IsPlacedAtCondition implements ICondition {

	private Prop prop;
	private Scene scene;

	public IsPlacedAtCondition(Prop prop, Scene scene) {
		this.prop = prop;
		this.scene = scene;
	}

	@Override
	public boolean get() {
		return (prop.getLocation() == scene);
	}

	@Override
	public void set(boolean bool) {
		if (bool) {
			prop.setLocation(scene);
		} else {
			prop.setLocation(null); // TODO: Ska det göras såhär? Kanske
									// istället, gör ingenting? Då det
									// antagligen sköts ändå
		}
	}

}
