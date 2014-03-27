package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Direction.Dir;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.Gold;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.InputManager.InputAction;

public class InteractionSystem implements ISystem, Observer {

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
			if(e.containsComponent(Engine.CompGold) && !e.containsComponent(Engine.CompPlayer)){
				Gold playerGold = player.getComponent(Gold.class);
				Gold entityGold = e.getComponent(Gold.class);
				playerGold.setGold(playerGold.getGold()+entityGold.getGold());
				System.out.println("GOLD PICKED UP!");
				engine.removeEntity(e);
			}
		}
	}
	
	public void setDungeon(Dungeon dungeon){
		this.dungeon = dungeon;
	}

	@Override
	public void notify(Enum<?> i) {
		if(i.equals(InputAction.INTERACTION)){
			Position pos = player.getComponent(Position.class);
			Tile tile = dungeon.getTile(pos.getX(), pos.getY());
			ArrayList<Entity> entities = tile.getEntities();
			if(entities.size()-1 != 0){ // -1 because player will always be counted as one
				// if this occurs, use the entities on the current tile
				for(Entity e : entities){
					if(e.containsComponent(Engine.CompDungeon)) {
						DungeonComponent dc = e.getComponent(DungeonComponent.class);
						if(dc.getDungeon() == null){
							System.out.println("asdf");
						}
						Dungeon nextDungeon = dc.getDungeon();
						if(nextDungeon != null){
							engine.loadDungeon(nextDungeon, Engine.GameState.DUNGEON);
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
					if(e.containsComponent(Engine.CompSprite | Engine.CompBlocksLineOfSight | Engine.CompBlocksWalking)){
						// should be a door, add door flag later?
						// Generate an open door:
						
						Entity newDoor = EntityCreator.createDoor(x,y,"door_vertical",true);
						engine.removeEntity(e);
						engine.addEntity(newDoor);
					}
				}
			}
		}
	}

}
