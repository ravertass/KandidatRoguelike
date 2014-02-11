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
	
	/**
	 * an entity with a Health component loses dmg amount of HP and dies if 
	 * the health becomes 0 or less
	 *
	 * @param e			the effected entity 
	 * @param dmg		amount of health regenerated
	 */
	public void takeDamage(Entity e, int dmg){
		Health health = e.getComponent(Health.class);
		health.setHealth(health.getHealth()-dmg);
		if (health.getHealth() <= 0){
			die(e);
		}
	}
	
	/**
	 * an entity with a Health component heals for regen amount of HP or get 
	 * fullHP if the current health + regen is greater than the components fullHP 
	 * 
	 * @param e			the effected entity 
	 * @param regen		amount of health regenerated
	 */
	public void regenerate(Entity e, int regen){
		Health health = e.getComponent(Health.class);
		int currentHealth = health.getHealth();
		if (currentHealth+regen > health.getFullHP()){
			health.setHealth(health.getFullHP());
		} else {
			health.setHealth(currentHealth+regen);
		}
	}
	
	/**
	 * removes the entity from the CombatSystem since it does not have enough
	 * health to continue
	 * 
	 * @param e			the entity that dies
	 */
	private void die(Entity e) {
		removeEntity(e);
		// TODO ligga kvar som död?
	}

}
