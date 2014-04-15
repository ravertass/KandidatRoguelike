package se.chalmers.roguelike;

import java.util.HashMap;

import se.chalmers.roguelike.Components.*;

/**
 * Entity class 
 */
public class Entity {

	HashMap<Class<?>, IComponent> components;
	private long componentKey;
	private String name;
	
	/**
	 * Sets up a new entity
	 * @param name name of the entity
	 */
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
	

	/**
	 * Add a new component to the entity
	 * @param component component that should be added to the entity
	 */
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
		} else if (compClass == Inventory.class) {
			componentKey |= Engine.CompInventory;
		} else if(compClass == Text.class){
			componentKey |= Engine.CompText;
		}
	}
	
	/**
	 * @return the component key of the entity
	 */
	public long getComponentKey(){
		return componentKey;
	}
	
	/**
	 * Removes a component from the entity
	 * @param component component to remove
	 */
	public void remove(IComponent component) {
		components.remove(component.getClass());
	}
	
	/**
	 * Returns a specific component from the entity
	 * @param type the component type that should be returned
	 * @return the component that was requested
	 */
	public <T extends IComponent> T getComponent(Class<T> type) {
		
		return type.cast(components.get(type));
	}
	
	/**
	 * Returns the name of the component
	 */
	public String toString(){
		return name;
	}
	
	/**
	 * Checks if the  entity contains one or more components based on a key
	 * @param component the key that the entity should contain
	 * @return true if it contains the component(s), otherwise false
	 */
	public boolean containsComponent(long component){
		return (componentKey & component) == component;
	}
	
	public Entity clone() {

		Entity newEntity = new Entity(name);
		for(IComponent comp : components.values()){
			newEntity.add(comp.clone());
		}
		return newEntity;
	}
}
