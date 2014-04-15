package se.chalmers.roguelike.Components;

/**
 * A health component that is used by entities that can be destroyed or killed
 */
public class Health implements IComponent {

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
	 * Decreases current health and not maximum health
	 * 
	 * @param n
	 */
	public void decreaseHealth(int n) {
		health -= n;
	}

	/**
	 * Increasing current health and not maximum health
	 * 
	 * @param heal
	 */
	public void increaseHealth(int heal) {
		if (health + heal >= maxHealth) {
			health = maxHealth;
		} else {
			health += heal;
		}
	}

	/**
	 * sets amount of turns the entity is invulnerable
	 * 
	 * @param turns
	 */
	public void setInvulnerable(int turns) {
		if (invulnerable + turns < 0) {
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

	public int getMaxHealth() {
		return maxHealth;
	}

	public double getHealthPercentage() {
		return (double) health / (double) maxHealth;
	}

	public IComponent clone() {
		Health newHealth = new Health(maxHealth);
		newHealth.health = this.health;
		newHealth.invulnerable = this.invulnerable;
		return newHealth;
	}
}