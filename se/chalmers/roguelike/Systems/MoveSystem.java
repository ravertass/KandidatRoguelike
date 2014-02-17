package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Player;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Direction.Dir;

/**
 * MoveSystem handles moving of entities based on their input
 */
public class MoveSystem implements ISystem {
	
	ArrayList<Entity> entities;
	Dungeon world;
	Input i;
	
	/**
	 * Creates a new move system 
	 * 
	 * @param world the dungeon that the move system currently works on
	 */
	public MoveSystem(Dungeon world) {
		entities = new ArrayList<Entity>();
		this.world = world;
	}

	/**
	 * Moves entities if they have any turns left and if they have any input
	 * to ove them
	 */
	public void update() {
		for (Entity e : entities) {
			i = e.getComponent(Input.class);
			if(i.getNextKey() != -1 && e.getComponent(TurnsLeft.class).getTurnsLeft() > 0) {
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
				else if (key == Keyboard.KEY_NUMPAD5) {
					e.getComponent(TurnsLeft.class).decreaseTurnsLeft();
				}
				i.resetKey();
			}
		}
	}
	/**
	 * Checks if the entity can move to a new tile, if so moves it.
	 * 
	 * @param e entity to add
	 * @param x x coordinate of the tile the entity wants to move to
	 * @param y y coordinate of the tile the entity wants to move to
	 * @param direction the new direction the player should face towards
	 */
	private void moveEntity(Entity e, int x, int y, Dir direction){
		Position pos = e.getComponent(Position.class);
		Direction dir = e.getComponent(Direction.class);
		TurnsLeft turns = e.getComponent(TurnsLeft.class);
		if(world.isWalkable(pos.getX()+x,pos.getY()+y)){
			pos.set(pos.getX()+x, pos.getY()+y);
			turns.decreaseTurnsLeft();
		}
		dir.setDirection(direction);
	}
	
	/**
	 * Adds an entity to the system
	 * 
	 * @param entity entity to add to the system
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
		
	}
	
	/**
	 * Removes an entity from the system
	 * 
	 * @param entity entity to remove
	 */
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
}
