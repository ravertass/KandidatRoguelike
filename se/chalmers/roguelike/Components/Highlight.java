package se.chalmers.roguelike.Components;
/**
 * Placeholder so far - only used as a flag.
 * @author twister
 *
 */
public class Highlight implements IComponent {
	/**
	 * Does nothing at the moment but may be further implemented in the future
	 */
	public Highlight() {
		;
	}
	
	public IComponent clone() {
		return new Highlight(); //TODO don't know if this i right
	}


}
