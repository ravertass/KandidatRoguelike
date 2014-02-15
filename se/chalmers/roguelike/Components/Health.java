package se.chalmers.roguelike.Components;

public class Health implements IComponent{
	
	private int health;
	private int maxHealth;
	private int invulnerable;
	
	public Health(int hp) {
		health = hp;
		maxHealth = hp;
		invulnerable = 0;
	}
	
	public void setHealth(int hp) {
		health = hp;
	}
	
	public int getHealth() {
		return health;
	}
	
	/**
	 * 
	 * @param turns		amount of turns the entity is invulnerable
	 */
	public void setInvulnerable(int turns) {
		if (invulnerable+turns < 0){
			invulnerable = 0;
		}
		invulnerable = turns;
	}
	
	/**
	 * returns a value above 0 if invulnerable otherwise 0
	 * 
	 * @return invulnerable
	 */
	public int getInvulnerable() {
		return invulnerable;
	}
	
	/**
	 *  
	 * @return the maxHealth
	 */
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getHealthPercentage() {
		return health/maxHealth;
	}
	
}
