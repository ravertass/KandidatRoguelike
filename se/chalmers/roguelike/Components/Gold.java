package se.chalmers.roguelike.Components;

public class Gold implements IComponent {
	private int amountOfGold;
	
	public Gold(){
		this(0);
	}
	
	public Gold(int amountOfGold){
		this.amountOfGold = amountOfGold;
	}
	
	public int getGold(){
		return amountOfGold;
	}
}
