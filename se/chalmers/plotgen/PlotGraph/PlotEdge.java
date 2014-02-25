package se.chalmers.plotgen.PlotGraph;

import se.chalmers.plotgen.PlotData.Action;

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
	
	private Action action;

	public PlotEdge(Action action) {
		this.action = action;
	}
	
	public Action getAction() {
		return action;
	}
}
