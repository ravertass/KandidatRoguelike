package se.chalmers.roguelike.Components;

import java.util.ArrayList;
import java.util.HashMap;

import se.chalmers.roguelike.Systems.StatusEffectSystem.StatusEffect;

public class StatusEffects implements IComponent {
	
	HashMap<StatusEffect, Integer> statusEffects;
	
	public StatusEffects() {
		statusEffects = new HashMap<StatusEffect, Integer>();
	}
	
	public HashMap<StatusEffect, Integer> getEffects() {
		return this.statusEffects;
	}
	
	public void addEffect(StatusEffect se, int turnsLeft) {
		this.statusEffects.put(se,turnsLeft);
	}
	
	public void removeEffect(StatusEffect se) {
		this.statusEffects.remove(se);
	}
	
	public void removeAllEffects() {
		this.statusEffects.clear();
	}
	
	public void setStatusEffects(HashMap<StatusEffect, Integer> h) {
		this.statusEffects = h;
	}
	
	public StatusEffects clone() {
		StatusEffects se = new StatusEffects();
		se.setStatusEffects(new HashMap<StatusEffect, Integer>(statusEffects));
		return se;
	}

}
