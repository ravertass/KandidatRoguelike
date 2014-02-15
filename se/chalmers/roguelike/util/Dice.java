package se.chalmers.roguelike.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 
 * @author samwise
 *
 */
public class Dice {
	
	
	/**
	 * Rolls the given number of dice and returns an ArrayList with the results
	 * 
	 * @param numberOfDice The number of dice to be rolled.
	 * @param sizeOfDice The number of faces of the dice
	 * @return An ArrayList with the rolled dice.
	 * 
	 */
	public static ArrayList<Integer> rollDice(int numberOfDice, int sizeOfDice) {
		Random random = new Random();
		ArrayList<Integer> rolls = new ArrayList<Integer>();
		for (int i=0; i<numberOfDice; i++) {
			rolls.add(random.nextInt(sizeOfDice)+1);
		}
		return rolls;
	}
	
	/**
	 * Rolls the given number of dice and returns the sum.
	 * 
	 * @param numberOfDice The number of dice to be rolled.
	 * @param sizeOfDice The number of faces of the dice
	 * @return The sum of the rolled dice.
	 */
	public static int roll(int numberOfDice, int sizeOfDice) {
		ArrayList<Integer> rolls = rollDice(numberOfDice, sizeOfDice);
		return sum(rolls);
	}
	
	/**
	 * Rolls the given number of dice, drops the lowest value dice and returns the sum.
	 * @param numberOfDice The number of dice to be rolled.
	 * @param sizeOfDice The number of faces of the dice
	 * @return The sum of the rolled dice minus the lowest dice.
	 */
	public static int rollDropLowest (int numberOfDice, int sizeOfDice) {
		ArrayList<Integer> rolls = rollDice(numberOfDice,sizeOfDice);
		rolls.remove(Collections.min(rolls));
		return sum(rolls);
	}
	
	/**
	 * Rolls the given number of dice, drops the highest value dice and returns the sum.
	 * @param numberOfDice The number of dice to be rolled.
	 * @param sizeOfDice The number of faces of the dice
	 * @return The sum of the rolled dice minus the highest dice.
	 */
	public static int rollDropHighest (int numberOfDice, int sizeOfDice) {
		ArrayList<Integer> rolls = rollDice(numberOfDice,sizeOfDice);
		rolls.remove(Collections.max(rolls));
		return sum(rolls);
	}
	
	
	private static int sum (ArrayList<Integer> rolls) {
		int total = 0;
		for (Integer roll : rolls) {
			total += roll;
		}
		return total;
		
	}
	
}
