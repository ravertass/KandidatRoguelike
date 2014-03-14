package se.chalmers.roguelike.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import se.chalmers.roguelike.Components.Position;

public class DelauneyTriangulator {
	
	private ArrayList<Triangle> triangles;
	private Triangle superTriangle;
	
	/**
	 * Needs a super triangle to be able to triangulate
	 *  
	 * @param superTriangle
	 */
	public DelauneyTriangulator(Triangle superTriangle){
		triangles = new ArrayList<Triangle>();
		this.superTriangle = superTriangle;
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
	public Set<Edge> triangulate(ArrayList<Position> nodes){
		triangles.clear();
		triangles.add(superTriangle);
		//iterate all nodes
		System.out.println("Amount of nodes: " + nodes.size() + "\nAmount of triangles: " + triangles.size());
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Position node : nodes) {
			edges.clear();
			ArrayList<Triangle> trianglesToRemove = new ArrayList<Triangle>();
			for (Triangle triangle : triangles) {
				if (triangle.circumCircle().contains(node.getX(), node.getY())){
					trianglesToRemove.add(triangle);
					Edge edge1 = new Edge(triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2());
					Edge edge2 = new Edge(triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3());
					Edge edge3 = new Edge(triangle.getX3(), triangle.getY3(), triangle.getX1(), triangle.getY1());
					
					
					//Add the edges if they haven't been added before, if they have been added
					//before means they are duplicates and should be removed instead
					if (!edges.contains(edge1))
						edges.add(edge1);
					else	
						edges.remove(edge1);

					if (!edges.contains(edge2))
						edges.add(edge2);
					else
						edges.remove(edge2);
					
					if (!edges.contains(edge3))
						edges.add(edge3);
					else
						edges.remove(edge3);
					
				}

			}
			System.out.println("Amount of triangle to remove: " + trianglesToRemove.size() + "\nAmount of edges to create new triangles from: " + edges.size());
			for (Triangle triangle : trianglesToRemove) {
				triangles.remove(triangle);
			}
			//create new triangles	
			for (Edge edge : edges) {
				triangles.add(new Triangle(node.getX(), node.getY(), edge.getX1(), edge.getY1(), edge.getX2(), edge.getY2()));
		}
		}
		System.out.println("Triangles before stemsFromSuper: " + triangles.size());
		removeSuperTriangleStems();
		System.out.println("Triangles after stemsFromSuper: " + triangles.size());
		return toEdges();
	}

	private Set<Edge> toEdges() {
		Set<Edge> edges = new HashSet<Edge>();
		for (Triangle triangle : triangles) {
			edges.add(new Edge(triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2()));
			edges.add(new Edge(triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3()));
			edges.add(new Edge(triangle.getX3(), triangle.getY3(), triangle.getX1(), triangle.getY1()));
		}
		return edges;
	}

	private void removeSuperTriangleStems() {
		ArrayList<Triangle> trianglesToRemove = new ArrayList<Triangle>();
		triangles.remove(superTriangle);
		for (Triangle triangle : triangles) {
			if (triangle.stemsFrom(superTriangle))
				trianglesToRemove.add(triangle);
		}
		for (Triangle triangle : trianglesToRemove) {
			triangles.remove(triangle);
		}
	}
	
	public ArrayList<Triangle> getTriangles(){
		return triangles;
	}
	
}