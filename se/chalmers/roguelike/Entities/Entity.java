package se.chalmers.roguelike.Entities;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.IComponent;

/**
 * Basic Entity interface to get started.
 */
public class Entity {

	ArrayList<IComponent> components;
	
	public Entity(){
		components = new ArrayList<>();
	}
	
	public void add(IComponent component) {
		components.add(component);
	}
	
	public void remove(IComponent component) {
		components.remove(component);
	}
	
	public ArrayList<IComponent> get() {
		return components;
	}
	
}
