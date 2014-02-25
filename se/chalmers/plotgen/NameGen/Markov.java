package se.chalmers.plotgen.NameGen;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Markov {

	private final int MAX;
	private final int ORDER;
	private final String end = "EOW";
	HashMap<String, MarkovInstance> zeroOrderMarkovTable;
	HashMap<String, MarkovInstance> firstOrderMarkovTable;
	HashMap<String, MarkovInstance> secondOrderMarkovTable;
	HashMap<String, MarkovInstance> thirdOrderMarkovTable;
	HashMap<String, MarkovInstance> fourthOrderMarkovTable;

	public Markov(int k) {
		MAX = 20;
		if (k < 1 || k > 4) {
			System.err.println("Order has to be between 1 and 4 (Markov)");
		}
		ORDER = k;
		init();
	}

	/**
	 * Reads the given file if found
	 * 
	 * @param fileName
	 * @return a Scanner loaded with a file
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
	 * Initiate the HashMaps
	 * 
	 */
	private void init() {
		zeroOrderMarkovTable = new HashMap<String, MarkovInstance>();
		firstOrderMarkovTable = new HashMap<String, MarkovInstance>();
		secondOrderMarkovTable = new HashMap<String, MarkovInstance>();
		thirdOrderMarkovTable = new HashMap<String, MarkovInstance>();
		fourthOrderMarkovTable = new HashMap<String, MarkovInstance>();
	}

	/**
	 * learn the class a new set of words
	 * 
	 * @param filePath the path to the file you want to teach the Markov class
	 */
	public void load(String filePath) {
		Scanner s = readFile(filePath);
		String markovString = initScanner(s);
		String[] trainingSet = markovString.split("\\n");
		for (String word : trainingSet) {
			addWord(word.trim());
		}
	}

	/**
	 * 
	 * @param s is a Scanner with a file
	 * @return a String with delimiters
	 */
	private String initScanner(Scanner s) {
		String markovString = s.useDelimiter("\\Z").next();
		return markovString;
	}

	/**
	 * Adds a word to the markovtables
	 * 
	 * @param name
	 */
	private void addWord(String name) {
		int length = name.length();
		String startAWord = "START";
		String oneLetter;
		String twoLetters;
		String threeLetters;
		String fourLetters;
		String following;
		int i = 0;

		// Updates the table with starting characters
		if (i < length) {
			following = name.substring(i, i + 1);
			updateTable(startAWord, following, zeroOrderMarkovTable);
			i++;
			// Update the strings
			oneLetter = following;
		} else {
			return;
		}

		// Updates the tables including the one based on the history of 1
		if (i < length) {
			following = name.substring(i, i + 1);
			updateTable(oneLetter, following, firstOrderMarkovTable);
			i++;
			// Update the strings
			twoLetters = oneLetter + following;
			oneLetter = following;
		} else {
			updateTable(oneLetter, end, firstOrderMarkovTable);
			return;
		}

		// Updates the tables including the one based on the history of 2
		if (i < length) {
			following = name.substring(i, i + 1);
			updateTable(oneLetter, following, firstOrderMarkovTable);
			updateTable(twoLetters, following, secondOrderMarkovTable);
			i++;
			// Update the strings
			threeLetters = twoLetters + following;
			twoLetters = oneLetter + following;
			oneLetter = following;
		} else {
			updateTable(oneLetter, end, firstOrderMarkovTable);
			updateTable(twoLetters, end, secondOrderMarkovTable);
			return;
		}

		// Updates the tables including the one based on the history of 3
		if (i < length) {
			following = name.substring(i, i + 1);
			updateTable(oneLetter, following, firstOrderMarkovTable);
			updateTable(twoLetters, following, secondOrderMarkovTable);
			updateTable(threeLetters, following, thirdOrderMarkovTable);
			i++;
			// Update the strings
			fourLetters = threeLetters + following;
			threeLetters = twoLetters + following;
			twoLetters = oneLetter + following;
			oneLetter = following;
		} else {
			updateTable(oneLetter, end, firstOrderMarkovTable);
			updateTable(twoLetters, end, secondOrderMarkovTable);
			updateTable(threeLetters, end, thirdOrderMarkovTable);
			return;
		}

		// Updates the tables including the one based on the history of 4
		while (i < length) {
			following = name.substring(i, i + 1);
			updateTable(oneLetter, following, firstOrderMarkovTable);
			updateTable(twoLetters, following, secondOrderMarkovTable);
			updateTable(threeLetters, following, thirdOrderMarkovTable);
			updateTable(fourLetters, following, fourthOrderMarkovTable);
			i++;
			// Update the strings
			fourLetters = threeLetters + following;
			threeLetters = twoLetters + following;
			twoLetters = oneLetter + following;
			oneLetter = following;
		}

		updateTable(oneLetter, end, firstOrderMarkovTable);
		updateTable(twoLetters, end, secondOrderMarkovTable);
		updateTable(threeLetters, end, thirdOrderMarkovTable);
		updateTable(fourLetters, end, fourthOrderMarkovTable);
	}

	/**
	 * Updating the ordered markov table for the Markov chain. Helper method to
	 * addWord
	 * 
	 * @param seq The history that next should be based on
	 * @param next the next char seen from the history (seq)
	 * @param hmap the k-order table that should be updated
	 */
	private void updateTable(String seq, String next, HashMap<String, MarkovInstance> hmap) {
		if (!hmap.containsKey(seq)) {
			MarkovInstance instance = new MarkovInstance(seq);
			instance.updateMap(next);
			hmap.put(seq, instance);
		} else {
			MarkovInstance existing = hmap.get(seq);
			existing.updateMap(next);
		}
	}

	/**
	 * Returns a randomly generated name
	 * 
	 * @return a Markov chain presented as a String
	 */
	public String generateName() {

		MarkovInstance start = zeroOrderMarkovTable.get("START");
		ArrayList<String> list = start.toProbabilities();
		int random = (int) (Math.random() * list.size());
		String genName = list.get(random);
		
		String twoLetter = initNextChar(genName, firstOrderMarkovTable);
		if (twoLetter.equals(genName))
			return firstToUpperCase(twoLetter);
		
		if (ORDER == 1)
			return genName = genName + createName(twoLetter.substring(1), firstOrderMarkovTable);

		if (ORDER == 2)
			genName = createName(twoLetter, secondOrderMarkovTable);
		
		String threeLetter = initNextChar(twoLetter, secondOrderMarkovTable);
		if (threeLetter.equals(twoLetter))
			return firstToUpperCase(threeLetter);

		if (ORDER == 3)
			return genName = createName(threeLetter, thirdOrderMarkovTable);

		String fourLetter = initNextChar(threeLetter, thirdOrderMarkovTable);
		if (fourLetter.equals(threeLetter))
			return firstToUpperCase(fourLetter);

		if (ORDER == 4)
			return genName = createName(fourLetter, fourthOrderMarkovTable);
		
		return "ERROR";
	}

	/**
	 * Helper method for generateName
	 * 
	 * @param sequence is the name generated so far
	 * @param hmap should be the hashmap of order ORDER
	 * @return a name
	 */
	private String createName(String sequence,
			HashMap<String, MarkovInstance> hmap) {
		StringBuilder name = new StringBuilder();
		name.append(sequence);
		String sub;

		while (name.length() < MAX) {
			sub = name.substring(name.length() - ORDER, name.length());

			MarkovInstance nextInst = hmap.get(sub);
			ArrayList<String> prob = nextInst.toProbabilities();
			int random = (int) (Math.random() * prob.size());
			String nextChar = prob.get(random);
			if (nextChar.equals(end)) {
				break;
			} else {
				name.append(nextChar);
			}
		}
		return firstToUpperCase(name.toString());
	}

	private String initNextChar(String sequence,
			HashMap<String, MarkovInstance> hmap) {
		ArrayList<String> list = new ArrayList<String>();
		MarkovInstance instance = hmap.get(sequence);
		list = instance.toProbabilitiesWithout(end);
		if (list.isEmpty()) {
			return sequence;
		}
		int random = (int) (Math.random() * list.size());
		return sequence + list.get(random);
	}

	private String firstToUpperCase(String name) {
		String startLetter = name.substring(0, 1).toUpperCase();
		return startLetter + name.substring(1);
	}
}