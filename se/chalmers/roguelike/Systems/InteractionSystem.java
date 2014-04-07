package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.Components.BlocksWalking;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Direction.Dir;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.Gold;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Stair;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.InputManager.InputAction;

public class InteractionSystem implements ISystem, Observer {

	private Engine engine;
	private Entity player;
	private Dungeon dungeon;
	
	/**
	 * Sets up the interactionsystem.
	 * 
	 * @param engine the engine instance the system uses.
	 */
	public InteractionSystem(Engine engine){
		this.engine = engine;
	}
	
	/**
	 * Runs with the game loop.
	 */
	public void update() {
		if(dungeon == null || player == null){
			return;
		}
		interact();
	}

	/**
	 * Adds a player to the system
	 * @param entity the player
	 */
	public void addEntity(Entity entity) {
		player = entity;
		
	}

	/**
	 * Removes the player from the system
	 */
	public void removeEntity(Entity entity) {
		player = null;
		
	}
	
	/**
	 * Picks up whatever is on the player tile.
	 */
	private void interact(){
		Position pos = player.getComponent(Position.class);
		Tile tile = dungeon.getTile(pos.getX(), pos.getY());
		ArrayList<Entity> entities = tile.getEntities();
		for(Entity e : entities){
			if(e.containsComponent(Engine.CompGold) && !e.containsComponent(Engine.CompPlayer)){
				Gold playerGold = player.getComponent(Gold.class);
				Gold entityGold = e.getComponent(Gold.class);
				playerGold.setGold(playerGold.getGold()+entityGold.getGold());
				engine.removeEntity(e);
			}
		}
	}
	
	/**
	 * Sets the dungeon that the system will be using
	 * @param dungeon the dungeon that should be used
	 */
	public void setDungeon(Dungeon dungeon){
		this.dungeon = dungeon;
		System.out.println("New dungeon set");
	}

	/**
	 * notify-function for the observer interface, will match and see if the 
	 * message from the observer is correct.
	 */
	public void notify(Enum<?> i) {
		if (Engine.gameState == Engine.GameState.DUNGEON && player != null
				&& i.equals(InputAction.INTERACTION)) {
			Position pos = player.getComponent(Position.class);
			Tile tile = dungeon.getTile(pos.getX(), pos.getY());
			ArrayList<Entity> entities = tile.getEntities();
			if(entities.size()-1 != 0){ // -1 because player will always be counted as one
				for(Entity e : entities){
					if(e.containsComponent(Engine.CompStair)) {
						DungeonComponent dc = e.getComponent(DungeonComponent.class);
						Dungeon nextDungeon = dc.getDungeon();
						Stair stair = e.getComponent(Stair.class);
						if(nextDungeon != null){
							engine.loadDungeon(nextDungeon, stair.getX(), stair.getY());
						} else {
							engine.loadOverworld();
						}
					}
				}
			} else {
				// Currently only supports N/E/W/S, could support NE, NW etc.
				// but theres no graphics for it being faced that way so would be weird
				// if we want to add it, just add them as more cases, very easy
				int x = pos.getX();
				int y = pos.getY();
				Direction direction = player.getComponent(Direction.class);
				if(direction.getDir() == Dir.WEST){
					x -= 1;
				} else if(direction.getDir() == Dir.EAST){
					x += 1;
				} else if(direction.getDir() == Dir.NORTH){
					y += 1; 
				} else if(direction.getDir() == Dir.SOUTH){
					y -= 1;
				} 
				tile = dungeon.getTile(x, y);
				entities = tile.getEntities();
				for(Entity e : entities){
					if(Engine.debug){
						System.out.println("Entity in front of player: "+e);
					}
					if(e.containsComponent(Engine.CompSprite | Engine.CompBlocksLineOfSight | Engine.CompBlocksWalking)){
						// should be a door, add door flag later?
						BlocksWalking blocksWalking = e.getComponent(BlocksWalking.class);
						String spriteName = e.getComponent(Sprite.class).getSpriteName();
						int lastUnderscore = spriteName.lastIndexOf('_');
						Entity newDoor;
						if(blocksWalking.getBlocksWalking()){
							// door is closed, open it
							newDoor = EntityCreator.createDoor(x, y,
									(spriteName.substring(0, lastUnderscore) + "_open"), true);
						} else {
							// door is open, close it
							newDoor = EntityCreator.createDoor(x, y,
									(spriteName.substring(0, lastUnderscore)+"_closed"),false);
						}
						engine.removeEntity(e);
						engine.addEntity(newDoor);
					} 
				}
			}
		}
	}
}
