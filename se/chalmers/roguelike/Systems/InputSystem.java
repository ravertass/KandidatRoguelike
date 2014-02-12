package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.*;

import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Entities.Entity;
import se.chalmers.roguelike.util.Pair;
/**
 * A system handling all the input from the user and directing it to the entities who are effected by it. 
 *
 */
public class InputSystem implements ISystem {
	/**
	 * The effected entities.
	 */
	private ArrayList<Entity> entities;
	
	public InputSystem(){
		entities = new ArrayList<Entity>();
	}
		
	public void update() {
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				for (Entity e : entities) {
					e.getComponent(Input.class).setNextKey(Keyboard.getEventKey());
				}
			}
			if(Mouse.getEventButtonState()) {
				for (Entity e : entities) {
					e.getComponent(Input.class).setNextMouseClick(
							new Pair<Integer,Integer>(Mouse.getX(),Mouse.getY()));
				}
			}
		}
	}
	/**
	 * Adds an entity who are effected by input from the user.
	 * @param e
	 */
	public void addEntity(Entity e) {
		entities.add(e);
	}
	/**
	 * Removes and effected entity from the list of effected entities.
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	

}
