package se.chalmers.roguelike.World;

import java.util.ArrayList;
import java.util.Iterator;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.Dice;

/**
 * Dungeon is an class that holds the game world along with several helper 
 * functions for determining if the world can be walked on etc.
 * 
 * @author Anttila
 *
 */

public class Dungeon {
	
	private int worldWidth;
	private int worldHeight;
	private Tile[][] tiles;
	private ArrayList<Entity> entities;
	private ArrayList<Entity> backup = new ArrayList<Entity>();  
	private Engine engine; // I dont like this solution, discuss and make better
	/**
	 * Creates a new world object
	 * 
	 * @param worldWidth the width of the world
	 * @param worldHeight the height of the world
	 * @param tiles the array of tiles the world should have
	 */
	public Dungeon(Engine engine, int worldWidth, int worldHeight, Tile[][] tiles){
		this.engine = engine;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.tiles = tiles;
		entities = new ArrayList<Entity>();
	}
	
	/**
	 * Currently generates a new world, will probably be removed in the future
	 * when a real level generator exists
	 */
	public Dungeon(Engine engine){ 
		
		this.engine = engine;
		
		// Debug world
		worldWidth = 50;
		worldHeight = 50;
		tiles = new Tile[worldHeight][worldWidth];
		entities = new ArrayList<Entity>();
		
		// Debug hardcoded world
		
		// X and Y flipped
//		for(int x=0;x<worldWidth;x++){
//			for(int y=0;y<worldHeight;y++){
//				if(Dice.roll(2, 6)>=8 && ((x != 10) || (y != 10))) {
//					Sprite sprite = new Sprite("brick");
//					tiles[x][y] = new Tile(sprite, false, true);
//				} else if (x == 0 || y == 0 || x == worldWidth-1 || y == worldHeight-1) {
//					Sprite sprite = new Sprite("brick");
//					tiles[x][y] = new Tile(sprite, false, true);
//				} else {
//					Sprite sprite = new Sprite("sand");
//					tiles[x][y] = new Tile(sprite, true, true);
//				}
//			}
//		}
	}
	
	/**
	 * Looks up if something can walk on a tile
	 * 
	 * @param x x coordinate for the tile to look up
	 * @param y y coordinate for the tile to look up
	 * @return true if the tile can be walked on, false if it can not (already occupied, walls, etc)
	 */
	public boolean isWalkable(int x, int y){
		return x >= 0 && x < worldWidth && y >=0 && y < worldHeight && 
				tiles[y][x] != null && tiles[y][x].isWalkable();
	}
	
	/**
	 * Looks up and returns a tile 
	 * 
	 * @param x x coordinate for the tile to look up
	 * @param y y coordinate for the tile to look up
	 * @return returns the tile if it exists, otherwise null
	 */
	public Tile getTile(int x, int y){
		if(x < 0 || x >= worldHeight || y < 0 || y >= worldHeight){
			return null;
		}
		return tiles[y][x];
	}
	
	public int getWorldWidth(){
		return worldWidth;
	}
	
	public int getWorldHeight(){
		return worldHeight;
	}
	
	public Tile[][] getWorld(){
		return tiles;
	}
	
	/**
	 * Method use to changing the world
	 * @param worldWidth the width of the new world, must match the array
	 * @param worldHeight the height of the new world, must match the array
	 * @param tiles a 2d-array of all the tiles for the world
	 */
	public void setWorld(int worldWidth, int worldHeight, Tile[][] tiles){
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.tiles = tiles;
	}
	public void addEntity(int x, int y, Entity entity){
		if(x<0 || y<0 || x > worldWidth || y > worldHeight || tiles[y][x] == null){
			return; // out of bounds check
		}
		tiles[y][x].addEntity(entity);
		entities.add(entity);
	}
	public void removeEntity(int x, int y, Entity entity){
		if(x<0 || y<0 || x > worldWidth || y > worldHeight || tiles[y][x] == null){
			return; // out of bounds check
		}
		tiles[y][x].removeEntity(entity);
		entities.remove(entity);
	}
	public void unregister(){

		/*
		 * This is probably the worst code I've written in a while. Why does it look 
		 * like this? To avoid concurrency issues, otherwise if you just iterate over
		 * the list, it will change the list since removeEntity will in the end remove
		 * it from the list you're iterating over. Stupid goddamn thing.
		 * 
		 * I'll rewrite it to not be crap some day I guess.
		 */
		int player = -1;
		for(int i=0;i<entities.size();i++){
			Entity e = entities.get(i);
			if((e.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer){ 
				player = i;
			}
			backup.add(e);
		}

		for(Entity e : backup){
			engine.removeEntity(e);
		}
		if(player != -1){
			backup.remove(player); // makes sure the player is removed from the backup so he isn't added twice in a restore (once from engine, once in register())
		}
		entities.clear(); // clears the active entities, to avoid concurrent exceptions
	}
	public void register(){
		System.out.println("Foobar");
		for(Entity e : backup){
			System.out.println("RESTORING");
			engine.addEntity(e);
		}
		
		// All entities has been added, backup can be cleared
		backup.clear();
	}
}
