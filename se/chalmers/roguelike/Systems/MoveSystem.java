package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Entities.Entity;
import se.chalmers.roguelike.Components.Position;

public class MoveSystem implements ISystem {
	
	ArrayList<Entity> entities;
	
	public MoveSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			Input i = e.getComponent(Input.class);
			if(i.getNextKey() != -1) {
				switch(i.getNextKey()) {
					case(Keyboard.KEY_W): 
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()+1);
					case(Keyboard.KEY_A):
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getX()-1);
					case(Keyboard.KEY_S):
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()-1);
					case(Keyboard.KEY_D):
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getX()+1);
				}
			}
		}
	}

	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
		
	}

}
