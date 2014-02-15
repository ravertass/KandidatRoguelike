package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Player;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.util.Pair;

/**
 * A system handling all the input from the user and directing it to the
 * entities who are effected by it.
 * 
 */
public class InputSystem implements ISystem {
	/**
	 * The effected entities.
	 */
	private ArrayList<Entity> entities;

	public InputSystem() {
		entities = new ArrayList<Entity>();
	}

	public void update() {
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
					System.exit(0);
				for (Entity e : entities) {
					if (e.getComponent(Player.class) != null)
						e.getComponent(Input.class).setNextKey(Keyboard.getEventKey());
				}
			}
		}
		// System.out.println("DX: " + Mouse.getX());
		// System.out.println("DY: " + Mouse.getY());
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				System.out.println("MouseButton: " + Mouse.getEventButton());

				for (Entity e : entities) {
					e.getComponent(Input.class).setNextMouseClick(
							new Pair<Integer, Integer>(Mouse.getX(),
									Mouse.getY()), Mouse.getEventButton());
				}
			}

		}
	}

	/**
	 * Adds an entity who are effected by input from the user.
	 * 
	 * @param e
	 */
	public void addEntity(Entity e) {
			entities.add(e);
	}

	/**
	 * Removes and effected entity from the list of effected entities.
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e) {
		 entities.remove(e);
	}

}
