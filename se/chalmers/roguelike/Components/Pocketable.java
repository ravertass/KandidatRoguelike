package se.chalmers.roguelike.Components;

/**
 * Just a flag for things you can have in your inventory.
 * 
 * @author twister
 */
public class Pocketable implements IComponent {

	public IComponent clone() {
		return new Pocketable();
	}
}