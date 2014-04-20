package se.chalmers.roguelike.Components;

/**
 * Works as a flag to determine if a star is the first star that should be active.
 */

public class FirstStarFlag implements IComponent {

	public FirstStarFlag clone() {
		return new FirstStarFlag();
	}
}
