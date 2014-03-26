package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
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
		}
	}

}
