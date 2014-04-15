package se.chalmers.roguelike.util;
/**
 * An interface for systems that are observing something.
 * @author twister
 *
 */
public interface Observer {
	
	public void notify(final Enum<?> i);

}
