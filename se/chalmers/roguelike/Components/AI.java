package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.Entities.Entity;

public class AI implements IComponent{

	/**
	 * targets the player if it is close to the AI-entity
	 */
	private Entity target;
	
	
	public AI(){
		target = null;
	}
	
	public void setTarget(Entity e){
		target = e;
	}
	
	public Entity getTarget(){
		return target;
	}
	
}
