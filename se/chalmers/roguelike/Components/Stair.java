package se.chalmers.roguelike.Components;

public class Stair implements IComponent {

	// Essentially just a copy of position.
	private int x, y;

	public Stair(int x, int y) {
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

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return ("(X:" + x + ",Y:" + y + ")");
	}

	public IComponent clone() {
		return new Stair(x, y);
	}
}