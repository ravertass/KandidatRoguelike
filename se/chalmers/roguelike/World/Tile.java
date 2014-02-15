package se.chalmers.roguelike.World;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Entities.Entity;

public class Tile {
		
		Sprite sprite;
		ArrayList<Entity> entities;
		boolean walkable;
		boolean blocksLineOfSight;
		
		public Tile(Sprite sprite, boolean walkable, boolean blocksLineOfSight) {
			this.sprite = sprite;
			this.walkable = walkable;
			this.blocksLineOfSight = blocksLineOfSight;
		}
		
		/**
		 * Checks if the Tile contains a character, if it does the character is returned, otherwise returns null.
		 * @return A Character if it exists, otherwise null.
		 */
		public Entity containsCharacter() {
			for(Entity e : entities) {
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
			return walkable;
		}
		
		public void setWalkable(boolean walkable) {
			this.walkable = walkable;
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
			return sprite;
		}
		
		public void setSprite(Sprite sprite) {
			this.sprite = sprite;
		}
		

		
}
