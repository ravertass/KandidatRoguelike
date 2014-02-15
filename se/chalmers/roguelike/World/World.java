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
		
		
		// Debug hardcoded world
		for(int x=0;x<worldWidth;x++){
			for(int y=0;y<worldHeight;y++){
				if(x==5 && y==05){
					Sprite sprite = new Sprite("wall");
					tiles[x][y] = new Tile(sprite, false, true);
				} else {
					Sprite sprite = new Sprite("floor");
					tiles[x][y] = new Tile(sprite, true, true);
				}
			}
		}
	}
	
	public boolean isWalkable(int x, int y){
		return x >= 0 && x < worldWidth && y >=0 && y < worldHeight && 
				tiles[x][y].isWalkable() && tiles[x][y] != null;
	}
	
	public Tile getTile(int x, int y){
		if(x < 0 || x >= worldHeight || y < 0 || y >= worldHeight){
			return null;
		}
		return tiles[x][y];
	}
	
	public int getWorldWidth(){
		return worldWidth;
	}
	
	public int getWorldHeight(){
		return worldHeight;
	}
}
