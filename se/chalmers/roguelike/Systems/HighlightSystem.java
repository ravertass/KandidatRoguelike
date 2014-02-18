package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.util.Observer;

public class HighlightSystem implements ISystem, Observer {

	ArrayList<Entity> entities;

	Input i;

	Camera camera;
	
	Position clickPos;
	
	final Position noClick = new Position(-1,-1);
	
	int buttonClicked;

	public HighlightSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			i = e.getComponent(Input.class);
			if (Mouse.isButtonDown(1) && buttonClicked == 0) {
				System.out.println("FIRE!!");
				resetMouse();
			} else if (buttonClicked == 0) {
				e.getComponent(Position.class).set(
						(clickPos.getX() / 32)
								+ camera.getPosition().getX(),
						(clickPos.getY() / 32)
								+ camera.getPosition().getY());
				e.getComponent(Sprite.class).setVisibility(true);
				resetMouse();
			} else if (Mouse.isButtonDown(1)) {
				e.getComponent(Position.class).set(
						(Mouse.getX() / 32) + camera.getPosition().getX(),
						(Mouse.getY() / 32) + camera.getPosition().getY());
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
