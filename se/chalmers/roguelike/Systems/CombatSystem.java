package se.chalmers.roguelike.Systems;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Dice;
import se.chalmers.roguelike.util.TrueTypeFont;
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

	public CombatSystem(Engine engine) {
		this.engine = engine;
		entities = new ArrayList<Entity>();
		todie = new ArrayList<Entity>();
	}

	@Override
	public void update() {
	}

	public void update(Dungeon dungeon1) {
		dungeon = dungeon1;
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
						if (i >= range) {
							System.out.println("out of range, i=" + i);
							break;
						}
						Tile tile = dungeon.getTile(pos.getX(), pos.getY());
						// if (tile == null)
						// break;
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
					ArrayList<Position> possibleTargets = Util.circlePositions(
							targetPosition, e.getComponent(Weapon.class)
									.getAoESize());
					for (Position p : possibleTargets) { // TODO CHANGE
															// EVERYTHING
						attack(p, e);
					}
				} else if (targetingSystem == TargetingSystem.BOX) {
					ArrayList<Position> possibleTargets = new ArrayList<Position>();
					int aoeSize = e.getComponent(Weapon.class).getAoESize();
					// blow adds all positions around the center with a radius of the wepons getaoesize
					for (int x = attackCords.getX() - aoeSize; x <= attackCords
							.getX() + aoeSize; x++) {
						for (int y = attackCords.getY() - aoeSize; y <= attackCords
								.getY() + aoeSize; y++) {
							possibleTargets.add(new Position(x, y));
						}
					}
					for (Position p : possibleTargets) {
						if (p.getX() >= 0 && p.getY() >= 0
								&& p.getX() <= dungeon1.getWorldWidth()
								&& p.getY() <= dungeon1.getWorldHeight())
							attack(p, e);
					}

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
		// Kills all enemies with 0 hp.
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
		Attribute attackerStats = attacker.getComponent(Attribute.class);

		Attribute targetStats;
		// Make a list of targets depending on the type of the weapon
		ArrayList<Entity> targets = new ArrayList<Entity>();
		targets.add(dungeon.getTile(x, y).containsCharacter());

		int damage = -1;
		for (Entity target : targets) {
			targetStats = target.getComponent(Attribute.class);
			if (target != null) {
				int attackroll = Dice.roll(3, 10)
						+ attackerStats.getMod(attackerStats.perception());
				attackerStats = attacker.getComponent(Attribute.class);
				System.out.println("Attackroll: " + attackroll
						+ "| Defender Agility: " + attackerStats.agility());
				if (attackroll >= attackerStats.agility()) {
					damage = weapon.getDamage();
					if (damage >= 0) {
						target.getComponent(Health.class)
								.decreaseHealth(damage);
						if (target.getComponent(Health.class).getHealth() <= 0) {
							attackerStats.increaseExperience(targetStats.xpyield());
						}
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
