package se.chalmers.roguelike.util;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Components.Position;
/**
 * A class representing the camera with its position and width and height.
 * @author twister
 *
 */
public class Camera {
	
	private Position pos;
	
	private final int CAMERA_WIDTH = (Engine.screenWidth-Engine.hudWidth)/Engine.spriteSize; // in tiles
	private final int CAMERA_HEIGHT = Engine.screenHeight/Engine.spriteSize; // in tiles
	
	public Camera() {
		pos = new Position(0,0);
	}
	/**
	 * Sets the position of the camera defined by the lowerleft corner. Tile cordinates.
	 * @param p
	 */
	public void setPosition(Position p) {
		this.pos = p;
	}
	/**
	 * 
	 * @return The position of the lowerleft corner. Tile cordinates.
	 */
	public Position getPosition() {
		return this.pos;
	}
	/**
	 * Returns the width of the camera in tiles.
	 * @return
	 */
	public int getWidth() {
		return this.CAMERA_WIDTH;
	}
	/**
	 * Returns the hight of the camera in tiles.
	 * @return
	 */
	public int getHeight() {
		return this.CAMERA_HEIGHT;
	}
	/**
	 * Returns true if the position is within the camera.
	 * @param p
	 * @return
	 */
	public boolean contains(Position p){
		return pos.getX() <= p.getX() && p.getX() <= pos.getX()+getWidth() &&
				pos.getY() <= p.getY() && p.getY() <= pos.getY()+getHeight();
	}
}
