package se.chalmers.roguelike;

import java.util.HashMap;

import se.chalmers.roguelike.Components.*;
import se.chalmers.roguelike.Components.Attribute;

/**
 * Entity class 
 */
public class Entity {

	HashMap<Class<?>, IComponent> components;
	private long componentKey;
	private String name;
	
	public Entity(String name){
		components = new HashMap<Class<?>, IComponent>();
		componentKey = 0;
		this.name = name;
	}
	
	public Entity(String name, long ck, HashMap<Class<?>, IComponent> comps) {
		this.name = name;
		this.componentKey = ck;
		this.components = comps;
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
		} else if(compClass == Seed.class){
			componentKey |= Engine.CompSeed;
		} else if(compClass == DungeonComponent.class){
			componentKey |= Engine.CompDungeon;
		} else if(compClass == SelectedFlag.class){
			componentKey |= Engine.CompSelectedFlag;
		} else if(compClass == Gold.class){
			componentKey |= Engine.CompGold;
		} else if(compClass == BlocksWalking.class){
			componentKey |= Engine.CompBlocksWalking;
		} else if(compClass == PlotAction.class){
			componentKey |= Engine.CompPlotAction;
		} else if(compClass == BlocksLineOfSight.class){
			componentKey |= Engine.CompBlocksLineOfSight;
		} else if(compClass == MobType.class){
			componentKey |= Engine.CompMobType;
		} else if(compClass == Stair.class){
			componentKey |= Engine.CompStair;
		} else if(compClass == FieldOfView.class){
			componentKey |= Engine.CompFieldOfView;
		} else if (compClass == Usable.class) {
			componentKey |= Engine.CompUsable;
		}
//		System.out.println("New compkey: "+componentKey); // debug
	}
	
	public long getComponentKey(){
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
	
	public boolean containsComponent(long component){
		return (componentKey & component) == component;
	}
	
	public Entity clone() {
		return new Entity(name, componentKey, components);
	}
}
