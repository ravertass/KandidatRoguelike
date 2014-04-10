package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.Systems.ItemSystem;
import se.chalmers.roguelike.Systems.ItemSystem.UseEffect;

/**
 * Just a flag for things you can have in your inventory.
 * @author twister
 *
 */
public class Usable implements IComponent {
	
	private UseEffect useEffect;
	
	public Usable(UseEffect ue) {
		this.useEffect = ue;
	}
	
	public UseEffect getUseEffect() {
		return this.useEffect;
	}

}
