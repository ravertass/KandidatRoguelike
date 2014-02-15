package se.chalmers.roguelike.Systems;

import se.chalmers.roguelike.Entity;

public interface ISystem {
	
	public void update();
	
	public void addEntity(Entity entity);
	
	public void removeEntity(Entity entity);
}
