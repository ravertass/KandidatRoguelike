package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.World.Dungeon;

/**
 * This system will handle everything that comes with entities losing and
 * gaining health.
 * 
 */
public class CombatSystem implements ISystem {

	/**
	 * A list of entities that could enter combat
	 */
	private Dungeon dungeon;
	private ArrayList<Entity> entities;
	private Engine engine;
	private ArrayList<Entity> todie;
	
	public CombatSystem(Dungeon dungeon, Engine engine) {
		this.dungeon = dungeon;
		this.engine = engine;
		entities = new ArrayList<Entity>();
		todie = new ArrayList<Entity>();
	}

	@Override
	public void update() {

		for (Entity e : entities) {
			
			Input input = e.getComponent(Input.class);
			Position attackCords = input.getAttackCords();
			if (attackCords.getX() != -1) {
				attack(attackCords);
				e.getComponent(TurnsLeft.class).decreaseTurnsLeft();
			}
			input.resetAttackCords();
			
			if (e.getComponent(Health.class).getHealth() <= 0) {
				todie.add(e);
			}
		}
		for (Entity e : todie) {
			dungeon.getTile(e.getComponent(Position.class).getX(), e.getComponent(Position.class).getY()).removeEntity(e);
			engine.removeEntity(e);
		}
		todie.clear();
		
	}

	/**
	 * Add an entity to the CombatSystem
	 * 
	 * @param e
	 */
	public void addEntity(Entity e) {
		entities.add(e);
	}

	/**
	 * Removes an entity from the CombatSystem
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	/**
	 * attack method
	 */
	public void attack(Position pos) {
		attack(pos.getX(), pos.getY());
	}

	public void attack(int x, int y) {
		Entity target = dungeon.getTile(x, y).containsCharacter();
		if (target != null)
			target.getComponent(Health.class).decreaseHealth(1);
	}

	/**
	 * an entity with a Health component loses dmg amount of HP and dies if the
	 * health becomes 0 or less
	 * 
	 * @param e
	 *            the effected entity
	 * @param dmg
	 *            amount of health regenerated
	 */
	public void takeDamage(Entity e, int dmg) {
		Health health = e.getComponent(Health.class);
		health.setHealth(health.getHealth() - dmg);
		if (health.getHealth() == 0) {
			die(e);
		}
	}

	/**
	 * an entity with a Health component heals for regen amount of HP or get
	 * fullHP if the current health + regen is greater than the components
	 * fullHP
	 * 
	 * @param e
	 *            the effected entity
	 * @param regen
	 *            amount of health regenerated
	 */
	public void regenerate(Entity e, int regen) {
		Health health = e.getComponent(Health.class);
		int currentHealth = health.getHealth();
		if (currentHealth + regen > health.getMaxHealth()) {
			health.setHealth(health.getMaxHealth());
		} else {
			health.setHealth(currentHealth + regen);
		}
	}

	/**
	 * removes the entity from the CombatSystem since it does not have enough
	 * health to continue
	 * 
	 * @param e
	 *            the entity that dies
	 */
	private void die(Entity e) {
		removeEntity(e);
		// TODO ligga kvar som d�d?
	}

}
