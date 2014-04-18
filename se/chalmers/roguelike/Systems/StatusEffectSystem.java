package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;

public class StatusEffectSystem implements ISystem {
	
	public ArrayList<Entity> entities;
	
	public static enum StatusEffect {
		POISONED, SLOWED, BURNING, PARALYZED
	}
	
	public StatusEffectSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}

}
