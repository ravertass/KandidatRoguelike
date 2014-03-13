package se.chalmers.roguelike.util;

import org.newdawn.slick.geom.Circle;

import se.chalmers.roguelike.Components.Position;

public class Triangle {

	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int x3;
	private int y3;

	public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.x3 = x3;
		this.y3 = y3;
	}
	
	public boolean contains(int x, int y) {
		// TODO
		return false;
	}
	
	public Circle circumCircle(){
		// TODO
		return null;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public int getX3() {
		return x3;
	}

	public int getY3() {
		return y3;
	}
	
	public Position getPos1() {
		return new Position(x1, y1);
	}
	
	public Position getPos2() {
		return new Position(x2, y2);
	}
	
	public Position getPos3() {
		return new Position(x3, y3);
	}
	
	static public Triangle getSuperTriangle(int height, int width, int xPos, int yPos){
		int x1, y1, x2, y2, x3, y3;
		int bottomBase = (int) (height/Math.sin(60)*Math.sin(30));
		x1 = xPos - bottomBase;
		y1 = xPos;
		x2 = width/2;
		y2 = (int) (Math.sqrt(width^2 - (width/2)^2));
		x3 = xPos + width + bottomBase;
		y3 = xPos;
		return new Triangle(x1, y1, x2, y2, x3, y3);
	}
	
}
