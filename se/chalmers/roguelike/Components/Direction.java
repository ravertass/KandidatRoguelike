package se.chalmers.roguelike.Components;

/**
 * 
 * The direction an entity (usually a mob) is facing.
 *
 */
public class Direction implements IComponent {
	public enum Dir {
		NORTH, EAST, SOUTH, WEST, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST
	}
	
	private Dir dir;
	
	public Direction(Dir dir) {
		this.dir = dir;
	}
	
	// Makes SOUTH the standard starting direction
	public Direction() {
		this(Dir.SOUTH);
	}

	public void setDirection(Dir dir) {
		this.dir = dir;
	}
	
	public Dir getDir() {
		return dir;
	}
	
	public IComponent clone() {
		return new Direction(dir);
	}
}
