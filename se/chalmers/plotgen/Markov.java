package se.chalmers.plotgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Markov {

	private final int MAX;
	private final int ORDER;
	HashMap<String, MarkovInstance> zeroOrderMarkovTable;
	HashMap<String, MarkovInstance> firstOrderMarkovTable;
	HashMap<String, MarkovInstance> secondOrderMarkovTable;
	HashMap<String, MarkovInstance> thirdOrderMarkovTable;
	HashMap<String, MarkovInstance> fourthOrderMarkovTable;

	public Markov(int k) {
		MAX = 20;
		if (k < 1 || k > 4) {
			System.err.println("Order has to be between 0 and 4");
		}
		ORDER = k;
		init();
/*
		ArrayList<String> nameLengths = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			String nam = generateName();
			if (nam.length() < MAX) {
				nameLengths.add(nam);
			}
		}
		System.out.println(nameLengths.size() + "\n");
		for (String string : nameLengths) {
			System.out.println(string);
		}*/
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
	 * @param filePath the path to the file you want to teach the
	 * Markov class 
	 */
	public void load(String filePath) {
		Scanner s = readFile(filePath);
		String markovString = initScanner(s);
		String[] trainingSet = markovString.split("\\n");
		for (String word : trainingSet) {
			addWord(word);
		}
	}
	
	/**
	 * 
	 * @param s 	a Scanner with a file
	 * @return 		a String with delimiters
	 */
	private String initScanner(Scanner s){
		String markovString = s.useDelimiter("\\Z").next();
		return markovString;
	}

	/**
	 * 
	 * @param name
	 */
	private void addWord(String name) {
		int length = name.length();
		String startAWord = "START";
		String endAWord = "EOW";
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
			updateTable(oneLetter, endAWord, firstOrderMarkovTable);
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
			updateTable(oneLetter, endAWord, firstOrderMarkovTable);
			updateTable(twoLetters, endAWord, secondOrderMarkovTable);
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
			updateTable(oneLetter, endAWord, firstOrderMarkovTable);
			updateTable(twoLetters, endAWord, secondOrderMarkovTable);
			updateTable(threeLetters, endAWord, thirdOrderMarkovTable);
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

		updateTable(oneLetter, endAWord, firstOrderMarkovTable);
		updateTable(twoLetters, endAWord, secondOrderMarkovTable);
		updateTable(threeLetters, endAWord, thirdOrderMarkovTable);
		updateTable(fourLetters, endAWord, fourthOrderMarkovTable);
	}

	/**
	 *  Updating the ordered markovtable for the Markov chain
	 * 
	 * @param seq	The history that next should be based on
	 * @param next	the next char seen from the history (seq)
	 * @param hmap	the k-order table that should be updated
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

		StringBuilder name = new StringBuilder();
		MarkovInstance start = zeroOrderMarkovTable.get("START");
		ArrayList<String> list = start.toProbabilities();
		int random = (int) (Math.random() * list.size());
		name.append(list.get(random));
		String genName = "";
		String endAWord = "EOW";

		if (ORDER == 1) {
			genName = createName(name.toString(), firstOrderMarkovTable);
		}

		else if (ORDER == 2) {
			while (endAWord.equals("EOW")) {
				MarkovInstance second = firstOrderMarkovTable.get(name
						.toString());
				list.clear();
				list = second.toProbabilities();
				random = (int) (Math.random() * list.size());
				endAWord = list.get(random);
			}
			name.append(endAWord);
			genName = createName(name.toString(), secondOrderMarkovTable);
		}

		else if (ORDER == 3) {
			while (endAWord.equals("EOW")) {
				MarkovInstance second = firstOrderMarkovTable.get(name
						.toString());
				list.clear();
				list = second.toProbabilities();
				random = (int) (Math.random() * list.size());
				endAWord = list.get(random);
			}
			name.append(endAWord);
			// reset endAWord
			endAWord = "EOW";
			while (endAWord.equals("EOW")) {
				MarkovInstance third = secondOrderMarkovTable.get(name
						.toString());
				list.clear();
				list = third.toProbabilities();
				random = (int) (Math.random() * list.size());
				endAWord = list.get(random);
			}
			name.append(endAWord);
			genName = createName(name.toString(), thirdOrderMarkovTable);
		}

		else if (ORDER == 4) {
			while (endAWord.equals("EOW")) {
				MarkovInstance second = firstOrderMarkovTable.get(name
						.toString());
				list.clear();
				list = second.toProbabilities();
				random = (int) (Math.random() * list.size());
				endAWord = list.get(random);
			}
			name.append(endAWord);
			// reset endAWord
			endAWord = "EOW";
			while (endAWord.equals("EOW")) {
				MarkovInstance third = secondOrderMarkovTable.get(name
						.toString());
				list.clear();
				list = third.toProbabilities();
				random = (int) (Math.random() * list.size());
				endAWord = list.get(random);
			}
			name.append(endAWord);
			// reset endAWord
			endAWord = "EOW";
			// while (endAWord.equals("EOW")){
			MarkovInstance fourth = thirdOrderMarkovTable.get(name.toString());
			list.clear();
			list = fourth.toProbabilities();
			random = (int) (Math.random() * list.size());
			endAWord = list.get(random);
			// }
			if (!endAWord.equals("EOW")) {
				name.append(endAWord);
				genName = createName(name.toString(), fourthOrderMarkovTable);
			} else {
				genName = name.toString();
			}
		}
		String startLetter = genName.substring(0, 1).toUpperCase();
		return startLetter + genName.substring(1);
	}

	/**
	 * Helper method for generateName
	 *
	 * @param currentString is the name so far
	 * @param hmap 			is the table you should check in
	 * @return a name
	 */
	private String createName(String currentString,
			HashMap<String, MarkovInstance> hmap) {
		StringBuilder name = new StringBuilder();
		name.append(currentString);
		String sub;

		while (name.length() < MAX) {
			sub = name.substring(name.length() - ORDER, name.length());

			MarkovInstance nextInst = hmap.get(sub);
			if (nextInst == null) {
				System.err.println("ERROR");
				return "E @ " + sub;
			}
			ArrayList<String> prob = nextInst.toProbabilities();
			int random = (int) (Math.random() * prob.size());
			String nextChar = prob.get(random);
			if (nextChar.equals("EOW")) {
				break;
			} else {
				name.append(nextChar);
			}
		}
		return name.toString();
	}
}