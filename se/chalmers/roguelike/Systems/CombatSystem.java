package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Dice;
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
				TargetingSystem targetingSystem = e.getComponent(Weapon.class)
						.getTargetingSystem();
				int range = e.getComponent(Weapon.class).getRange();
				if (targetingSystem == TargetingSystem.LINE) {
					int i = 0;
					for (Position pos : line) {
						if (i >= range)
							break;
						Tile tile = dungeon.getTile(pos.getX(), pos.getY());
						Entity target = tile.containsCharacter();
						// if there is a valid target, attack then break the
						// loop
						if (target != null) {
							Position targetpos = target
									.getComponent(Position.class);
							attack(targetpos, e);
						}
						// if there is a wall, break
						if (!tile.isWalkable() && tile.blocksLineOfSight())
							break;
						i++;
					}
				} else if (targetingSystem == TargetingSystem.CIRCLE) {
					Position targetPosition = getFirstViableTarget(line, range);
					
				} else if (targetingSystem == TargetingSystem.CONE) {

				} else if (targetingSystem == TargetingSystem.NOVA) {

				} else if (targetingSystem == TargetingSystem.SINGLE_TARGET) {
					attack(getFirstViableTarget(line, range), e);
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
	public void attack(Position targetpos, Entity attacker) {
		attack(targetpos.getX(), targetpos.getY(), attacker);
	}

	public void attack(int x, int y, Entity attacker) {
		Weapon weapon = attacker.getComponent(Weapon.class);

		// Make a list of targets depending on the type of the weapon
		ArrayList<Entity> targets = new ArrayList<Entity>();
		targets.add(dungeon.getTile(x, y).containsCharacter());

		int damage = -1;
		for (Entity target : targets) {
			if (target != null) {
				int attackroll = Dice.roll(2, 6);
				if (attackroll >= 7) {
					damage = weapon.getDamage();
					if (damage >= 0) {
						System.out.println("Damage: " + damage);
						target.getComponent(Health.class)
								.decreaseHealth(damage);
					}
					System.out.println(attacker + " attacks " + target
							+ " for " + damage + " damage.");
				} else {
					System.out.println(attacker + " missed.");
				}
			}
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

	public Position getFirstViableTarget(ArrayList<Position> line, int range) {
		// Checks for the first character in this line by checking
		// each position in the line in order
		Position targetpos = null;
		int i = 0;
		for (Position pos : line) {
			if (i >= range)
				break;
			Tile tile = dungeon.getTile(pos.getX(), pos.getY());
			Entity target = tile.containsCharacter();
			// if there is a valid target, attack then break the
			// loop
			if (target != null) {
				targetpos = target.getComponent(Position.class);
				break;
			}
			// if there is a wall, break
			if (!tile.isWalkable() && tile.blocksLineOfSight())
				break;
			i++;
		}
		return targetpos;
	}

}
