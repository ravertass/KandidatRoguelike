package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entities.Entity;

public class CombatSystem implements ISystem {
	
	private ArrayList<Entity> entities;
	
	
	
	

	@Override
	public void update() {
		// TODO Auto-generated method stub	
	}

	/**
	 * Add an entity to the CombatSystem
	 * @param e
	 */
	public void addEntity(Entity e) {
		entities.add(e);		
	}
	
	/**
	 * Removes an entity from the CombatSystem
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}

}
