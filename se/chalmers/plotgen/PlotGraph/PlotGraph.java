package se.chalmers.plotgen.PlotGraph;

import java.util.ArrayList;

import se.chalmers.plotgen.util.DirectedGraph;
import se.chalmers.plotgen.util.Pair;

/**
 * This is a graph which models an abstracted plot.
 * 
 * The edges contain Interactions, which are actions between 'entities' (Actors,
 * Props, Scenes). These will represent different choices for the player or the
 * NPCs. The Interactions will be in an abstracted form, written for the
 * computer easily interpreted by the game.
 * 
 * The nodes contain strings with pieces of story. These strings are written for
 * humans, so they're easily interpreted by the player. A human should be able
 * to read all plot texts of a graph and understand the plot.
 * 
 * This graph is also a state machine; it knows which plot vertex we're at.
 * 
 * @author fabian
 */
public class PlotGraph {
	DirectedGraph<PlotVertex, PlotEdge> graph;
	// Which plot vertex we're at in the story
	PlotVertex activeVertex;

	public PlotGraph() {
		graph = new DirectedGraph<PlotVertex, PlotEdge>();
	}

	public void addRootVertex(PlotVertex rootVertex) {
		graph.addVertex(rootVertex);
		graph.setRootVertex(rootVertex);
	}

	/**
	 * 
	 * @param outVertex
	 * @param inVertex
	 * @param inEdge
	 * @return false if the outVertex doesn't exist
	 */
	// The way this is implemented, the directed graph wouldn't need
	// separate edges, since one vertex can only be reached by one edge
	public boolean addVertex(PlotVertex outVertex, PlotVertex inVertex,
			PlotEdge inEdge) {
		graph.addVertex(inVertex);
		// If the outVertex doesn't exist, return false
		return graph.addEdge(inEdge, outVertex, inVertex);
	}

	/**
	 * 
	 * @param vertex
	 * @return false if given plot vertex doesn't exist in the graph
	 */
	public boolean setActiveVertex(PlotVertex vertex) {
		// If the vertex doesn't exist, return false
		if (!graph.getVertices().contains(vertex)) {
			return false;
		}

		activeVertex = vertex;
		return true;
	}

	@Override
	public String toString() {
		// We iterate through all the vertices and edges, beginning with
		// the root vertex, and add them to the output string.
		PlotVertex rootVertex = graph.getRootVertex();
		String string = rootVertex.getPlotText() + "\n";
		
		ArrayList<Pair<PlotEdge, PlotVertex>> adjacencies = new ArrayList<Pair<PlotEdge, PlotVertex>>();
		adjacencies.addAll(graph.getAdjacencies(rootVertex));

		string = addAdjacents(adjacencies, rootVertex, string, "");

		return string;
	}

	// Help function for the toString function
	// Because recursion roxx
	// (this may seem like a bad idea because of stack overflow etc. but I don't
	// think that will happen in this case)
	private String addAdjacents(
			ArrayList<Pair<PlotEdge, PlotVertex>> adjacencies,
			PlotVertex vertex, String string, String spaces) {
		
		// These will make the text nicely formatted
		spaces += "  ";
		
		for (Pair<PlotEdge, PlotVertex> edgeVertex : adjacencies) {
			
			string += spaces;
			
			PlotEdge edge = edgeVertex.getFirst();
			PlotVertex newVertex = edgeVertex.getSecond();
			
			string += edge.getAction() + " : ";
			string += newVertex.getPlotText() + "\n";
			ArrayList<Pair<PlotEdge, PlotVertex>> newAdjacencies = 
					new ArrayList<Pair<PlotEdge, PlotVertex>>();
			newAdjacencies.addAll(graph.getAdjacencies(newVertex));
			string = addAdjacents(newAdjacencies, newVertex, string, spaces);
		}
		return string;
	}
}
