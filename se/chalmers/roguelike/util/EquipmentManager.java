package se.chalmers.roguelike.util;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Equipable;

public class EquipmentManager {
	
	public static enum EquipmentType {
		HELMET, ARMOR, GLOVES, RING, MEDALLION
	}
	
	private Entity helmet;
	private Entity armor;
	private Entity gloves;
	private Entity ring;
	private Entity medallion;
	
	
	public EquipmentManager() {
		
	}
	
	public void equip(Entity e) {
		switch(e.getComponent(Equipable.class).getType()) {
			case HELMET: 
				helmet = e;
				break;
			case ARMOR:
				armor = e;
				break;
			case GLOVES:
				gloves = e;
				break;
			case RING:
				ring = e;
				break;
			case MEDALLION:
				medallion = e;
				break;
		}
	}

}
