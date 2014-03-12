package se.chalmers.roguelike.Components;

public class Seed implements IComponent {
	private long seed;
	public Seed(long seed){
		this.seed = seed;
	}
	
	public long getSeed(){
		return seed;
	}
	
	public void setSeed(long seed){
		this.seed = seed;
	}
}
