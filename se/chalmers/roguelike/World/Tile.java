package se.chalmers.roguelike.World;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Sprite;

public class Tile {

	private Sprite backgroundSprite;
	private ArrayList<Entity> entities;
	private boolean backgroundWalkable;
	private boolean blocksLineOfSight;

	public Tile(Sprite backgroundSprite, boolean backgroundWalkable, boolean blocksLineOfSight) {
		this.backgroundSprite = backgroundSprite;
		this.backgroundWalkable = backgroundWalkable;
		this.blocksLineOfSight = blocksLineOfSight;
		entities = new ArrayList<Entity>();
	}

	/**
	 * Checks if the Tile contains a character, if it does the character is
	 * returned, otherwise returns null.
	 * 
	 * @return A Character if it exists, otherwise null.
	 */
	public Entity containsCharacter() {
		for (Entity e : entities) {
			if (e.getComponent(se.chalmers.roguelike.Components.Character.class) != null)
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
		// Detta kommer så småningom också att kolla om
		// entiteterna i tilen är walkable
		return backgroundWalkable;
	}

	public void setWalkable(boolean walkable) {
		this.backgroundWalkable = walkable;
	}

	public boolean blocksLineOfSight() {
		return blocksLineOfSight;
	}

	public void setBlocksLineOfSight(boolean blocksLineOfSight) {
		this.blocksLineOfSight = blocksLineOfSight;
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	public Sprite getSprite() {
		return backgroundSprite;
	}

	public void setSprite(Sprite sprite) {
		this.backgroundSprite = sprite;
	}

}
