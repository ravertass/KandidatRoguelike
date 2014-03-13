package se.chalmers.roguelike.util;

import se.chalmers.roguelike.Components.Position;

public class Edge implements Comparable<Edge> {

	int x1;
	int y1;
	int x2;
	int y2;

	public Edge(int x1, int y1, int x2, int y2) {

		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public Edge(float[] trianglePoint1, float[] trianglePoint2) {
		this.x1 = (int) trianglePoint1[0];
		this.y1 = (int) trianglePoint1[1];
		this.x2 = (int) trianglePoint2[0];
		this.y2 = (int) trianglePoint2[1];
	}
	
	public int getX1(){
		return x1;
	}
	public int getY1(){
		return y1;
	}
	public int getX2(){
		return x2;
	}
	public int getY2(){
		return y2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (compareTo(other) != 0)
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Edge edge) {
		if ((this.x1 == edge.x1) && (this.y1 == edge.y1) && (this.x2 == edge.x2) && (this.y2 == edge.y2))
			return 0;
		else if ((this.x1 == edge.x2) && (this.y1 == edge.y2) && (this.x2 == edge.x1) && (this.y2 == edge.y1))
			return 0;
		else if (this.x1 < edge.x1)
			return -1;
		else 
			return 1;
	}
	
	@Override
	public String toString() {
		return ("("+x1+","+y1+")("+x2+","+y2+")");
	}
}
