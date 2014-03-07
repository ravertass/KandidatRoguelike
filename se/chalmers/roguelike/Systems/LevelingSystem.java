package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Attribute;

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
	private int xpToSecondLevel; // xp required to get to second level

	public LevelingSystem() {
		entities = new ArrayList<Entity>();
		xpmodifier = 0.2;
		xpToSecondLevel = 100;
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			Attribute a = e.getComponent(Attribute.class);
			int maxXpForCurrentLevel = (int) Math.pow(xpmodifier, a.getLevel())
					* xpToSecondLevel;
			if (a.experience() > maxXpForCurrentLevel)
				a.setLevel(a.getLevel() + 1);
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
