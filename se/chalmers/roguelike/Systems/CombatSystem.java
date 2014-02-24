package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Util;

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

		// For each entity capable of attacking
		for (Entity e : entities) {

			Input input = e.getComponent(Input.class);
			Position attackCords = input.getAttackCords();
			// If the entity has an attackcordinate
			if (attackCords.getX() != -1) {
				// calculate the line between the attacker and the
				// attackcordinates
				ArrayList<Position> line = Util.calculateLine(
						e.getComponent(Position.class), attackCords);
				// remove the first position in the line, since it is the
				// character attacking
				line.remove(0);

				// Checks for the first character in this line by checking each
				// position in the line in order
				for (Position pos : line) {
					Tile tile = dungeon.getTile(pos.getX(), pos.getY());
					Entity target = tile.containsCharacter();
					// if there is a valid target, attack then break the loop
					if (target != null) {
						Position targetpos = target
								.getComponent(Position.class);
						attack(targetpos, e.getComponent(Weapon.class)
								.getDamage());
						break;
					}
					// if there is a wall, break
					if (!tile.isWalkable() && tile.blocksLineOfSight())
						break;
				}

				e.getComponent(TurnsLeft.class).decreaseTurnsLeft();
			}
			input.resetAttackCords();

			if (e.getComponent(Health.class).getHealth() <= 0) {
				todie.add(e);
			}
		}
		for (Entity e : todie) {
			dungeon.getTile(e.getComponent(Position.class).getX(),
					e.getComponent(Position.class).getY()).removeEntity(e);
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
	public void attack(Position pos, int damage) {
		attack(pos.getX(), pos.getY(), damage);
	}

	public void attack(int x, int y, int damage) {
		Entity target = dungeon.getTile(x, y).containsCharacter();
		if (target != null)
			if (damage > 0) {
				System.out.println("Damage: " + damage);
				target.getComponent(Health.class).decreaseHealth(damage);
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

}
