package se.chalmers.roguelike.Components;

import se.chalmers.plotgen.PlotData.Prop;

/**
 * Works as a flag for items that are considered plot loot. Also contains the 
 * Prop equivalent (from the plotgen package) of the item.
 */
public class PlotLoot implements IComponent {

	Prop prop;
	
	public PlotLoot(Prop prop) {
		this.prop = prop;
	}
	
	public PlotLoot clone() {
		return new PlotLoot(prop);
	}
	
	public Prop getProp() {
		return prop;
	}
}
