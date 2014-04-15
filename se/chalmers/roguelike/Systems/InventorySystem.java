package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Observer;

/**
 * This system handles all the enteties which have an inventory, the player and
 * all the mobs.
 * 
 * @author twister
 * 
 */
public class InventorySystem implements ISystem, Observer {

	private ArrayList<Entity> entities;
	ArrayList<Entity> toRemove;

	private Entity player;

	private ItemSystem itemSystem;

	private Dungeon world;
	
	private Rectangle inventoryBox;

	private boolean timeToLoot;

	public InventorySystem() {
		inventoryBox = new Rectangle(Engine.screenWidth - Engine.hudWidth + 4, 20, Engine.spriteSize * 2 * 6, Engine.spriteSize * 2 * 6);
		itemSystem = new ItemSystem();
		this.entities = new ArrayList<Entity>();
		toRemove = new ArrayList<Entity>();
		timeToLoot = false;
	}

	@Override
	public void update() {

	}

	/**
	 * This will check if the g button has been pressed and then will pick upp
	 * all pocketable items that is on the tile.
	 * 
	 * @param d
	 */
	public void update(Dungeon d) {
		this.world = d;
		if (timeToLoot) {
			Position p = player.getComponent(Position.class);
			Tile playerTile = world.getTile(p.getX(), p.getY());
			toRemove.clear();
			for (Entity e : playerTile.getEntities()) {
				if ((e.getComponentKey() & Engine.CompPocketable) == Engine.CompPocketable
						&& !player.getComponent(Inventory.class).isFull()) {
					player.getComponent(Inventory.class).addItem(e);
					toRemove.add(e);
				}
			}
			for (Entity e : toRemove) {

				playerTile.removeEntity(e); // removes the picked up items from
											// the current tile
			}
			timeToLoot = false;
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
		if ((InputAction) i == InputAction.LOOT) {
			timeToLoot = true;
		} else if(((InputAction) i == InputAction.MOUSECLICK) && Engine.gameState == Engine.GameState.DUNGEON && player != null) {
			if(inventoryBox.contains(Mouse.getX(), Mouse.getY())) {
				// x and y are the "tilecords" in the inventory
				int x = (Mouse.getX() - (Engine.screenWidth - 200 + 4))/(Engine.spriteSize*2);
				int y = (Mouse.getY() - (20 + Engine.spriteSize*2*6)) / (Engine.spriteSize*2);
				System.out.println(x+y*6);
				if((x+y*6) + 1 <= player.getComponent(Inventory.class).getSize()) {
					Entity item = player.getComponent(Inventory.class).getItems().get(x + y * 6);
					if(item != null) {
						itemSystem.useItem(player, item);
						player.getComponent(Inventory.class).deleteItem(item);
					}
				}
				
			}
		}
	}

}
