package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.util.Dice;

public class Attribute implements IComponent {
	private String name;

	public SpaceClass spaceClass;
	public SpaceRace spaceRace;

	private int level;
	private int experience;
	private int xpyield;

	private int strength;
	private int endurance;
	private int perception;
	private int intelligence;
	private int charisma;
	private int agility;

	public enum SpaceClass {
		SPACE_WARRIOR, SPACE_ROGUE, SPACE_MAGE // TODO Moar to be added just dummy for now
	}

	public enum SpaceRace {
		SPACE_ALIEN, SPACE_HUMAN, SPACE_DWARF // TODO Moar to be added, just dummy for now
	}
	
	public static enum Stat {
		STRENGTH, ENDURANCE, PERCEPTION, INTELLIGENCE, CHARISMA, AGILLITY
	}

	public Attribute(String name, SpaceClass spaceClass, SpaceRace spaceRace, int level, int xpyield) {
		this.name = name;
		this.level = level;

		this.spaceClass = spaceClass;
		this.spaceRace = spaceRace;
		this.strength = Dice.rollDropLowest(4, 10);
		this.endurance = Dice.rollDropLowest(4, 10);
		this.perception = Dice.rollDropLowest(4, 10);
		this.intelligence = Dice.rollDropLowest(4, 10);
		this.charisma = Dice.rollDropLowest(4, 10);
		this.agility = Dice.rollDropLowest(4, 10);
		this.xpyield = xpyield;
		if (spaceRace == SpaceRace.SPACE_ALIEN) {
			this.perception += 4;
			this.strength -= 4;
		}
		if (spaceRace == SpaceRace.SPACE_HUMAN) {

		}
		if (spaceRace == SpaceRace.SPACE_DWARF) {
			this.endurance += 4;
			this.agility -= 4;
		}
	}

	public int getMod(int statValue) {
		return -4 + statValue / 4;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int strength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int endurance() {
		return endurance;
	}

	public void setEndurance(int endurance) {
		this.endurance = endurance;
	}

	public int perception() {
		return perception;
	}

	public void setPerception(int perception) {
		this.perception = perception;
	}

	public int intelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int charisma() {
		return charisma;
	}

	public void setCharisma(int charisma) {
		this.charisma = charisma;
	}

	public int agility() {
		return agility;
	}

	public void setAgility(int agility) {
		this.agility = agility;
	}

	public int experience() {
		return experience;
	}

	public void increaseExperience(int x) {
		experience += x;
	}

	public int xpyield() {
		return xpyield;
	}

	public void setXpYield(int x) {
		xpyield = x;
	}

	@Override
	public String toString() {
		return (name + " is a " + spaceClass + " " + spaceRace + ", Strength: " + strength + ", Endurance: "
				+ endurance + ", Perception: " + perception + ", Intelligence: " + intelligence
				+ ", Charisma: " + charisma + ", Agility: " + agility);
	}

	public IComponent clone() {
		Attribute attrib = new Attribute(name, spaceClass, spaceRace, level, xpyield);
		attrib.experience = experience;
		attrib.xpyield = xpyield;
		attrib.strength = strength;
		attrib.endurance = endurance;
		attrib.perception = perception;
		attrib.intelligence = intelligence;
		attrib.charisma = charisma;
		attrib.agility = agility;
		return attrib;
	}
}