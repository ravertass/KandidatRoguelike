package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.util.Pair;


public class Input implements IComponent {
	
	private int nextKey;
	private Pair<Integer, Integer> nextMouseClickPos;
	private int nextMouseClick;
	
	public Input() {
		nextKey = -1;
		nextMouseClickPos = new Pair<Integer,Integer>(-1,-1);
		nextMouseClick = -1;
	}
	
	public void setNextKey(int key){
		nextKey = key;		
	}
	public void setNextMouseClick(Pair<Integer,Integer> nextClick, int mouseButton) {
		nextMouseClickPos = nextClick;
		nextMouseClick = mouseButton;
	}
	
	public int getNextKey() {
		return nextKey;
	}
	
	public Pair<Integer,Integer> getNextMouseClickPos() {
		return nextMouseClickPos;
	}
	
	public void resetKey() {
		nextKey = -1;
	}
	
	public void resetMouse() {
		nextMouseClickPos = new Pair<Integer,Integer>(-1,-1);
		nextMouseClick = -1;
	}
	
	public int getNextMouseClick() {
		return nextMouseClick;
	}
	
	public Pair<Integer,Integer> getMousePos() {
		Pair<Integer,Integer> temp = nextMouseClickPos;
		nextMouseClickPos.setFirst(-1);
		nextMouseClickPos.setSecond(-1);
		return temp;
	}
}
