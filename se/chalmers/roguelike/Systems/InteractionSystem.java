package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;

public class InteractionSystem implements ISystem {

	private Engine engine;
	private Entity player;
	private Dungeon dungeon;
	
	public InteractionSystem(Engine engine){
		this.engine = engine;
	}
	
	@Override
	public void update() {
		if(dungeon == null || player == null){
			return;
		}
		interact();
	}

	@Override
	public void addEntity(Entity entity) {
		player = entity;
		
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
	private void interact(){
		Position pos = player.getComponent(Position.class);
		Tile tile = dungeon.getTile(pos.getX(), pos.getY());
		ArrayList<Entity> entities = tile.getEntities();
		for(Entity e : entities){
			if(e.containsComponent(Engine.CompGold)){
				System.out.println("GOLD PICKED UP!");
				engine.removeEntity(e);
			}
		}
	}
	
	public void setDungeon(Dungeon dungeon){
		this.dungeon = dungeon;
	}

}
