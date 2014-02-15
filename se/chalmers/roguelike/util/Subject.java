package se.chalmers.roguelike.util;

public interface Subject {
	
	public void addObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObservers(final Enum<?> i);

}
