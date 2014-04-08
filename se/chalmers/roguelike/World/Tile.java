package se.chalmers.roguelike.World;

import java.util.ArrayList;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.BlocksLineOfSight;
import se.chalmers.roguelike.Components.BlocksWalking;
import se.chalmers.roguelike.Components.Sprite;

public class Tile {

	private Sprite backgroundSprite;
	private ArrayList<Entity> entities;
	private boolean backgroundWalkable;
	// private boolean blocksLineOfSight;
	private boolean hasBeenSeen;
	private int itemsBlocking, blocksLineOfSight;

	public Tile(Sprite backgroundSprite, boolean backgroundWalkable,
			boolean blocksLineOfSight) {
		this.backgroundSprite = backgroundSprite;
		this.backgroundWalkable = backgroundWalkable;
//		this.blocksLineOfSight = blocksLineOfSight;
		this.blocksLineOfSight = (blocksLineOfSight ? 1 : 0); 
		entities = new ArrayList<Entity>();
		this.hasBeenSeen = false;
		itemsBlocking = 0;
	}

	/**
	 * Checks if the Tile contains a character, if it does the character is
	 * returned, otherwise returns null.
	 * 
	 * @return A Character if it exists, otherwise null.
	 */
	public Entity containsCharacter() {
		for (Entity e : entities) {
			if (e.getComponent(Attribute.class) != null)
				return e;
		}
		return null;
	}

	public boolean containsEntity(Entity entity) {
		for (Entity e : entities) {
			if (e.equals(entity))
				return true;
		}
		return false;
	}

	public boolean isWalkable() {
//		// Detta kommer så småningom också att kolla om
//		// entiteterna i tilen är walkable
//		if (entities.size() != 0)
//			return false;
//		return backgroundWalkable;
		return backgroundWalkable && !(itemsBlocking > 0);
	}

	public void setWalkable(boolean walkable) {
		this.backgroundWalkable = walkable;
	}

	public boolean blocksLineOfSight() {
		return blocksLineOfSight != 0;
	}

	public void setBlocksLineOfSight(boolean blocksLineOfSight) {
		if(blocksLineOfSight){
			this.blocksLineOfSight++;
		} else {
			this.blocksLineOfSight = 0; // set to -- or something?
		}
		// this.blocksLineOfSight = blocksLineOfSight;
	}

	public void addEntity(Entity e) {
		BlocksWalking blocksWalking = e.getComponent(BlocksWalking.class);
		BlocksLineOfSight blocksLOS = e.getComponent(BlocksLineOfSight.class);
		if(blocksWalking != null && blocksWalking.getBlocksWalking()){
			itemsBlocking++;
		}
		if(blocksLOS != null && blocksLOS.getBlockStatus()){
			blocksLineOfSight++;
		}
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		BlocksWalking blocksWalking = e.getComponent(BlocksWalking.class);
		BlocksLineOfSight blocksLOS = e.getComponent(BlocksLineOfSight.class);
		if(blocksWalking != null && blocksWalking.getBlocksWalking()){
			itemsBlocking--;
		}
		if(blocksLOS != null && blocksLOS.getBlockStatus()){
			blocksLineOfSight--;
		}
		entities.remove(e);
	}

	public Sprite getSprite() {
		return backgroundSprite;
	}

	public void setSprite(Sprite sprite) {
		this.backgroundSprite = sprite;
	}
	
	public void setHasBeenSeen(Boolean b) {
		this.hasBeenSeen = b;
	}
	
	public boolean hasBeenSeen() {
		return this.hasBeenSeen;
	}
	
	public ArrayList<Entity> getEntities(){
		// Creates a copy of the list so we dont modify the tiles list directly
		ArrayList<Entity> newList = new ArrayList<Entity>();
		for(Entity e : entities){
			newList.add(e);
		}
		return newList;
	}
}
