package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Generator;


public class OverworldSystem implements ISystem{
	
	private ArrayList<Dungeon> dungeons;
	
	private Dungeon currentDungeon;
	
	
	public OverworldSystem() {
		Dungeon d = new Dungeon();
		for(int i = 0; i<5; i++) {
			d.setWorld(50, 50, new Generator().toTiles());;
			dungeons.add(d);
		}
		
	}

	@Override
	public void update() {
		//TODO add stuff here
		
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO not necessary?
		
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO not necessary?
		
	}

	
}
