package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Entities.Entity;

public class HighlightSystem implements ISystem {
	

	ArrayList<Entity> entities;
	
	Input i;
	
	public HighlightSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		System.out.println(entities.size());
		for (Entity e : entities) {
			i = e.getComponent(Input.class);
			if(i.getNextMouseClick().getFirst() >= 0) {
				e.getComponent(Position.class).set(i.getNextMouseClick().getFirst()/32, i.getNextMouseClick().getSecond()/32);
				e.getComponent(Sprite.class).setVisibility(true);
				i.resetMouse();
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
