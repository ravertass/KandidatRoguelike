package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.util.CombatLog;

/**
 * A system which controlls the levels of all effected entities and levels them
 * up if they got more than the xp required.
 * 
 * @author twister
 * 
 */
public class LevelingSystem implements ISystem {

	private ArrayList<Entity> entities;

	private double xpmodifier;
	private int baseXP; // xp required to get to second level

	public LevelingSystem() {
		entities = new ArrayList<Entity>();
		xpmodifier = 1.2;
		baseXP = 100;
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			Attribute a = e.getComponent(Attribute.class);
			int maxXpForCurrentLevel = 0;
			for(int i = 1; i <= a.getLevel(); i++) {
				maxXpForCurrentLevel += baseXP*(Math.pow(1.2, a.getLevel()));
			}
			if (a.experience() >= maxXpForCurrentLevel){
				a.setLevel(a.getLevel() + 1);
				CombatLog.getInstance().addToLog("LEVEL UP! You feel slightly stronger...");
			}
		}

	}

	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	@Override
	public void removeEntity(Entity entity) {
		entities.remove(entity);

	}

}
