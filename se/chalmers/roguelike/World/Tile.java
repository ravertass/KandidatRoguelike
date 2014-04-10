package se.chalmers.roguelike.World;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.BlocksLineOfSight;
import se.chalmers.roguelike.Components.BlocksWalking;
import se.chalmers.roguelike.Components.Sprite;

/**
 * A class representing a tile.
 */
public class Tile {

	private Sprite backgroundSprite;
	private ArrayList<Entity> entities;
	private boolean backgroundWalkable;
	private boolean hasBeenSeen;
	private int itemsBlocking, blocksLineOfSight;

	/**
	 * Creates a new tile
	 * @param backgroundSprite sprite of the tile
	 * @param backgroundWalkable whether or not the tile should be walkable
	 * @param blocksLineOfSight whether or not the tile should block line of sight 
	 */
	public Tile(Sprite backgroundSprite, boolean backgroundWalkable,
			boolean blocksLineOfSight) {
		this.backgroundSprite = backgroundSprite;
		this.backgroundWalkable = backgroundWalkable;
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

	/**
	 * Check if the tile contains a specific entity or not
	 * @param entity entity that is being searched after
	 * @return true if the tile contains it, otherwise false
	 */
	public boolean containsEntity(Entity entity) {
		for (Entity e : entities) {
			if (e.equals(entity))
				return true;
		}
		return false;
	}

	/**
	 * A check if the tile can be walked upon or not
	 * @return true if walkable, otherwise false
	 */
	public boolean isWalkable() {
		return backgroundWalkable && !(itemsBlocking > 0);
	}

	/**
	 * Changes the walkable flag
	 * @param walkable new status
	 */
	public void setWalkable(boolean walkable) {
		this.backgroundWalkable = walkable;
	}

	/**
	 * Checks if the tile blocks line of sight
	 * @return true if it blocks line of sight, othewise false
	 */
	public boolean blocksLineOfSight() {
		return blocksLineOfSight != 0;
	}

	/**
	 * Changes the blocks line of sight status
	 * @param blocksLineOfSight new status
	 */
	public void setBlocksLineOfSight(boolean blocksLineOfSight) {
		if(blocksLineOfSight){
			this.blocksLineOfSight++;
		} else {
			this.blocksLineOfSight = 0; // set to -- or something?
		}
	}

	/**
	 * Adds an entity to the tile
	 * @param e entity that should be added
	 */
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

	/**
	 * Removes an entity from the tile
	 * @param e the entity that should be removed
	 */
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

	/**
	 * Returns the sprite of the tile
	 * @return sprite of the tile
	 */
	public Sprite getSprite() {
		return backgroundSprite;
	}

	/**
	 * Sets a new sprite for the tile
	 * @param sprite new sprite that should be used
	 */
	public void setSprite(Sprite sprite) {
		this.backgroundSprite = sprite;
	}
	
	/**
	 * Sets a new flag if the tile has been seen or not
	 * @param b new status
	 */
	public void setHasBeenSeen(Boolean b) {
		this.hasBeenSeen = b;
	}
	
	/**
	 * Check if the entity has been seen or not
	 * @return true if the tile has been seen, false otherwise
	 */
	public boolean hasBeenSeen() {
		return this.hasBeenSeen;
	}

	/**
	 * Returns the list of entities on the tile
	 * @return a list of entities
	 */
	public ArrayList<Entity> getEntities(){
		// Creates a copy of the list so we dont modify the tiles list directly
		ArrayList<Entity> newList = new ArrayList<Entity>();
		for(Entity e : entities){
			newList.add(e);
		}
		return newList;
	}
}
