package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Direction.Dir;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.util.Observer;


/**
 * MoveSystem handles moving of entities based on their input
 */
public class MoveSystem implements ISystem, Observer {
	
	ArrayList<Entity> entities;
	Dungeon world;
	Input i;
	
	private InputAction playerMove;


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
			if(i.getNextEvent() != InputAction.DUMMY && e.getComponent(TurnsLeft.class).getTurnsLeft() > 0) {
				InputAction event = i.getNextEvent();
				switch (event) {
					case GO_NORTH:
						moveEntity(e, 0, 1, Direction.Dir.NORTH);
						break;
					case GO_WEST:
						moveEntity(e, -1, 0, Direction.Dir.WEST);
						break;
					case GO_EAST:
						moveEntity(e, 1, 0, Direction.Dir.EAST);
						break;
					case GO_SOUTH:
						moveEntity(e, 0, -1, Direction.Dir.SOUTH);
						break;
					case GO_NORTHWEST:
						moveEntity(e, -1, 1, Direction.Dir.NORTHWEST);
						break;
					case GO_NORTHEAST:
						moveEntity(e, 1, 1, Direction.Dir.NORTHEAST);
						break;
					case GO_SOUTHWEST:
						moveEntity(e, -1, -1, Direction.Dir.SOUTHWEST);
						break;
					case GO_SOUTHEAST:
						moveEntity(e, 1, -1, Direction.Dir.SOUTHEAST);
						break;
//					case DO_NOTHING:
//						e.getComponent(TurnsLeft.class).decreaseTurnsLeft();
//						break;
					
				}
				i.resetEvent();
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

	@Override
	public void notify(Enum<?> i) {
		playerMove = (InputAction) i;
	}
}
