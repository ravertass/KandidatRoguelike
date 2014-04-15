package se.chalmers.plotgen.NameGen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This name generator uses Markov Chains of orders 1-4 when generating names
 * 
 * @author David
 * 
 */
public class NameGenerator {

	private String defaultFilePath;
	private Markov markov;
	private int filesLoaded;
	private Random randomizer;

	public NameGenerator(int order, long seed) {
		defaultFilePath = "." + File.separator + "resources" + File.separator
				+ "lists" + File.separator + "male_american_names" + ".txt";
		markov = new Markov(order);
		filesLoaded = 0;
		randomizer = new Random(seed);
	}

	/**
	 * teach the Markov instance a new set of names
	 * 
	 * @param fileName the name of the file you want as input
	 */
	public void loadFile(String fileName) {
		String filePath = "." + File.separator + "resources" + File.separator
				+ "lists" + File.separator + fileName + ".txt";
		setupFile(filePath);
		markov.load(filePath);
		filesLoaded++;
	}

	/**
	 * uses a default name file if nothing was loaded before
	 * 
	 * @return a generated name
	 */
	public String generateName() {
		if (filesLoaded == 0) {
			markov.load(defaultFilePath);
		}
		return markov.generateName(randomizer);
	}

	/**
	 * this method takes the content of a file and then re-write the file with
	 * one word per line
	 * 
	 * @param fileName the path to the file you want to prepare
	 */
	public void setupFile(String fileName) {
		ArrayList<String> names = new ArrayList<String>();

		try {
			Scanner s = new Scanner(new File(fileName));
			while (s.hasNext()) {
				String line = s.nextLine();
				String[] words = line.split("\\s+");
				for (String word : words) {
					names.add(word.toLowerCase());
				}
			}
			s.close();

			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(fileName));
			for (String name : names) {
				writer.write(name + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}