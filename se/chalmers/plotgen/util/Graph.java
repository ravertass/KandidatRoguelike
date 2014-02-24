package se.chalmers.plotgen.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// TODO: Testa den här klassen!

public class Graph implements IDirectedGraph {

	private IVertex rootVertex;
	private Set<IVertex> vertices;
	// Den här datastrukturen kan verkar fett förvirrande, men
	// i skrivande stund känns den vettig
	private HashMap<IVertex, Set<Pair<IEdge, IVertex>>> edges;

	/**
	 * @param rootVertex The root vertex of the graph.
	 */
	public Graph(IVertex rootVertex) {
		this.rootVertex = rootVertex;
		addVertex(rootVertex);
	}
	
	@Override
	public void addVertex(IVertex v) {
		vertices.add(v);
	}

	/**
	 * return false if the vertices didn't exist and the edge couldn't be added
	 */
	public boolean addEdge(IEdge e, IVertex v1, IVertex v2) {
		// If the vertices exist in the graph
		if (vertices.contains(v1) && vertices.contains(v2)) {
			// If the first vertex doesn't have any edges yet
			if (!edges.containsKey(v1)) {
				edges.put(v1, new HashSet<Pair<IEdge,IVertex>>());
			}
			edges.get(v1).add(new Pair<IEdge,IVertex>(e, v2));
			
			return true;
		}
		return false;
	}

	@Override
	public IVertex getRootVertex() {
		return rootVertex;
	}

	/**
	 * Since the graph is directed, this will return
	 * the outgoing edges from a vertex.
	 */
	@Override
	public Set<Pair<IEdge,IVertex>> getEdgesFrom(IVertex v) {
		return edges.get(v);
	}

}
