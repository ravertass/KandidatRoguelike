package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Sprite;
/**
 * 
 * This system will figure out the correct sprite coordinates for
 * mobs, according to their direction. The pre-determined placement
 * of sprites in a mob spritesheet should be the following:
 * 
 * S|W
 * ---
 * N|E
 * 
 * where S = south, etc.
 * 
 * @author fabian
 *
 */
public class MobSpriteSystem implements ISystem {

	// These are the tile coords for the different directions,
	// according to the spritesheet design above
	private int SX = 0; private int SY = 0;
	private int WX = 1; private int WY = 0;
	private int NX = 0; private int NY = 1;
	private int EX = 1; private int EY = 1;
	
	private ArrayList<Entity> entities;
	
	public MobSpriteSystem() {
		entities = new ArrayList<Entity>();
	}
	
	public void update() {
		for (Entity entity : entities) {
			Sprite sprite = entity.getComponent(Sprite.class);
			Direction direction = entity.getComponent(Direction.class);
			Direction.Dir dir = direction.getDir();
			
			// I guess this code is a bit spaghetti-ish, but meh
			if (dir == Direction.Dir.SOUTH) {
				sprite.setSpriteX(SX);
				sprite.setSpriteY(SY);
			}
			
			if (dir == Direction.Dir.WEST ||dir == Direction.Dir.NORTHWEST || dir == Direction.Dir.SOUTHWEST) {
				sprite.setSpriteX(WX);
				sprite.setSpriteY(WY);
			}
			
			if (dir == Direction.Dir.NORTH) {
				sprite.setSpriteX(NX);
				sprite.setSpriteY(NY);
			}
			
			if (dir == Direction.Dir.EAST || dir == Direction.Dir.SOUTHEAST || dir == Direction.Dir.NORTHEAST) {
				sprite.setSpriteX(EX);
				sprite.setSpriteY(EY);
			}
		}
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

}
