package se.chalmers.plotgen.PlotData;

/**
 * A physical, non-living object in a plot.
 * 
 * A prop can belong to one actor XOR one scene.
 * 
 * Generally, you don't add props to actors or scenes; you add actors or scenes
 * to a prop.
 * prop.setOwner()
 * prop.setLocation
 */
public class Prop extends PlotThing implements IPlotBody {

	private Scene location;
	private Actor owner;
	
	private Scene snapLocation;
	private Actor snapOwner;

	public Prop(String name) {
		super(name);
		location = null;
		owner = null;
		
		snapLocation = null;
		snapOwner = null;
	}
	
	public void saveSnapShot() {
		snapLocation = location;
		snapOwner = owner;
	}
	
	public void loadSnapShot() {
		location = snapLocation;
		owner = snapOwner;
	}

	/**
	 * 
	 * @return the scene where the prop is at. Will be null if the prop is owned
	 *         by an actor.
	 */
	public Scene getLocation() {
		return location;
	}

	public void setLocation(Scene location) {
		keepConstraints();

		this.location = location;
		location.addProp(this);
	}

	/**
	 * 
	 * @return the actor whom the prop belongs to. Will be null if the prop is
	 *         at a location.
	 */
	public Actor getOwner() {
		return owner;
	}

	public void setOwner(Actor owner) {
		keepConstraints();

		this.owner = owner;
		owner.addProp(this);
	}

	private void keepConstraints() {
		// This does so the prop can't belong to multiple
		// actors and/or scenes at once
		if (owner != null) {
			removeFromActor();
		}
		if (location != null) {
			removeFromScene();
		}
	}

	public void removeFromActor() {
		owner.removeProp(this);
		owner = null;
	}

	public void removeFromScene() {
		location.removeProp(this);
		location = null;
	}

}
