package se.chalmers.roguelike.Components;

import java.util.ArrayList;

public class PopupText implements IComponent {
	
	private ArrayList<String> text;
	
	public PopupText(ArrayList<String> s) {
		this.text = s;
	}
	
	public ArrayList<String> getText() {
		return this.text;
	}
	
	public void setStrings(ArrayList<String> s) {
		this.text = s;
	}
	
	public IComponent clone() {
		ArrayList<String> copy = new ArrayList<String>();
		for(String s : text) {
			copy.add(new String(s));
		}
		return new PopupText(copy);
	}


}
