package se.chalmers.roguelike.Components;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;

/**
 * This class is representing an inventory for a character. All enteties with
 * this component is handled by the inventorysystem.
 * 
 * @author twister
 * 
 */
public class Inventory implements IComponent {

	private ArrayList<Entity> items;

	public Inventory(ArrayList<Entity> a) {
		items = a;
	}

	public Inventory() {
		items = new ArrayList<Entity>();
	}

	private ArrayList<Entity> getItems() {
		return items;
	}

	private void setInventory(ArrayList<Entity> a) {
		this.items = a;
	}

	private void addItem(Entity e) {
		items.add(e);
	}

	private void deleteItem(Entity e) {
		items.remove(e);
	}

}
