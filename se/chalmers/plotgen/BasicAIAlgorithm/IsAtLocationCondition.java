package se.chalmers.plotgen.BasicAIAlgorithm;

import se.chalmers.plotgen.PlotData.IPlotBody;
import se.chalmers.plotgen.PlotData.Scene;

/**
 * A condition that checks if an actor is at a certain location.
 */
public class IsAtLocationCondition implements ICondition {

	private IPlotBody plotBody;
	private Scene scene;

	public IsAtLocationCondition(IPlotBody plotBody, Scene scene) {
		this.plotBody = plotBody;
		this.scene = scene;
	}

	@Override
	public boolean get() {
		return (plotBody.getLocation() == scene);
	}

	@Override
	public void set(boolean bool) {
		if (bool) {
			plotBody.setLocation(scene);
		} else {
			plotBody.setLocation(null); // TODO: Ska det göras såhär? Kanske
										// istället, gör ingenting? Då det
										// antagligen sköts ändå
		}
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof IsAtLocationCondition) {
			return (scene == (((IsAtLocationCondition) object).scene) && 
					plotBody == (((IsAtLocationCondition) object).plotBody));
		}
		return false;
	}

	@Override
	public String toString() {
		return "SamePlaceCondition " + plotBody + " " + scene + " " + get();
	}
}
