package se.chalmers.roguelike.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import se.chalmers.roguelike.Components.Position;

public class KruskalMST {

	private double totalWeight;
	private ArrayList<Edge> path;
	
	public KruskalMST(){
		path = new ArrayList<>();
		totalWeight = 0;
	}
	
	public ArrayList<Edge> createMST(ArrayList<Edge> edges){
		Collections.sort(edges);

		//Create a list of all nodes
		ArrayList<Position> nodes = new ArrayList<Position>();
		for (Edge edge : edges) {
			Position pos1 = new Position(edge.getX1(), edge.getY1());
			Position pos2 = new Position(edge.getX2(), edge.getY2());
			if (!nodes.contains(pos1))
				nodes.add(pos1);
			if (!nodes.contains(pos2))
				nodes.add(pos2);
		}
		while (!(edges.isEmpty())) {
			Edge currentEdge = edges.remove(0);
			Position pos1 = new Position(currentEdge.getX1(), currentEdge.getY1());
			Position pos2 = new Position(currentEdge.getX2(), currentEdge.getY2());
			if(!createsCyclic(currentEdge)){
				path.add(currentEdge);
				totalWeight += currentEdge.getWeight();
				nodes.remove(pos1);
				nodes.remove(pos2);
			}
		}
		return path;
	}
	
	//Check for cyclic behaviour
	private boolean createsCyclic(Edge newEdge){
		Position start = new Position(newEdge.getX1(), newEdge.getY1());
		Position end = new Position(newEdge.getX2(), newEdge.getY2());
		
		HashMap<Position, ArrayList<Edge>> nodes = new HashMap<Position, ArrayList<Edge>>();
		for (Edge edge : path) {
			Position pos1 = new Position(edge.getX1(), edge.getY1());
			Position pos2 = new Position(edge.getX2(), edge.getY2());
			if (!nodes.containsKey(pos1)){
				ArrayList<Edge> edges = new ArrayList<Edge>();
				edges.add(edge);
				nodes.put(pos1, edges);
			} else {
				nodes.get(pos1).add(edge);
			}
			if (!nodes.containsKey(pos2)){
				ArrayList<Edge> edges = new ArrayList<Edge>();
				edges.add(edge);
				nodes.put(pos2, edges);
			} else {
				nodes.get(pos2).add(edge);
			}
		}
		return canGetToEnd(start, end, nodes);
	}
		
		public boolean canGetToEnd(Position start, Position end, HashMap<Position, ArrayList<Edge>> nodes){
			if (start.equals(end))
				return true;
			if (nodes.isEmpty())
				return false;
			if (nodes.containsKey(start)){
				ArrayList<Edge> edges = nodes.remove(start);
				ArrayList<Boolean> bools = new ArrayList<Boolean>();
				for (Edge edge : edges) {
					Position pos1 = new Position(edge.getX1(), edge.getY1());
					if(start.equals(pos1))
						start = new Position(edge.getX2(), edge.getY2());
					else
						start = pos1;
					bools.add(canGetToEnd(start, end, nodes));
				}
				return bools.contains(new Boolean(true));
			}
			return false;
		}
		
	
	public double getWeight(){
		return totalWeight;
	}

}
