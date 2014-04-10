package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.Entity;

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
	public IComponent clone() { 
		AI newAI = new AI();
		newAI.target = this.target;
		return newAI;
	}
	
}