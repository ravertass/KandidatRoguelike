package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.util.Pair;


public class Input implements IComponent {
	
	private int nextKey;
	private Pair<Integer, Integer> nextMouseClick;
	
	public Input() {
		nextKey = -1;
		nextMouseClick = new Pair<Integer,Integer>(-1,-1);
	}
	
	public void setNextKey(int key){
		nextKey = key;		
	}
	public void setNextMouseClick(Pair<Integer,Integer> nextClick) {
		nextMouseClick = nextClick;
	}
	
	public int getNextKey() {
		return nextKey;
	}
	
	public Pair<Integer,Integer> getNextMouseClick() {
		return nextMouseClick;
	}
	
	public void resetKey() {
		nextKey = -1;
	}
	
	public void resetMouse() {
		nextMouseClick = new Pair<Integer,Integer>(-1,-1);
	}
	
	public Pair<Integer,Integer> getMouseCords() {
		Pair<Integer,Integer> temp = nextMouseClick;
		nextMouseClick.setFirst(-1);
		nextMouseClick.setSecond(-1);
		return temp;
	}
}
