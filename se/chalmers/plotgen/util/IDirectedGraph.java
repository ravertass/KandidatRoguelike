package se.chalmers.plotgen.util;

import java.util.Set;

public interface IDirectedGraph {

	public void addVertex(IVertex v);
	public boolean addEdge(IEdge e, IVertex v1, IVertex v2);
	public IVertex getRootVertex();
	public Set<Pair<IEdge,IVertex>> getEdgesFrom(IVertex v);
}
