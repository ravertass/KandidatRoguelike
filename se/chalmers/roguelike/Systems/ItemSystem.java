package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.DoubleName;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Player;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Usable;
import se.chalmers.roguelike.util.Pair;

public class ItemSystem implements ISystem {

	private static HashMap<Entity, UseEffect> lookupPotions;
	private static ArrayList<Pair<Entity, UseEffect>> listPotions;

	private ArrayList<String> colors;
	private UseEffect[] effects;

	public static enum UseEffect {
		HEAL, TAKE_DAMAGE
	};

	public ItemSystem() {
		colors = new ArrayList<String>();
		lookupPotions = new HashMap<Entity, UseEffect>();
		listPotions = new ArrayList<Pair<Entity, UseEffect>>();
		this.setupPotions();
		effects = UseEffect.values();
		Collections.shuffle(colors);
		int counter = 0;
		for (UseEffect pe : effects) {
			Entity e = new Entity(colors.get(counter) + " potion");
			e.add(new Sprite("potions/potion_" + colors.get(counter)));
			e.add(new DoubleName(pe.toString().toLowerCase() + " potion"));
			e.add(new Usable(pe));
			lookupPotions.put(e, pe);
			listPotions.add(new Pair<Entity, UseEffect>(e, pe));
		}
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

	/**
	 * This uses an item on the specific target.
	 * 
	 * @param target
	 * @param item
	 */
	public void useItem(Entity target, Entity item) {
		System.out.println("Heeeaaaaliiiiiiing :D only");

		if(item.getComponent(Usable.class) != null) {
			UseEffect ue = item.getComponent(Usable.class).getUseEffect();
			
			if (ue == UseEffect.HEAL) { // Heals for 50% of the targets maxhealth.
				Health h = target.getComponent(Health.class);
				h.increaseHealth(h.getMaxHealth() / 2);
			} else if (ue == UseEffect.TAKE_DAMAGE) { // takes 25 % damage if player
														// drinks it, 50 % if thrown
				Health h = target.getComponent(Health.class);
				if (target.getComponent(Player.class) != null) {
					h.decreaseHealth(h.getMaxHealth() / 4);
				} else {
					h.decreaseHealth(h.getMaxHealth() / 2);
				}
			}
		}
		


	}

	public static Entity getRandomPotion() {
		Random r = new Random();
		int i = r.nextInt(lookupPotions.size());
		return listPotions.get(i).getFirst().clone();
	}

	private void setupPotions() {
		colors.add("blue");
		colors.add("red");
		// colors.add(new Sprite("potions/potions_white"));
		// colors.add(new Sprite("potions/potions_green"));
		// colors.add(new Sprite("potions/potions_cyan"));
		// colors.add(new Sprite("potions/potions_magenta"));
		// colors.add(new Sprite("potions/potions_silver"));
		// colors.add(new Sprite("potions/potions_gold"));
		// colors.add(new Sprite("potions/potions_misty"));
	}

}
