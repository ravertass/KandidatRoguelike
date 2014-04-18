package se.chalmers.roguelike.Components;

import java.util.ArrayList;

import se.chalmers.roguelike.Systems.StatusEffectSystem.StatusEffect;

public class StatusEffects {
	
	ArrayList<StatusEffect> statusEffects;
	
	public StatusEffects() {
		statusEffects = new ArrayList<StatusEffect>();
	}
	
	public void addEffect(StatusEffect se) {
		this.statusEffects.add(se);
	}
	
	public void removeEffect(StatusEffect se) {
		this.statusEffects.remove(se);
	}
	
	public void removeAllEffects() {
		this.statusEffects.clear();
	}

}
