package se.chalmers.plotgen.NameGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MarkovInstance {

	private int total;
	private String chars;
	private HashMap<String, Integer> following;
	
	public MarkovInstance(String chars){
		following = new HashMap<String, Integer>();
		this.chars = chars;
	}

	/**
	 * Updates the HashMap with a char that follows the current sequence
	 * 
	 * @param follower
	 */
	public void updateMap(String follower){
		total++;
		if (!following.containsKey(follower)){
			following.put(follower, 1);
		} else {
			following.put(follower, following.get(follower)+1);
		}
	}
	
	public String getString(){
		return chars;
	}
	
	/**
	 * Creates a somewhat ineffective way to calculate fair probabilities.
	 * Takes the percentage of following chars and then fills an array with
	 * the percentage*10000 of each char
	 * 
	 * @return an array with following chars occurring according their %
	 */
	public ArrayList<String> toProbabilities(){
		Set<String> keySet = following.keySet();
		ArrayList<String> probList = new ArrayList<String>();
		for (String key : keySet) {
			double value = (float)following.get(key)/total;
			value *= 10000;
			while (value > 0){
				probList.add(key);
				value--;
			}
		}
		return probList;
	}
	
	/**
	 * gives the probabilities like toProbabilities for every nextChar except
	 * keyToRemove which will be excluded
	 * 
	 * @param keyToRemove
	 * @return a list of nextChars
	 */
	public ArrayList<String> toProbabilitiesWithout(String keyToRemove){
		Set<String> keySet = following.keySet();
		ArrayList<String> probList = new ArrayList<String>();
		for (String key : keySet) {
			if (!key.equals(keyToRemove)){
				double value = (float)following.get(key)/total;
				value *= 10000;
				while (value > 0){
					probList.add(key);
					value--;
				}
			}
		}
		return probList;
	}
	
	/**
	 * Gives a text representation of the Markov Instance
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("The sequence \"" + getString() + "\" has " + total + " followers spread over " 
				+ following.keySet().size() + " unique instances\n");
		return sb.toString();
	}
}