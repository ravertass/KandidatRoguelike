package se.chalmers.roguelike.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import se.chalmers.roguelike.Components.Position;

public class DelauneyTriangulator {
	
	private ArrayList<Triangle> triangles;
	
	/**
	 * Needs a super triangle to be able to triangulate
	 *  
	 * @param superTriangle
	 */
	public DelauneyTriangulator(Triangle superTriangle){
		triangles = new ArrayList<Triangle>();
		triangles.add(superTriangle);
	}

	/**
	 * Takes a list of nodes and creates a delauney triangulation
	 * 
	 * For every triangle, see if the node is within its circumcircle.
	 * If it is, remove the triangle and remember unique edges.
	 * Then create new triangles from the kept edges to the node.
	 * 
	 * @param nodes
	 */
	public ArrayList<Triangle> triangulate(ArrayList<Position> nodes){
		
		//iterate all nodes
		for (Position node : nodes) {
			ArrayList<Edge> edges = new ArrayList<Edge>();
			Set<Edge> preventDuplicateEdges = new HashSet<Edge>();
			
			for (Triangle triangle : triangles) {
				if (triangle.circumCircle().contains(node.getX(), node.getY())){
					triangles.remove(triangle); //faster with indexed for-loop?
					Edge edge1 = new Edge(triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2());
					Edge edge2 = new Edge(triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3());
					Edge edge3 = new Edge(triangle.getX3(), triangle.getY3(), triangle.getX1(), triangle.getY1());
					
					//Add the edges if they haven't been added before, if they have been added
					//before means they are duplicates and should be removed instead
					if (preventDuplicateEdges.add(edge1))
						edges.add(edge1);
					else
						edges.remove(edge1);
					
					if (preventDuplicateEdges.add(edge2))
						edges.add(edge2);
					else
						edges.remove(edge2);
					
					if (preventDuplicateEdges.add(edge3))
						edges.add(edge3);
					else
						edges.remove(edge3);
				}
			}
		//create new triangles	
		for (Edge edge : edges) {
			triangles.add(new Triangle(node.getX(), node.getY(), edge.getX1(), edge.getY1(), edge.getX2(), edge.getY2()));
		}
		}
		return triangles;
	}
	
}