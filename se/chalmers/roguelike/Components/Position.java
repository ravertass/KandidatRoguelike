package se.chalmers.roguelike.Components;

/**
 * 
 * The tile position of en entity in the World.
 *
 */
public class Position implements IComponent {
	private int x, y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void set(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return ("(X:" + x + ",Y:" + y + ")");
	}
}
