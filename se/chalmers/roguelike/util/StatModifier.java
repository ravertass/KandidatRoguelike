package se.chalmers.roguelike.util;

import java.util.HashMap;

import se.chalmers.roguelike.Components.Attribute;

public class StatModifier {
	
	HashMap<Attribute.Stat, Integer> mod;
	
	public StatModifier() {
		mod.put(Attribute.Stat.AGILLITY, 0);
		mod.put(Attribute.Stat.CHARISMA, 0);
		mod.put(Attribute.Stat.ENDURANCE, 0);
		mod.put(Attribute.Stat.PERCEPTION, 0);
		mod.put(Attribute.Stat.STRENGTH, 0);
		mod.put(Attribute.Stat.INTELLIGENCE, 0);
		
	}
	
	public void changeModifier(Attribute.Stat s, int i) {
		mod.put(s, i);
	}
	
	public HashMap<Attribute.Stat, Integer> getModifiers() {
		return this.mod;
	}

}
