package se.chalmers.roguelike.util;

import java.util.Comparator;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Sprite;

public  class SpriteComparator implements Comparator<Entity> {

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
