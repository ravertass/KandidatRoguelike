package se.chalmers.plotgen.PlotData;

import java.util.HashSet;

/**
 * A living person in a plot.
 * 
 * An actor belongs at a scene. An actor may hold multiple props.
 * 
 * Generally, you don't add an actor to a scene, you add a scene to an actor.
 * You also add an actor to a prop, not vice versa.
 * 
 * @author fabian
 */
public class Actor extends PlotThing {

	private Scene location;
	private HashSet<Prop> props;

	public Actor(String name) {
		super(name);
		location = null;
		props = new HashSet<Prop>();
	}

	public Scene getLocation() {
		return location;
	}

	public void setLocation(Scene location) {
		// Keep the constraint that an actor can only belong to one
		// scene at a time
		if (location != null) {
			location.removeActor(this);
		}
		
		this.location = location;
		location.addActor(this);
	}

	public void removeFromLocation() {		
		location.removeActor(this);
		location = null;
	}

	public HashSet<Prop> getProps() {
		return props;
	}

	public void addProp(Prop prop) {
		props.add(prop);
	}

	public void removeProp(Prop prop) {
		props.remove(prop);
	}

}
