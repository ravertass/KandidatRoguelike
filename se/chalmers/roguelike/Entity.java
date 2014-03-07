package se.chalmers.roguelike;

import java.util.HashMap;

import se.chalmers.roguelike.Components.*;
import se.chalmers.roguelike.Components.Attribute;

/**
 * Entity class 
 */
public class Entity {

	HashMap<Class<?>, IComponent> components;
	private int componentKey;
	private String name;
	
	public Entity(String name){
		components = new HashMap<Class<?>, IComponent>();
		componentKey = 0;
		this.name = name;
	}
	
	public void add(IComponent component) {
		Class<?> compClass = component.getClass();
		components.put(compClass, component);
		
		// Determine the new component key for the entity.
		if(compClass == Attribute.class){
			componentKey |= Engine.CompAttribute;
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
		} else if(compClass == Highlight.class){
			componentKey |= Engine.CompHighlight;
		} else if(compClass == Player.class){
			componentKey |= Engine.CompPlayer;
		}
//		System.out.println("New compkey: "+componentKey); // debug
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
	
	public String toString(){
		return name;
	}
}
