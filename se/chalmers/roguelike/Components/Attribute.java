package se.chalmers.roguelike.Components;

public class Attribute implements IComponent {
	private String name;

	private int level;
	private int experience;
	private int xpyield;
	
	private int strength;
	private int endurance;
	private int perception;
	private int intelligence;
	private int charisma;
	private int agility;

	public Attribute(String name, int level, int strength, int endurance,
			int perception, int intelligence, int charisma, int agility, int xpyield) {
		this.name = name;
		this.level = level;

		this.strength = strength;
		this.endurance = endurance;
		this.perception = perception;
		this.intelligence = intelligence;
		this.charisma = charisma;
		this.agility = agility;
		this.xpyield = xpyield;
	}

	public int getMod(int statValue) {
		
		return -4 + statValue/4;
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
		return this.experience;
	}
	
	public void increaseExperience(int x) {
		this.experience += x;
	}
	
	public int xpyield() {
		return this.xpyield;
	}
	
	public void setXpYield(int x) {
		this.xpyield = x;
	}

}
