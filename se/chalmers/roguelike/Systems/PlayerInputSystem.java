package se.chalmers.roguelike.Systems;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.util.Observer;
/**
 * A system for directing the right input towards the player. Not really a system since the update-method isn't used.
 * @author twister
 *
 */
public class PlayerInputSystem implements ISystem, Observer {
	
	Entity player;

	@Override
	public void update() {
		// TODO This isn't used for the moment but will probably be used when we add more stuff to the input.
		
	}

	@Override
	public void addEntity(Entity entity) {
		player = entity; //wonder if this is the correct use of it
		
	}

	@Override
	public void removeEntity(Entity entity) {
		//the player should never be removed so this method is moot
		
	}

	@Override
	public void notify(Enum<?> i) {
		player.getComponent(Input.class).setNextEvent((InputAction)i);
	}
}

