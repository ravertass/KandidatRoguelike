package se.chalmers.roguelike.World;

import se.chalmers.roguelike.Components.Sprite;

public class World {
	
	int worldWidth;
	int worldHeight;
	Tile[][] tiles;
	
	public World(){
		
		// Debug world
		worldWidth = 50;
		worldHeight = 50;
		tiles = new Tile[worldWidth][worldHeight];
		for(int x=0;x<worldWidth;x++){
			for(int y=0;y<worldHeight;y++){
				Sprite sprite = new Sprite("floor");
				tiles[x][y] = new Tile(sprite, true, true);
			}
		}
	}
	
	public boolean collisionCheck(int x, int y){
		//return tiles[x][y].collision();
		return true;
	}
	
	public Tile getTile(int x, int y){
		return tiles[x][y];
	}
	
	public int getWorldWidth(){
		return worldWidth;
	}
	
	public int getWorldHeight(){
		return worldHeight;
	}
}
