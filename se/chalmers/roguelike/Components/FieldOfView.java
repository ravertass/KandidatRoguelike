package se.chalmers.roguelike.Components;
/**
 * A class representing how far an entity can see.
 * @author twister
 *
 */
public class FieldOfView implements IComponent {
	
	private int viewDistance; //in tiles how far an entity can see
	
	public FieldOfView(int viewDistance) {
		this.viewDistance = viewDistance;
	}
	
	public int getViewDistance() {
		return this.viewDistance;
	}
	
	public void setViewDistance(int distance) {
		viewDistance = distance;
	}
	
	public IComponent clone() {
		return new FieldOfView(viewDistance);
	}
}