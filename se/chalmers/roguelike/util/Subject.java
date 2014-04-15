package se.chalmers.roguelike.util;
/**
 * An interface for classes which observers are listening to.
 * @author twister
 *
 */
public interface Subject {
	
	public void addObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObservers(final Enum<?> i);

}
