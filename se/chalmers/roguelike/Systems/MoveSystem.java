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
						e.getComponent(Direction.class).setDirection(Dir.NORTH);
						break;
					case(Keyboard.KEY_NUMPAD8):
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()+1);
						e.getComponent(Direction.class).setDirection(Dir.NORTH);
						break;
					case(Keyboard.KEY_A):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()-1);
						e.getComponent(Direction.class).setDirection(Dir.WEST);
						break;
					case(Keyboard.KEY_NUMPAD4):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()-1);
						e.getComponent(Direction.class).setDirection(Dir.WEST);
						break;
					case(Keyboard.KEY_S):
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()-1);
						e.getComponent(Direction.class).setDirection(Dir.SOUTH);
						break;
					case(Keyboard.KEY_NUMPAD2):
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()-1);
						e.getComponent(Direction.class).setDirection(Dir.SOUTH);
						break;
					case(Keyboard.KEY_D):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()+1);
						e.getComponent(Direction.class).setDirection(Dir.EAST);
						break;
					case(Keyboard.KEY_NUMPAD6):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()+1);
						e.getComponent(Direction.class).setDirection(Dir.EAST);
						break;
					case(Keyboard.KEY_NUMPAD7):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()-1);
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()+1);
						e.getComponent(Direction.class).setDirection(Dir.NORTHWEST);
						break;
					case(Keyboard.KEY_NUMPAD9):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()+1);
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()+1);
						e.getComponent(Direction.class).setDirection(Dir.NORTHEAST);
						break;
					case(Keyboard.KEY_NUMPAD3):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()+1);
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()-1);
						e.getComponent(Direction.class).setDirection(Dir.SOUTHEAST);
						break;
					case(Keyboard.KEY_NUMPAD1):
						e.getComponent(Position.class).setX(e.getComponent(Position.class).getX()-1);
						e.getComponent(Position.class).setY(e.getComponent(Position.class).getY()-1);
						e.getComponent(Direction.class).setDirection(Dir.SOUTHWEST);
						break;
				}
				i.resetKey();
				System.out.println(e.getComponent(Direction.class).getDir());
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
