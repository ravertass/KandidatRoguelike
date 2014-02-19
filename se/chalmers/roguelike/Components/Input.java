package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.util.Pair;

/**
 * A component for storing input for entities
 * @author twister
 *
 */
public class Input implements IComponent {
	
	
	private InputAction nextEvent;
	
	public Input() {
		nextEvent = InputAction.DUMMY;
	}
	/**
	 * This will be set for the player by the playerinputsystem and AI for enemies.
	 * @param i
	 */
	public void setNextEvent(InputAction i) {
		this.nextEvent = i;
	}
	
	public InputAction getNextEvent() {
		return nextEvent;
	}
	/**
	 * Sets the next action to a dummyaction which will tell systems that there is no relevant input.
	 */
	public void resetEvent() {
		nextEvent = InputAction.DUMMY;
	}
	
}
