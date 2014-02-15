package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entities.Entity;

public class CollisionDetectionSystem implements ISystem {

	private ArrayList<Entity> entities;
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addEntity(Entity e) {
		entities.add(e);
		
	}

	@Override
	public void removeEntity(Entity e) {
		entities.remove(e);
		
	}
	
	

}
