package se.chalmers.roguelike.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

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
		System.out.println(edges);

		while (!(edges.isEmpty())) {
			Edge currentEdge = edges.remove(0);
			if(!createsCyclic(currentEdge)){
				path.add(currentEdge);
				totalWeight += currentEdge.getWeight();
			}
			System.out.println(path);
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
					Position currentStart = start;
					Position pos1 = new Position(edge.getX1(), edge.getY1());
					if(currentStart.equals(pos1))
						currentStart = new Position(edge.getX2(), edge.getY2());
					else
						currentStart = pos1;
					bools.add(canGetToEnd(currentStart, end, nodes));
				}
				return bools.contains(new Boolean(true));
			}
			return false;
		}
		
	
	public double getWeight(){
		return totalWeight;
	}

	public static void main(String[] args){
		KruskalMST krus = new KruskalMST();
		ArrayList<Edge> example = new ArrayList<Edge>();
		example.add(new Edge(0,0,3,3));
		example.add(new Edge(1,2,3,3));
		example.add(new Edge(8,8,3,3));
		example.add(new Edge(8,8,1,2));
		example.add(new Edge(0,0,0,2));		
		example.add(new Edge(0,2,8,8));
		
		System.out.println(krus.createMST(example));
	}
	
}
