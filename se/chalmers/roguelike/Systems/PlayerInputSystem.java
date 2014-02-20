package se.chalmers.roguelike.Systems;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.util.Observer;
/**
 * A system for directing the right input towards the player. Not really a system since the update-method isn't used.
 * @author twister
 *
 */
public class PlayerInputSystem implements ISystem, Observer {
	
	Camera camera;
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
	/**
	 * This system subscribes to get input from the inputmanager, this method will be called	
	 * when inpuc happens. 
	 */
	@Override
	public void notify(Enum<?> i) {
		player.getComponent(Input.class).setNextEvent((InputAction)i);
		if(i.equals(InputAction.MOUSECLICK) && Mouse.isButtonDown(1) && Mouse.getEventButton() == 0) {
			player.getComponent(Input.class).setAttackCords(new Position((Mouse.getX()/16)+camera.getPosition().getX(),(Mouse.getY()/16)+camera.getPosition().getY()));
//			System.out.println(player.getComponent(Input.class).getAttackCords());
		}
		
	}
	
	public void setCamera(Camera c) {
		this.camera = c;
	}
}

