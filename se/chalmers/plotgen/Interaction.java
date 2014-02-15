package se.chalmers.plotgen;

/**
 * This class models actions between 'entities' in the plot.
 * 'Entities' (not to be confused with the object type in the
 * EC design pattern) can be Actors, Props or Scenes.
 * 
 * This class won't really be implemented for a long time. Now, it only
 * contains a string for testing purposes.
 * 
 * The idea is that the interactions should be of forms like:
 * - [Donald Duck] [goes to] [Magma Planet]
 * - [Chip] [talks to] [Dale]
 * - [Goofy] [kills] [Minnie Mouse]
 * - [Mickey Mouse] [takes] [Apple of Doom] from [Ice Planet]
 * - [The Beagle Boys] [take] [Orange of Despair] from [Scrooge McDuck]
 * 
 * Interactions should be easily interpreted by the game.
 * 
 * @author fabian
 */
public class Interaction {

	private String testString;
	
	public Interaction(String testString) {
		this.testString = testString;
	}
	
	public String getTestString() {
		return testString;
	}
}
