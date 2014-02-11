package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entities.Entity;

public class AISystem implements ISystem {

	/**
	 * A list of entities that has some kind of AI
	 */
	private ArrayList<Entity> entities;

	@Override
	public void update(){
		// TODO Auto-generated method stub	
	}

	/**
	 * Add an entity to the AISystem
	 * 
	 * @param e
	 */
	public void addEntity(Entity e){
		entities.add(e);		
	}
	
	/**
	 * Removes an entity from the AISystem
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
}
