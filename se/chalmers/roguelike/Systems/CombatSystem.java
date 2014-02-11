package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Entities.Entity;

public class CombatSystem implements ISystem {
	
	/**
	 * A list of entities that could enter combat
	 */
	private ArrayList<Entity> entities;

	@Override
	public void update(){
		// TODO Auto-generated method stub	
	}

	/**
	 * Add an entity to the CombatSystem
	 * @param e
	 */
	public void addEntity(Entity e){
		entities.add(e);		
	}
	
	/**
	 * Removes an entity from the CombatSystem
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	/**
	 * attack method
	 */
	public void attack(){
		//TODO a method for attacking!
	}
	
	public void takeDamage(Entity e, int dmg){
		Health health = e.getComponent(Health.class);
		health.setHealth(health.getHealth()-dmg);
		if (health.getHealth() <= 0){
			die(e);
		}
	}

	public void regenerate(Entity e, int regen){
		Health health = e.getComponent(Health.class);
		health.setHealth(health.getHealth()-regen);
	}
	
	private void die(Entity e) {
		// TODO Auto-generated method stub
		// ligga kvar som död?
	}

}
