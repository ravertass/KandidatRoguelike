package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.Util;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.util.Observer;

/**
 * A system used for highlighting certain tiles in the Dungeon.
 */
public class HighlightSystem implements ISystem, Observer {

	ArrayList<Entity> entities;

	Camera camera;
	
	Position clickPos;
	
	final Position noClick = new Position(-1,-1);
	
	int buttonClicked;
	
	public HighlightSystem() {
		entities = new ArrayList<Entity>();
		clickPos = noClick;
	}
	/**
	 * Will calculate on which tile to draw the highlight-sprite and then set its visibility to true.
	 */
	@Override
	public void update() {
		for (Entity e : entities) {
			if (Mouse.isButtonDown(1) && buttonClicked == 0) {
//				System.out.println("FIRE!!");
				resetMouse();
			} else if (buttonClicked == 0) {
				e.getComponent(Position.class).set(
						(clickPos.getX() / 16)
								+ camera.getPosition().getX(),
						(clickPos.getY() / 16)
								+ camera.getPosition().getY());
				e.getComponent(Sprite.class).setVisibility(true);
				if(Engine.debug){
					System.out.println("Mouse click at X: "+e.getComponent(Position.class).getX()+ " Y: "+e.getComponent(Position.class).getY());
				}
				resetMouse();

			} else if (Mouse.isButtonDown(1)) {
				
				//Gets all the positions between the player (currently just 10,10) and the Mouse
				ArrayList<Position> line = Util.calculateLine(new Position(10, 10),
						new Position((Mouse.getX() / 16)
								+ camera.getPosition().getX(),
								(Mouse.getY() / 16)
										+ camera.getPosition().getY()));

				// System.out.println("Line: " + line);

				for (Position pos : line) {
					// Insert code for highlighting all the tiles
				}

				e.getComponent(Position.class).set(
						(Mouse.getX() / 16) + camera.getPosition().getX(),
						(Mouse.getY() / 16) + camera.getPosition().getY());
				e.getComponent(Sprite.class).setVisibility(true);

			} else if (!Mouse.isButtonDown(1)) {
			}
		}

	}

	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	@Override
	public void removeEntity(Entity entity) {
		entities.remove(entity);

	}

	public void setCamera(Camera c) {
		this.camera = c;
	}

	@Override
	public void notify(Enum<?> i) {
		if(i.equals(InputManager.InputAction.MOUSECLICK)) {
			clickPos = new Position(Mouse.getX(),Mouse.getY());
			buttonClicked = Mouse.getEventButton();
		}
		
	}
	
	public void resetMouse() {
		this.buttonClicked = -1;
		this.clickPos = noClick;
	}

}
