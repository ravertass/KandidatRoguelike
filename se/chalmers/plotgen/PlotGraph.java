package se.chalmers.plotgen;

import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * This is a graph which models an abstracted plot.
 * 
 * The edges contain Interactions, which are actions between
 * 'entities' (Actors, Props, Scenes). These will represent different
 * choices for the player or the NPCs. The Interactions will be in an
 * abstracted form, written for the computer easily interpreted by
 * the game.
 * 
 * The nodes contain strings with pieces of story. These strings
 * are written for humans, so they're easily interpreted by the player.
 * A human should be able to read all plot texts of a graph and
 * understand the plot.
 * 
 * @author fabian
 */
public class PlotGraph {
	DefaultDirectedGraph<PlotVertex, PlotEdge> graph;
	
	public PlotGraph() {
		graph = new DefaultDirectedGraph<PlotVertex, PlotEdge>(PlotEdge.class);
	}
	
	/**
	 * This method only added for testing purposes. The PlotGraph class' methods
	 * should be the only interface between the internal graph and the outside (of course :P).
	 * @return
	 */
	public DefaultDirectedGraph<PlotVertex, PlotEdge> getGraph() {
		return graph;
	}
}
