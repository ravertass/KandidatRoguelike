package se.chalmers.plotgen;

/**
 * This is an edge in a plot graph.
 * 
 * The edge contains an Interaction, which is a class that models an
 * action between different 'entities' in the plot. The Interaction should
 * be easily interpreted by the game.
 * 
 * @author fabian
 */
public class PlotEdge {
	
	private Interaction interaction;

	public PlotEdge(Interaction interaction) {
		this.interaction = interaction;
	}
	
	public Interaction getInteraction() {
		return interaction;
	}
}
