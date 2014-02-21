package se.chalmers.plotgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MarkovInstance {

	private int total;
	private String chars;
	private HashMap<String, Integer> following;
	
	public MarkovInstance(String chars){
		following = new HashMap<>();
		this.chars = chars;
	}

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
	
	public ArrayList<String> toProbabilities(){
		Set<String> keySet = following.keySet();
		ArrayList<String> probList = new ArrayList<String>();
		for (String key : keySet) {
			float value = (float)following.get(key)/total;
			value *= 1000;
			while (value > 0){
				probList.add(key);
				value--;
			}
		}
		return probList;
	}
	
	public String toString(){
		String statString = "The sequence \"" + getString() + "\" has this many followers:";
		//StringBuilder sb = new StringBuilder();
		return statString + total + "\n";
	}
}
