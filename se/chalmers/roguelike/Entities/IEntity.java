package se.chalmers.roguelike.Entities;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.IComponent;

/**
 * Basic Entity interface to get started.
 */
public interface IEntity {

	public void add(IComponent component);
	
	public void remove(IComponent component);
	
	public ArrayList<IComponent> get();
	
}
