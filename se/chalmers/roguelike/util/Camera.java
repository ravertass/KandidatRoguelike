package se.chalmers.roguelike.util;

import se.chalmers.roguelike.Components.Position;

public class Camera {
	
	Position pos;
	
	public Camera() {
		pos = new Position(0,0);
	}
	
	public void setPosition(Position p) {
		this.pos = p;
	}
	
	public Position getPosition() {
		return this.pos;
	}
}
