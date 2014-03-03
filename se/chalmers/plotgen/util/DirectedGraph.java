package se.chalmers.plotgen.util;

import java.util.HashMap;
import java.util.Set;

/**
 * A generic directed graph.
 * 
 * @author fabian
 *
 * @param <V> the vertex class
 * @param <E> the edge class
 */

public class DirectedGraph<V,E> {

	private V rootVertex;
	private HashMap<V, HashMap<E, V>> adjacencies;

	/**
	 * @param rootVertex The root vertex of the graph.
	 */
	public DirectedGraph() {
		adjacencies = new HashMap<V, HashMap<E,V>>();
	}
	
	public void setRootVertex(V rootVertex) {
		this.rootVertex = rootVertex;
	}
	
	public void addVertex(V v) {
		adjacencies.put(v, new HashMap<E,V>());
	}

	/**
	 * return false if the vertices didn't exist and the edge couldn't be added
	 */
	public boolean addEdge(E e, V v1, V v2) {
		// If the vertices exist in the graph
		if (adjacencies.containsKey(v1) && adjacencies.containsKey(v2)) {
			adjacencies.get(v1).put(e, v2);
			return true;
		}
		return false;
	}

	public V getRootVertex() {
		return rootVertex;
	}

	public Set<V> getVertices() {
		return adjacencies.keySet();
	}
	
	/**
	 * Since the graph is directed, this will return
	 * the outgoing edges from a vertex.
	 */
	public HashMap<E, V> getAdjacencies(V v) {
		return adjacencies.get(v);
	}

}
