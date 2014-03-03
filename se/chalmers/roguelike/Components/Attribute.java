package se.chalmers.roguelike.Components;

public class Attribute implements IComponent {
	private String name;
	private int level;

	private int strength;
	private int endurance;
	private int perception;
	private int intelligence;
	private int charisma;
	private int agility;
	
	public Attribute(String name, int level) {
		this.name = name;
		this.level = level;
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
}
