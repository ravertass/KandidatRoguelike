package se.chalmers.plotgen.PlotData;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A living person in a plot.
 * 
 * An actor belongs at a scene. An actor may hold multiple props.
 * 
 * Generally, you don't add an actor to a scene, you add a scene to an actor.
 * actor.setLocation()
 * You also add an actor to a prop, not vice versa.
 * prop.setOwner()
 */
public class Actor extends PlotThing implements IPlotBody {

	private Scene location;
	private HashSet<Prop> props;
	private boolean alive;
	
	private Scene snapLocation;
	private HashSet<Prop> snapProps;
	private boolean snapAlive;
	
	private int type;

	public Actor(String name, int type) {
		super(name);
		this.type = type;
		location = null;
		props = new HashSet<Prop>();
		alive = true;
		
		snapLocation = null;
		snapProps = null;
		snapAlive = true;
	}
	
	public Actor(String name) {
		this(name, 0);
	}
	
	public int getType() {
		return type;
	}

	public void saveSnapShot() {
		snapLocation = location;
		snapProps = props;
		snapAlive = alive;
	}
	
	public void loadSnapShot() {
		location = snapLocation;
		props = snapProps;
		alive = snapAlive;
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

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
		if (alive == false) {
			removeFromLocation();
			ArrayList<Prop> propsToRemove = new ArrayList<Prop>();
			for (Prop prop : props) {
				propsToRemove.add(prop);
			}
			for (Prop prop : propsToRemove) {
				prop.removeFromActor();
			}
		}
	}

}
