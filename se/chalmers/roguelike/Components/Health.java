package se.chalmers.roguelike.Components;

public class Health implements IComponent{
	
	private int health;
	private int fullHealth;
	
	public Health(int hp){
		health = hp;
		fullHealth = hp;
	}
	
	public void setHealth(int hp){
		health = hp;
	}
	
	public int getHealth(){
		return health;
	}
	
	/**
	 *  
	 * @return the fullHealth
	 */
	public int getFullHP(){
		return fullHealth;
	}
	
}
