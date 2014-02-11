package se.chalmers.roguelike.Components;

public class Health implements IComponent{
	private int health;
	
	public Health(int hp){
		health = hp;
	}
	
	public void setHealth(int hp){
		health = hp;
	}
	
	public int getHealth(){
		return health;
	}
}
