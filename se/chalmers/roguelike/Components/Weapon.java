package se.chalmers.roguelike.Components;

public class Weapon {
	
//	private int maxDamage;
//	private int minDamage; maybe for the future?
	
	
	private int range;
	private int damage;
	
	// TODO add things like damagetype, weapontype and such
	
	public Weapon(int damage) { //Add things like name and other stuff to the constructer when applicable.
		this.damage = damage;
	}
	
	public int getDamage() {
		return this.damage;
	}
	/**
	 * Returns the range in tiles.
	 * @return
	 */
	public int getRange() {
		return this.range;
	}

}
