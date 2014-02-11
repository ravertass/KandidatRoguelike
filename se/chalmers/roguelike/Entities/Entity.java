package se.chalmers.roguelike.Entities;

import java.util.HashMap;
import se.chalmers.roguelike.Components.IComponent;

/**
 * Entity class 
 */
public class Entity {

	HashMap<Class<?>, IComponent> components;
	
	public Entity(){
		components = new HashMap<Class<?>, IComponent>();
	}
	
	public void add(IComponent component) {
		components.put(component.getClass(), component);
	}
	
	public void remove(IComponent component) {
		components.remove(component.getClass());
	}
	
	public <T extends IComponent> T getComponent(Class<T> type) {
		
		return type.cast(components.get(type));
	}
	
}
