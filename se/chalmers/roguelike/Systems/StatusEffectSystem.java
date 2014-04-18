package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.HashMap;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.StatusEffects;
import se.chalmers.roguelike.util.Observer;

public class StatusEffectSystem implements ISystem, Observer {
	
	public ArrayList<Entity> entities;
	
	public boolean newTurn = false;
	
	public static enum StatusEffect {
		POISONED, SLOWED, BURNING, PARALYZED
	}
	
	public StatusEffectSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		if(newTurn) {
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
	
	private void decreaseTurnsLeft() {
		for(Entity e : entities) {
			StatusEffects ses = e.getComponent(StatusEffects.class);
			HashMap<StatusEffect, Integer> effects = ses.getEffects();
			for(StatusEffect se : effects.keySet()) {
				if(effects.get(se) == 1) {
					effects.remove(se);
				} else {
					effects.put(se, effects.get(se) - 1);
				}
			}
		}
	}

}
