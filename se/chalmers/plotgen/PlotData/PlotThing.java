package se.chalmers.plotgen.PlotData;

/**
 * The superclass for subjects and objects in plot actions, that is, actors and
 * props (and in the future, maybe scenes).
 * 
 * @author fabian
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