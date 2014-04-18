package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.HashMap;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.StatusEffects;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.util.Observer;

public class StatusEffectSystem implements ISystem, Observer {

	public ArrayList<Entity> entities;

	public boolean newTurn = false;

	public static enum StatusEffect {
		POISONED, BURNING, PARALYZED
	}

	public StatusEffectSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		if (newTurn) {
			takeEffects();
			decreaseTurnsLeft();
			newTurn = false;
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

	/**
	 * This will be called by turnsystem whenever there is a new turn.
	 */
	@Override
	public void notify(Enum<?> i) {
		newTurn = true;

	}

	private void takeEffects() {
		for (Entity e : entities) {
			HashMap<StatusEffect, Integer> effects = e.getComponent(StatusEffects.class).getEffects();
			for(StatusEffect se : effects.keySet()) {
				switch(se) {
					case BURNING: 
						e.getComponent(Health.class).decreaseHealth(5); //TODO magic number
						break;
					case POISONED:
						e.getComponent(Health.class).decreaseHealth(5); //TODO moar magic numberz
						break;
					case PARALYZED:
						e.getComponent(TurnsLeft.class).decreaseTurnsLeft();
						break;
												
				}
			}
		}
	}

	private void decreaseTurnsLeft() {
		for (Entity e : entities) {
			HashMap<StatusEffect, Integer> effects = e.getComponent(StatusEffects.class).getEffects();
			for (StatusEffect se : effects.keySet()) {
				if (effects.get(se) == 1) {
					effects.remove(se);
				} else {
					effects.put(se, effects.get(se) - 1);
				}
			}
		}
	}

}
