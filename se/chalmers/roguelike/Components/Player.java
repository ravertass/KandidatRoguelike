package se.chalmers.roguelike.Components;

/**
 * A flag for player
 */
public class Player implements IComponent {
	public IComponent clone() {
		return new Player();
	}
}