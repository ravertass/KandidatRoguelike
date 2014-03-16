package se.chalmers.plotgen.PlotData;

import java.util.HashSet;

/**
 * A place in a plot.
 * 
 * A scene may hold multiple actors and multiple props.
 * 
 * Generally, you add scenes to actors and props, not vice versa.
 * actor.setLocation(scene)
 * prop.setLocation(scene)
 * 
 * @author fabian
 */
public class Scene extends PlotThing {

	private HashSet<Actor> actors;
	private HashSet<Prop> props;
	
	public Scene(String name) {
		super(name);
		actors = new HashSet<Actor>();
		props = new HashSet<Prop>();
	}

	public HashSet<Actor> getActors() {
		return actors;
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}
	
	public void removeActor(Actor actor) {
		actors.remove(actor);
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
