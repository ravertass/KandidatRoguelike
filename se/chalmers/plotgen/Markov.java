package se.chalmers.plotgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Markov {

	// private String markovString;
	private final int MAX;
	private final int ORDER;
	HashMap<String, MarkovInstance> zeroOrderMarkovTable;
	HashMap<String, MarkovInstance> firstOrderMarkovTable;
	HashMap<String, MarkovInstance> secondOrderMarkovTable;
	HashMap<String, MarkovInstance> thirdOrderMarkovTable;
	HashMap<String, MarkovInstance> fourthOrderMarkovTable;

	public Markov(int k, String fileName) {
		MAX = 15;
		ORDER = k;
		Scanner s = readFile(fileName);
		String markovString = init(s);

		String[] names = markovString.split("\\n");
		trainingText(k, names);
		System.out.println(zeroOrderMarkovTable.toString().length());
		System.out.println(firstOrderMarkovTable.toString().length());
		System.out.println(secondOrderMarkovTable.toString().length());
		System.out.println(thirdOrderMarkovTable.toString().length());
		System.out.println(fourthOrderMarkovTable.toString());
/*		ArrayList<String> nameLengths = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			String nam = generateName(k, MAX);
			if (nam.length() < MAX){
				nameLengths.add(nam);
			}
		}
		System.out.println(nameLengths.size() + "\n");
		for (String string : nameLengths) {
			System.out.println(string);
		}*/
	}

	/**
	 * Reads the given file if found. This will be used as training set.
	 * 
	 * @param fileName
	 * @return
	 */
	private Scanner readFile(String fileName) {
		Scanner s;
		try {
			s = new Scanner(new File(fileName));
			return s;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Initiate the filter for the scanner and all HashMaps
	 * 
	 * @param s
	 * @return
	 */
	protected String init(Scanner s) {
		zeroOrderMarkovTable = new HashMap<String, MarkovInstance>();
		firstOrderMarkovTable = new HashMap<String, MarkovInstance>();
		secondOrderMarkovTable = new HashMap<String, MarkovInstance>();
		thirdOrderMarkovTable = new HashMap<String, MarkovInstance>();
		fourthOrderMarkovTable = new HashMap<String, MarkovInstance>();
		String markovString = s.useDelimiter("\\Z").next();
		return markovString;
	}

	// Train the generator a list of new words
	private void trainingText(int order, String[] trainingSet) {
		for (String word : trainingSet) {
			addWord(word, order);
		}
	}

	// Adding a new word to update probabilities
	private void addWord(String name, int order) { // Currently not using order
		int length = name.length();
		String firstLetter = name.substring(0, 1);
		String secondLetter = name.substring(1, 2);
		int i = 0;

		// Currently only updates the first letter, misleading name with
		// zero-order (now I became unsure if the ordering should be + 1 instead
		// hmmm)
		if (i < length - 1) {
			updateZeroOrder(firstLetter, secondLetter);
			i++;
		} else {
			updateZeroOrder(firstLetter, "EOW");
			return;
		}

		String twoLetterString = firstLetter.toLowerCase() + secondLetter;

		if (i < length - 1) {
			updateFirstOrder(twoLetterString, name.substring(2, 3));
			i++;
		} else {
			updateFirstOrder(twoLetterString, "EOW");
			return;
		}

		String threeLetterString = twoLetterString + name.substring(2, 3);
		twoLetterString = threeLetterString.substring(1, 3);

		if (i < length - 1) {
			updateFirstOrder(twoLetterString, name.substring(3, 4));
			updateSecondOrder(threeLetterString, name.substring(3, 4));
			i++;
		} else {
			updateFirstOrder(twoLetterString, "EOW");
			updateSecondOrder(threeLetterString, "EOW");
			return;
		}

		String fourLetterString = threeLetterString + name.substring(3, 4);
		threeLetterString = fourLetterString.substring(1, 4);
		twoLetterString = threeLetterString.substring(1, 3);

		if (i < length - 1) {
			updateFirstOrder(twoLetterString, name.substring(4, 5));
			updateSecondOrder(threeLetterString, name.substring(4, 5));
			updateThirdOrder(fourLetterString, name.substring(4, 5));
			i++;
		} else {
			updateFirstOrder(twoLetterString, "EOW");
			updateSecondOrder(threeLetterString, "EOW");
			updateThirdOrder(fourLetterString, "EOW");
			return;
		}

		while (i < length - 1) {
			
			fourLetterString = threeLetterString + name.substring(i, i + 1);
			threeLetterString = fourLetterString.substring(1, 4);
			twoLetterString = threeLetterString.substring(1, 3);
			
			if (i < length - 1) {
				updateFirstOrder(twoLetterString, name.substring(i + 1, i + 2));
				updateSecondOrder(threeLetterString, name.substring(i + 1, i + 2));
				updateThirdOrder(fourLetterString, name.substring(i + 1, i + 2));
				i++;
			} else {
				updateFirstOrder(twoLetterString, "EOW");
				updateSecondOrder(threeLetterString, "EOW");
				updateThirdOrder(fourLetterString, "EOW");
			}
		}
	}

	//Updating the table for the zero-ordered Markov chain
	//should be added empty string and following is the start letter
	private void updateZeroOrder(String firstLetter, String following) {
		// Check if the first letter has been added earlier
		if (!zeroOrderMarkovTable.containsKey(firstLetter)) {
			MarkovInstance instance = new MarkovInstance(firstLetter);
			instance.updateMap(following);
			zeroOrderMarkovTable.put(firstLetter, instance);
		} else {
			MarkovInstance existing = zeroOrderMarkovTable.get(firstLetter);
			existing.updateMap(following);
		}
	}

	//Updating the table for the first-ordered Markov chain
	private void updateFirstOrder(String currentString, String following) {
		// Check if the current combination has been added earlier
		if (!firstOrderMarkovTable.containsKey(currentString)) {
			MarkovInstance instance = new MarkovInstance(currentString);
			instance.updateMap(following);
			firstOrderMarkovTable.put(currentString, instance);
		} else {
			MarkovInstance existing = firstOrderMarkovTable.get(currentString);
			existing.updateMap(following);
		}

	}

	//Updating the table for the second-ordered Markov chain
	private void updateSecondOrder(String currentString, String following) {
		// Check if the combination has been added earlier
		if (!secondOrderMarkovTable.containsKey(currentString)) {
			MarkovInstance instance = new MarkovInstance(currentString);
			instance.updateMap(following);
			secondOrderMarkovTable.put(currentString, instance);
		} else {
			MarkovInstance existing = secondOrderMarkovTable.get(currentString);
			existing.updateMap(following);
		}

	}

	//Updating the table for the third-ordered Markov chain
	private void updateThirdOrder(String currentString, String following) {
		// Check if the combination has been added earlier
		if (!thirdOrderMarkovTable.containsKey(currentString)) {
			MarkovInstance instance = new MarkovInstance(currentString);
			instance.updateMap(following);
			thirdOrderMarkovTable.put(currentString, instance);
		} else {
			MarkovInstance existing = thirdOrderMarkovTable.get(currentString);
			existing.updateMap(following);
		}

	}

	//Updating the table for the fourth-ordered Markov chain
	private void updateFourthOrder(String currentString, String following) {
		// Check if the combination has been added earlier
		if (!fourthOrderMarkovTable.containsKey(currentString)) {
			MarkovInstance instance = new MarkovInstance(currentString);
			instance.updateMap(following);
			fourthOrderMarkovTable.put(currentString, instance);
		} else {
			MarkovInstance existing = fourthOrderMarkovTable.get(currentString);
			existing.updateMap(following);
		}

	}

	/**
	 * Returns a randomly generated name
	 * 
	 * Returns @null if any order outside the interval [0,4] is chosen. 
	 * 
	 * @param k
	 * @param max
	 * @return
	 */
	private String generateName(int k, int max) {
		if (k >= 5 || k < 0) {
			System.err.println("This version only support orders between 0 and 4");
			return null;
		}
		
		StringBuilder name = new StringBuilder();
		
		// TODO complete makeNGram
		if (k == 0){
			//TODO stuff
		} else if (k == 1){
			//TODO stuff
		} else if (k == 2){
			//TODO stuff
			//Chose a random from zeroOrder 
			//pick from firstOrder until finding "\n"
			MarkovInstance inst = zeroOrderMarkovTable.get("b");
			name.append("b");
			ArrayList<String> listOfString = inst.toProbabilities();

			int random = (int) (Math.random()*listOfString.size());
			name.append(listOfString.get(random));
			int i = 2;
		//	String bigram = name.substring(i-2, i);
		//	bigram.toLowerCase(); //fungerar ej?!
			while (i < MAX){
				String bigram = name.substring(i-2, i);
				MarkovInstance nextInst = firstOrderMarkovTable.get(bigram);
				try {
					ArrayList<String> prob = nextInst.toProbabilities();
					if (prob.isEmpty())
						System.err.println("THE PROBABILITY LIST IS EMPTY");		//Gone later
					random = (int) (Math.random()*prob.size());
					String nextChar = prob.get(random);
					if(nextChar.equals("EOW")){
						break;
					} else {
						name.append(nextChar);
					}
					i++;
				} catch (Exception e) {
					System.err.println("ERROR according to problist of: " + name.toString());
					return "ERROR";											//Gone later
				}
			}
			
		} else if (k == 3){
			//TODO stuff
		} else if (k == 4){
			//TODO stuff
		}
		
		return name.toString();
	}
}