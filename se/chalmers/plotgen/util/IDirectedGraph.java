package se.chalmers.plotgen.util;

import java.util.Set;

public interface IDirectedGraph<V,E> {

	public void addVertex(V v);
	public void setRootVertex(V rootVertex);
	public boolean addEdge(E e, V v1, V v2);
	public V getRootVertex();
	public Set<Pair<E,V>> getAdjacencies(V v);
}
