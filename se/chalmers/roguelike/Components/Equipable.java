package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.util.EquipmentManager.EquipmentType;
import se.chalmers.roguelike.util.StatModifier;

/**
 * This component holds information about an equipable item.
 * @author twister
 *
 */
public class Equipable implements IComponent {
	
	private EquipmentType eqType;
	private StatModifier statMod;
	
	public Equipable(EquipmentType et) {
		statMod = new StatModifier();
		eqType = et;
	}
	
	public Equipable clone() {
		return new Equipable(eqType);
	}
	/**
	 * Returns on what slot this item will go.
	 * @return
	 */
	public EquipmentType getType() {
		return this.eqType;
	}
	
	public StatModifier getStatModifier() {
		return this.statMod;
	}

}
