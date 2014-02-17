package se.chalmers.roguelike.World;

import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.Dice;

public class Dungeon {
	
	int worldWidth;
	int worldHeight;
	Tile[][] tiles;
	
	public Dungeon(){
		
		// Debug world
		worldWidth = 50;
		worldHeight = 50;
		tiles = new Tile[worldWidth][worldHeight];
		
		
		// Debug hardcoded world
		for(int x=0;x<worldWidth;x++){
			for(int y=0;y<worldHeight;y++){
				if(Dice.roll(2, 6)>=8 && ((x != 10) || (y != 10))) {
					Sprite sprite = new Sprite("wall2");
					tiles[x][y] = new Tile(sprite, false, true);
				} else if (x == 0 || y == 0 || x == worldWidth-1 || y == worldHeight-1) {
					Sprite sprite = new Sprite("wall2");
					tiles[x][y] = new Tile(sprite, false, true);
				} else {
					Sprite sprite = new Sprite("grass");
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
