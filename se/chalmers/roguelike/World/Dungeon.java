package se.chalmers.roguelike.World;

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
	
	/**
	 * Creates a new world object
	 * 
	 * @param worldWidth the width of the world
	 * @param worldHeight the height of the world
	 * @param tiles the array of tiles the world should have
	 */
	public Dungeon(int worldWidth, int worldHeight, Tile[][] tiles){
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.tiles = tiles;
	}
	
	/**
	 * Currently generates a new world, will probably be removed in the future
	 * when a real level generator exists
	 */
	public Dungeon(){
		
		// Debug world
		worldWidth = 50;
		worldHeight = 50;
		tiles = new Tile[worldHeight][worldWidth];
		
		
		// Debug hardcoded world
		
		// X and Y flipped
		for(int x=0;x<worldWidth;x++){
			for(int y=0;y<worldHeight;y++){
				if(Dice.roll(2, 6)>=8 && ((x != 10) || (y != 10))) {
					Sprite sprite = new Sprite("brick");
					tiles[x][y] = new Tile(sprite, false, true);
				} else if (x == 0 || y == 0 || x == worldWidth-1 || y == worldHeight-1) {
					Sprite sprite = new Sprite("brick");
					tiles[x][y] = new Tile(sprite, false, true);
				} else {
					Sprite sprite = new Sprite("sand");
					tiles[x][y] = new Tile(sprite, true, true);
				}
			}
		}
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
}
