package se.chalmers.roguelike.Tests;

import java.util.Random;

import se.chalmers.plotgen.NameGen.NameGenerator;


public class NameGenTest {

	
	//To test you need to add \\bin after the . in the filepath in NameGenerator
	public static void main(String[] args) {
		NameGenerator name = new NameGenerator(1, new Random().nextLong());
		int i = 0;
		int maxOne = 0;
		int maxFour = 0;
		int aboveEight = 0;
		int fiveToEight = 0;
		String n;

		System.out.println();
		while (i < 100){
			i++;
			n = name.generateName();
//			System.out.println(n);
			if (n.length() < 2){
				maxOne++;
			} else if(n.length() < 5) {
				maxFour++;
			} else if(n.length() > 8) {
				aboveEight++;
			} else {
				fiveToEight++;
			}
		}
		System.out.println("\nTo short names: " + maxOne + "\nShort names: " + maxFour + 
				"\nLong names: " + fiveToEight + "\nTo long names: " + aboveEight);
		int total = maxOne + maxFour + aboveEight + fiveToEight;
		System.out.println("\nTotal names: " + total);
	}

}