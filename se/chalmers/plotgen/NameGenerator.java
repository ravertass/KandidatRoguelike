package se.chalmers.plotgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class NameGenerator {
	
	String filePath;
	
	public NameGenerator(){
		filePath = "bin" + File.separator + "resources" + File.separator + "lists"
				+ File.separator + "male_american_names" + ".txt";
		setupFile(filePath);
		new Markov(2, filePath);
	}

	/**
	 * this method takes the content of a file and then re-write 
	 * the file with one word per line
	 * 
	 * @param fileName	the path to the file you want to prepare
	 */
	public void setupFile(String fileName) {
		ArrayList<String> names = new ArrayList<>();

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
	
	public static void main(String[] args) {
		new NameGenerator();
	}

}