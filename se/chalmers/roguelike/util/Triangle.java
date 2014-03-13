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

	public Triangle(float[] trianglePoint1, float[] trianglePoint2,
			float[] trianglePoint3) {
		this.x1 = (int) trianglePoint1[0];
		this.y1 = (int) trianglePoint1[1];
		this.x2 = (int) trianglePoint2[0];
		this.y2 = (int) trianglePoint2[1];
		this.x3 = (int) trianglePoint3[0];
		this.y3 = (int) trianglePoint3[1];
	}

	public boolean contains(int x, int y) {
		// TODO
		return false;
	}

	public Circle circumCircle() {
		int d = 2*(x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2));
		int ux = ((x1^2+y1^2)*(y2-y3) + (x2^2 + y2^2)*(y3-y1)+(x3^2 + y3^2)*(y1-y2))/d;
		int uy = ((x1^2+y1^2)*(x3-x2) + (x2^2 + y2^2)*(x1-x3)+(x3^2 + y3^2)*(x2-x1))/d;
		
		int r = (int) Math.sqrt((uy-y1)^2 +(x1-ux)^2);
		return new Circle(ux, uy, r);
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

	static public Triangle getSuperTriangle(int height, int width, int xPos,
			int yPos) {
		int x1, y1, x2, y2, x3, y3;
		int bottomBase = (int) (height / Math.sin(60) * Math.sin(30));
		x1 = xPos - bottomBase;
		y1 = xPos;
		x2 = width / 2;
		y2 = (int) (Math.sqrt(width ^ 2 - (width / 2) ^ 2));
		x3 = xPos + width + bottomBase;
		y3 = xPos;
		return new Triangle(x1, y1, x2, y2, x3, y3);
	}
	
	public String toString() {
		return("("+ x1 +","+ y1 +")("+ x2 +","+ y2 +")("+ x3 +","+ y3 +")");
	}

}
