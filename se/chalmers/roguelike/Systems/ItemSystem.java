package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Sprite;

public class ItemSystem implements ISystem { 
	
	private ArrayList<Entity> potions;
	
	private ArrayList<Sprite> colors;
	
	public ItemSystem() {
		potions = new ArrayList<Entity>();
		this.setupPotions();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
	public static Entity getRandomPotion() {
		
	}
	
	private void setupPotions() {
		colors.add(new Sprite("potions/potions_blue"));
		colors.add(new Sprite("potions/potions_red"));
		colors.add(new Sprite("potions/potions_white"));
		colors.add(new Sprite("potions/potions_green"));
		colors.add(new Sprite("potions/potions_cyan"));
		colors.add(new Sprite("potions/potions_magenta"));
		colors.add(new Sprite("potions/potions_silver"));
		colors.add(new Sprite("potions/potions_gold"));
		colors.add(new Sprite("potions/potions_misty"));
	}
}
