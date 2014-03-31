package se.chalmers.roguelike.Components;

/**
 * 
 * The tile position of en entity in the World.
 *
 */
public class Position implements IComponent, Comparable<Position>{
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Position o) {
		if (this.x == o.getX())
			return 0;
		else if (this.x > o.getX())
			return 1;
		else 
			return -1;
	}
}