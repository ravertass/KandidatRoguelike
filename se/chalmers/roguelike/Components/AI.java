package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.Entities.Entity;

public class AI implements IComponent{

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
