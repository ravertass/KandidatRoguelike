package se.chalmers.roguelike.Components;

/**
 * TurnsLeft is a class designed to keep track of how many turns a unit has left. This is here to implement
 * buffs and debuffs.
 */
public class TurnsLeft implements IComponent {
	private int turnsLeft;

	/**
	 * Creates a new instance of turnsleft with one turn remaining.
	 */
	public TurnsLeft() {
		turnsLeft = 1;
	}

	/**
	 * Create a new instance of turnsleft with a set number of turns.
	 * 
	 * @param turnsLeft number of turns to be used.
	 */
	public TurnsLeft(int turnsLeft) {
		this.turnsLeft = turnsLeft;
	}

	/**
	 * Returns the number of turns left.
	 * 
	 * 0 means no turns left for the round, while it can be >1, 1 is the default value. Anything larger means
	 * it can do several turns on a round.
	 * 
	 * <0 means there's some kind of debuff and it has to skip turns.
	 * 
	 * @return
	 */
	public int getTurnsLeft() {
		return turnsLeft;
	}

	/**
	 * Sets a new number of turns left.
	 * 
	 * @param turnsLeft new amount of turns left.
	 */
	public void setTurnsLeft(int turnsLeft) {
		this.turnsLeft = turnsLeft;
	}

	public void decreaseTurnsLeft() {
		turnsLeft -= 1;
	}

	public IComponent clone() {
		return new TurnsLeft(turnsLeft);
	}

}
