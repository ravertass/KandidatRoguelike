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
		Input in = player.getComponent(Input.class);
		switch((InputAction) i) {
		case GO_NORTH:
			in.setNextKey(Keyboard.KEY_W);
			break;
		case GO_WEST:
			in.setNextKey(Keyboard.KEY_A);
			break;
		case GO_EAST:
			in.setNextKey(Keyboard.KEY_D);
			break;
		case GO_SOUTH:
			in.setNextKey(Keyboard.KEY_S);
			break;
		case GO_NORTHWEST:
			in.setNextKey(Keyboard.KEY_Q);
			break;
		case GO_NORTHEAST:
			in.setNextKey(Keyboard.KEY_E);
			break;
		case GO_SOUTHWEST:
			in.setNextKey(Keyboard.KEY_Z);
			break;
		case GO_SOUTHEAST:
			in.setNextKey(Keyboard.KEY_C);
			break;
		default:
			break;
			
		}
	}
}

