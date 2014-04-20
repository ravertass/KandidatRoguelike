package se.chalmers.plotgen.PlotData;

import java.util.HashSet;

/**
 * A place in a plot.
 * 
 * A scene may hold multiple actors and multiple props.
 * 
 * Generally, you add scenes to actors and props, not vice versa.
 * actor.setLocation(scene) prop.setLocation(scene)
 */
public class Scene extends PlotThing {

	private HashSet<Actor> actors;
	private HashSet<Prop> props;

	private HashSet<Actor> snapActors;
	private HashSet<Prop> snapProps;
	private int type;

	/**
	 * @param name The name of the scene
	 * @param type A number between 0 and 9 that determines what kind of scene
	 *            it is
	 */
	public Scene(String name, int type) {
		super(name);
		this.type = type;
		actors = new HashSet<Actor>();
		props = new HashSet<Prop>();
	}

	public Scene(String name) {
		this(name, 0);
	}
	
	public int getType() {
		return type;
	}

	public void saveSnapShot() {
		snapActors = actors;
		snapProps = props;
	}

	public void loadSnapShot() {
		actors = snapActors;
		props = snapProps;
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
