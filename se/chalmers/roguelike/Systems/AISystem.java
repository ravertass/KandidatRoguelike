package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entities.Entity;
import se.chalmers.roguelike.Components.AI;

public class AISystem implements ISystem {

	/**
	 * A list of entities that has some kind of AI
	 */
	private ArrayList<Entity> entities;
	AI ai;

	@Override
	public void update(){
		for (Entity e : entities){
			//move, attack or sleep?
			ai = e.getComponent(AI.class);
			Entity target = ai.getTarget();
			if (target != null){
				track(target);
			}
		}
		
	}

	private void track(Entity target) {
		// TODO Auto-generated method stub
		// implement some algorithm that tries to get to the position of 'target'
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
