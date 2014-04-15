package se.chalmers.plotgen.PlotData;

/**
 * The superclass for subjects and objects in plot actions, that is, actors,
 * props and scenes.
 */
public class PlotThing {
	private String name;

	public PlotThing(String name) {
		this.name = name;
	}

	/**
	 * For testing purposes.
	 */
	@Override
	public String toString() {
		return name;
	}
}