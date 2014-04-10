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
	
	private int maxSize;

	public Inventory(ArrayList<Entity> a) {
		maxSize = 16; // TODO magic number change laterz
		items = a;
	}

	public Inventory() {
		items = new ArrayList<Entity>();
	}

	public ArrayList<Entity> getItems() {
		return items;
	}

	public void setInventory(ArrayList<Entity> a) {
		this.items = a;
	}

	public void addItem(Entity e) {
		items.add(e);
	}

	public void deleteItem(Entity e) {
		items.remove(e);
	}
	/**
	 * Boolean to return if the current inventory is full.
	 * @return
	 */
	public boolean isFull() {
		return items.size() >= maxSize;
	}
	
	public IComponent clone() {
		ArrayList<Entity> copiedItems = new ArrayList<Entity>();
		for(Entity e : items) {
			copiedItems.add(e.clone());
		}
		return new Inventory(copiedItems);
	}


}
