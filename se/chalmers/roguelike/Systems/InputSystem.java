package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.*;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Components.Input;
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
	private Entity player;
	
	public InputSystem(){
		entities = new ArrayList<Entity>();
	}
		
	public void update() {
		if(player == null){
			return;
		}
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
					System.exit(0);
				player.getComponent(Input.class).setNextKey(Keyboard.getEventKey());
			}
		}
		while(Mouse.next()) {
			if(Mouse.getEventButtonState()) {
				player.getComponent(Input.class).setNextMouseClick(
						new Pair<Integer,Integer>(Mouse.getX(),Mouse.getY()));
			}
			
		}
	}
	/**
	 * Adds an entity who are effected by input from the user.
	 * @param e
	 */
	public void addEntity(Entity e) {
		if((e.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer){
			player = e;
		} else {
			entities.add(e);
		}
	}
	/**
	 * Removes and effected entity from the list of effected entities.
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	

}
