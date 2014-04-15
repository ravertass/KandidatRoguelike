package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Direction.Dir;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.World.Dungeon;


/**
 * MoveSystem handles moving of entities based on their input
 */
public class MoveSystem implements ISystem{
	
	ArrayList<Entity> entities;
	Dungeon world;
	Input i;


	/**
	 * Creates a new move system 
	 * 
	 * @param world the dungeon that the move system currently works on
	 */
	public MoveSystem() {
		entities = new ArrayList<Entity>();
	}
	
	@Override
	public void update() {
		
	}
	
	/**
	 * Moves entities if they have any turns left and if they have any input
	 * to ove them
	 */
	public void update(Dungeon dungeon) {
		world = dungeon;
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
					case TURN_NORTH:
						e.getComponent(Direction.class).setDirection(Dir.NORTH);
						break;
					case TURN_SOUTH:
						e.getComponent(Direction.class).setDirection(Dir.SOUTH);
						break;
					case TURN_WEST:
						e.getComponent(Direction.class).setDirection(Dir.WEST);
						break;
					case TURN_EAST:
						e.getComponent(Direction.class).setDirection(Dir.EAST);
						break;
					case DO_NOTHING:
						e.getComponent(TurnsLeft.class).decreaseTurnsLeft();
						break;
					default:
						break;
					
				}
				i.resetEvent();
			}
		}
	}
	/**
	 * Checks if the entity can move to a new tile, if so moves it.
	 * 
	 * @param e entity to add
	 * @param dx x coordinate of the tile the entity wants to move to
	 * @param dy y coordinate of the tile the entity wants to move to
	 * @param direction the new direction the player should face towards
	 */
	private void moveEntity(Entity e, int dx, int dy, Dir direction){
		Position pos = e.getComponent(Position.class);
		Direction dir = e.getComponent(Direction.class);
		TurnsLeft turns = e.getComponent(TurnsLeft.class);
		int newx = pos.getX()+dx;
		int newy = pos.getY()+dy;
		int oldx = pos.getX();
		int oldy = pos.getY();
		if(world.isWalkable(newx, newy)){
			world.removeEntity(oldx, oldy, e);
			pos.set(newx,newy);
			world.addEntity(newx, newy, e);
		}
		turns.decreaseTurnsLeft();
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
