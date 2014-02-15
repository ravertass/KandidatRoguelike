package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Entities.Entity;
import se.chalmers.roguelike.World.World;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Direction.Dir;

public class MoveSystem implements ISystem {
	
	ArrayList<Entity> entities;
	World world;
	Input i;
	
	public MoveSystem(World world) {
		entities = new ArrayList<Entity>();
		this.world = world;
	}

	@Override
	public void update() {
		for (Entity e : entities) {
			i = e.getComponent(Input.class);
			if(i.getNextKey() != -1) {
				int key = i.getNextKey();
				if(key == Keyboard.KEY_W || key == Keyboard.KEY_NUMPAD8) {
					moveEntity(e, 0, 1, Dir.NORTH);
				}
				else if(key == Keyboard.KEY_A || key == Keyboard.KEY_NUMPAD4) {
					moveEntity(e, -1, 0, Dir.WEST);
				}
				else if(key == Keyboard.KEY_S || key == Keyboard.KEY_NUMPAD2) {
					moveEntity(e, 0, -1, Dir.SOUTH);
				}
				else if(key == Keyboard.KEY_D || key == Keyboard.KEY_NUMPAD6) {
					moveEntity(e, 1, 0, Dir.EAST);
				}
				else if(key == Keyboard.KEY_Q || key == Keyboard.KEY_NUMPAD7) {
					moveEntity(e, -1, +1, Dir.NORTHWEST);
				}
				else if(key == Keyboard.KEY_E || key == Keyboard.KEY_NUMPAD9) {
					moveEntity(e, 1, 1, Dir.NORTHEAST);
				}
				else if(key == Keyboard.KEY_C || key == Keyboard.KEY_NUMPAD3) {
					moveEntity(e, 1, -1, Dir.SOUTHEAST);
				}
				else if(key == Keyboard.KEY_Z || key == Keyboard.KEY_NUMPAD1) {
					moveEntity(e, -1, -1, Dir.SOUTHWEST);
				}
				i.resetKey();
			}
		}
	}
	
	public void moveEntity(Entity e, int x, int y, Dir direction){
		Position pos = e.getComponent(Position.class);
		Direction dir = e.getComponent(Direction.class);
		if(world.isWalkable(pos.getX()+x,pos.getY()+y)){
			pos.set(pos.getX()+x, pos.getY()+y);
		}
		dir.setDirection(direction);
	}
	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
		
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
}
