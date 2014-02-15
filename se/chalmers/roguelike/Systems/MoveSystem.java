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
			Position pos = e.getComponent(Position.class);
			Direction dir = e.getComponent(Direction.class);
			if(i.getNextKey() != -1) {
				int key = i.getNextKey();
				if(key == Keyboard.KEY_W || key == Keyboard.KEY_NUMPAD8) {
					if(world.isWalkable(pos.getX(),pos.getY()+1)){
						pos.setY(pos.getY()+1);
						dir.setDirection(Dir.NORTH);
					}
				}
				
				else if(key == Keyboard.KEY_A || key == Keyboard.KEY_NUMPAD4) {
					if(world.isWalkable(pos.getX()-1,pos.getY())){
						pos.setX(pos.getX()-1);
						dir.setDirection(Dir.WEST);
					}
				}
				
				else if(key == Keyboard.KEY_S || key == Keyboard.KEY_NUMPAD2) {
					if(world.isWalkable(pos.getX(),pos.getY()-1)){
						pos.setY(pos.getY()-1);
						dir.setDirection(Dir.SOUTH);
					}
				}
				
				else if(key == Keyboard.KEY_D || key == Keyboard.KEY_NUMPAD6) {
					if(world.isWalkable(pos.getX()+1,pos.getY())){
						pos.setX(pos.getX()+1);
						dir.setDirection(Dir.EAST);
					}
				}
				
				else if(key == Keyboard.KEY_Q || key == Keyboard.KEY_NUMPAD7) {
					if(world.isWalkable(pos.getX()-1,pos.getY()+1)){
						pos.setX(pos.getX()-1);
						pos.setY(pos.getY()+1);
						dir.setDirection(Dir.NORTHWEST);
					}
				}
				else if(key == Keyboard.KEY_E || key == Keyboard.KEY_NUMPAD9) {
					if(world.isWalkable(pos.getX()+1,pos.getY()+1)){
						pos.setX(pos.getX()+1);
						pos.setY(pos.getY()+1);
						dir.setDirection(Dir.NORTHEAST);
					}
				}
				else if(key == Keyboard.KEY_C || key == Keyboard.KEY_NUMPAD3) {
					if(world.isWalkable(pos.getX()+1,pos.getY()-1)){
						pos.setX(pos.getX()+1);
						pos.setY(pos.getY()-1);
						dir.setDirection(Dir.SOUTHEAST);
					}
				}
				else if(key == Keyboard.KEY_Z || key == Keyboard.KEY_NUMPAD1) {
					if(world.isWalkable(pos.getX()-1,pos.getY()-1)){
						pos.setX(pos.getX()-1);
						pos.setY(pos.getY()-1);
						dir.setDirection(Dir.SOUTHWEST);
					}
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
