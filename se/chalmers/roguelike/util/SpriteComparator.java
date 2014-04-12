package se.chalmers.roguelike.util;

import java.util.Comparator;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Sprite;

/**
 * A comparator for entities that is using sprites
 *
 */
public  class SpriteComparator implements Comparator<Entity> {

	/**
	 * Compares two entities that contains sprite components which each other
	 * 
	 * @param arg0 the entity that works as the base case 
	 * @param arg1 the entity that should be compared agains the base case
	 * 
	 * @return returns -1 if the arg1 resides on a lower layer, 1 if it's higher, 0 otherwise
	 */
	public int compare(Entity arg0, Entity arg1) {
		Sprite sprite1 = arg0.getComponent(Sprite.class);
		Sprite sprite2 = arg1.getComponent(Sprite.class);
		
		int layer1 = sprite1.getLayer();
		int layer2 = sprite2.getLayer();
		if(layer1 > layer2){
			return 1;
		} else if(layer1 < layer2) {
			return -1;
		}
		
		return 0;
	}

}
