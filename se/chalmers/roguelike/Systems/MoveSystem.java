package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Entities.Entity;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Direction.Dir;

public class MoveSystem implements ISystem {
	
	ArrayList<Entity> entities;
	Input i;
	
	public MoveSystem() {
		entities = new ArrayList<Entity>();
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			i = e.getComponent(Input.class);
			Position pos = e.getComponent(Position.class);
			Direction dir = e.getComponent(Direction.class);
			if(i.getNextKey() != -1) {
				switch(i.getNextKey()) {
					case(Keyboard.KEY_W): 
						pos.setY(pos.getY()+1);
						dir.setDirection(Dir.NORTH);
						break;
					case(Keyboard.KEY_NUMPAD8):
						pos.setY(pos.getY()+1);
						dir.setDirection(Dir.NORTH);
						break;
					case(Keyboard.KEY_A):
						pos.setX(pos.getX()-1);
						dir.setDirection(Dir.WEST);
						break;
					case(Keyboard.KEY_NUMPAD4):
						pos.setX(pos.getX()-1);
						dir.setDirection(Dir.WEST);
						break;
					case(Keyboard.KEY_S):
						pos.setY(pos.getY()-1);
						dir.setDirection(Dir.SOUTH);	
						break;
					case(Keyboard.KEY_NUMPAD2):
						pos.setY(pos.getY()-1);
						dir.setDirection(Dir.SOUTH);;
						break;
					case(Keyboard.KEY_D):
						pos.setX(pos.getX()+1);
						dir.setDirection(Dir.EAST);
						break;
					case(Keyboard.KEY_NUMPAD6):
						pos.setX(pos.getX()+1);
						dir.setDirection(Dir.EAST);
						break;
					case(Keyboard.KEY_NUMPAD7):
						pos.setX(pos.getX()-1);
						pos.setY(pos.getY()+1);
						dir.setDirection(Dir.NORTHWEST);
						break;
					case(Keyboard.KEY_NUMPAD9):
						pos.setX(pos.getX()+1);
						pos.setY(pos.getY()+1);
						dir.setDirection(Dir.NORTHEAST);
						break;
					case(Keyboard.KEY_NUMPAD3):
						pos.setX(pos.getX()+1);
						pos.setY(pos.getY()-1);
						dir.setDirection(Dir.SOUTHEAST);
						break;
					case(Keyboard.KEY_NUMPAD1):
						pos.setX(pos.getX()-1);
						pos.setY(pos.getY()-1);
						dir.setDirection(Dir.SOUTHWEST);
						break;
				}
				i.resetKey();
			}
		}
	}

	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
		
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
}
