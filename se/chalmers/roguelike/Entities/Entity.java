package se.chalmers.roguelike.Entities;

import java.util.HashMap;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Components.*;
import se.chalmers.roguelike.Components.Character;

/**
 * Entity class 
 */
public class Entity {

	HashMap<Class<?>, IComponent> components;
	private int componentKey;
	
	public Entity(){
		components = new HashMap<Class<?>, IComponent>();
		componentKey = 0;
	}
	
	public void add(IComponent component) {
		Class<?> compClass = component.getClass();
		components.put(compClass, component);
		if(compClass == Character.class){
			componentKey |= Engine.CompCharacter;
		} else if(compClass == Health.class){
			componentKey |= Engine.CompHealth;
		} else if(compClass == Input.class){
			componentKey |= Engine.CompInput;
		} else if(compClass == Position.class){
			componentKey |= Engine.CompPosition;
		} else if(compClass == Sprite.class){
			componentKey |= Engine.CompSprite;
		} else if(compClass == TurnsLeft.class){
			componentKey |= Engine.CompTurnsLeft;
		} else if(compClass == Direction.class){
			componentKey |= Engine.CompDirection;
		} else if(compClass == AI.class){
			componentKey |= Engine.CompAI;
		}
		System.out.println("New compkey: "+componentKey); // debug
	}
	
	public int getComponentKey(){
		return componentKey;
	}
	
	public void remove(IComponent component) {
		components.remove(component.getClass());
	}
	
	public <T extends IComponent> T getComponent(Class<T> type) {
		
		return type.cast(components.get(type));
	}
	
}
