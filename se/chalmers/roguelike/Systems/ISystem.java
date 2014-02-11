package se.chalmers.roguelike.Systems;

import se.chalmers.roguelike.Entities.Entity;

public interface ISystem {
	
	public void update();
	
	public void addEntity(Entity entity);
}
