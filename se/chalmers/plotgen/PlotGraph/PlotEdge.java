package se.chalmers.plotgen.PlotGraph;

import se.chalmers.plotgen.PlotData.Action;

/**
 * This is an edge in a plot graph.
 * 
 * The edge contains an Action, which is a class that models an
 * action between different 'entities' in the plot. The Action should
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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PlotEdge) {
			return ((PlotEdge) o).getAction().equals(action);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return action.hashCode();
	}
}
