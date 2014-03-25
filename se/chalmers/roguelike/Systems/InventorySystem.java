package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Observer;

public class InventorySystem implements ISystem, Observer {
	
	private ArrayList<Entity> entities;
	ArrayList<Entity> toRemove;
	
	private Entity player;
	
	private Dungeon world;
	
	private boolean timeToLoot;
	
	public InventorySystem() {
		this.entities = new ArrayList<Entity>();
		toRemove = new ArrayList<Entity>();
		timeToLoot = false;
	}
	
	@Override
	public void update() {

	}
	
	public void update(Dungeon d) {
		this.world = d;
		if(timeToLoot) {
			Position p = player.getComponent(Position.class);
			Tile playerTile = world.getTile(p.getX(), p.getY());
			toRemove.clear();
			for(Entity e : playerTile.getEntities()) {
				if((e.getComponentKey() & Engine.CompPocketable) == Engine.CompPocketable && !player.getComponent(Inventory.class).isFull()) {
					player.getComponent(Inventory.class).addItem(e);
					toRemove.add(e);
				}
			}
			for(Entity e : toRemove) {
				
				playerTile.removeEntity(e); //removes the picked up items from the current tile
			}
		}
		
	}

	@Override
	public void addEntity(Entity entity) {
		if ((entity.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer) {
			this.player = entity;
		}
		this.entities.add(entity);
		
	}

	@Override
	public void removeEntity(Entity entity) {
		this.entities.add(entity);
		
	}

	@Override
	public void notify(Enum<?> i) {
		if((InputAction)i == InputAction.LOOT)
			timeToLoot = true;
	}



}
