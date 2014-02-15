package se.chalmers.roguelike.Components;

public class Character implements IComponent{
	private String name;
	private int level;
	
	
	public Character (String name, int level){
		this.name = name;
		this.level = level;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
}
