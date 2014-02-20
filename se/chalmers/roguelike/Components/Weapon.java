package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.util.Dice;

public class Weapon implements IComponent{
	
//	private int maxDamage;
//	private int minDamage; maybe for the future?
	
	
	private int range;
	private int numberOfDice;
	private int sizeOfDice;
	private int modifier;
	
	// TODO add things like damagetype, weapontype and such
	
	public Weapon(int numberOfDice, int sizeOfDice, int modifier) { //Add things like name and other stuff to the constructer when applicable.
		this.numberOfDice = numberOfDice;
		this.sizeOfDice = sizeOfDice;
		this.modifier = modifier;
	}
	
	public int getDamage() {
		return Dice.roll(numberOfDice, sizeOfDice) + modifier;
	}
	/**
	 * Returns the range in tiles.
	 * @return
	 */
	public int getRange() {
		return this.range;
	}

}
