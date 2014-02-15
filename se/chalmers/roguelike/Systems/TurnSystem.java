package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.TurnsLeft;

public class TurnSystem implements ISystem {
	
	private ArrayList<Entity> entities;
	
	public TurnSystem(){
		entities = new ArrayList<Entity>();
	}
	
	@Override
	public void update() {
		for(Entity e : entities){
			if(e.getComponent(TurnsLeft.class).getTurnsLeft() != 0){
				return; // means we found an entity that hasn't used up his turns
			}
		}
		// Otherwise: Reset turns
		for(Entity e : entities){
			int turnsLeft = e.getComponent(TurnsLeft.class).getTurnsLeft();
			System.out.println("RESETTING SOME TURNS FROM "+turnsLeft);
			e.getComponent(TurnsLeft.class).setTurnsLeft(++turnsLeft);
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
