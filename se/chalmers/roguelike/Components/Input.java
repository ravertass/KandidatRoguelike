package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.InputManager.InputAction;

/**
 * A component for storing input for entities
 * 
 * @author twister
 */
public class Input implements IComponent {

	private InputAction nextEvent;
	private Position attackPos;
	private Position noAttack = new Position(-1, -1);

	public Input() {
		nextEvent = InputAction.DUMMY;
		attackPos = noAttack;
	}

	/**
	 * This will be set for the player by the playerInputSystem and AISystem for enemies
	 * 
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

	/**
	 * Sets the coords of an upcoming attack.
	 * 
	 * @param p
	 */
	public void setAttackCords(Position p) {
		this.attackPos = p;
	}

	public Position getAttackCords() {
		return attackPos;
	}

	/**
	 * Resets the attackcords (sets them to -1,-1)
	 */
	public void resetAttackCords() {
		this.attackPos = noAttack;
	}

	public IComponent clone() {
		Input i = new Input();
		i.setAttackCords(attackPos);
		return i;
	}
}