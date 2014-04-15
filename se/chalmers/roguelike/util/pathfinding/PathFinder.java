package se.chalmers.roguelike.util.pathfinding;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.util.Util;

/**
 * 
 * @original author svantetobias
 * @original author Mwjuhl
 * @original author janniklind
 * 
 * @link http://code.google.com/p/a-star-java/
 * 
 *       New version is edited for use in this project
 * 
 */
public class PathFinder {

	AreaMap map;

	public ArrayList<Position> getWaypoints(AreaMap map) {
		this.map = map;
		DiagonalHeuristic heuristic = new DiagonalHeuristic();

		AStar aStar = new AStar(map, heuristic);

		// Calc shortest path
		ArrayList<Position> shortestPath = aStar.calcShortestPath(map.getStartLocationX(),
				map.getStartLocationY(), map.getGoalLocationX(), map.getGoalLocationY());

		// Calculating optimized waypoints
		ArrayList<Position> waypoints = calculateWayPoints(shortestPath);
		return waypoints;
	}

	private ArrayList<Position> calculateWayPoints(ArrayList<Position> shortestPath) {
		ArrayList<Position> waypoints = new ArrayList<Position>();

		shortestPath.add(0, map.getStartNode().getPosition());
		shortestPath.add(map.getGoalNode().getPosition());

		Position p1 = shortestPath.get(0);
		int p1Number = 0;
		waypoints.add(p1);

		Position p2 = shortestPath.get(1);
		int p2Number = 1;

		while (!p2.equals(shortestPath.get(shortestPath.size() - 1))) {
			if (lineClear(p1, p2)) {
				// make p2 the next point in the path
				p2Number++;
				p2 = shortestPath.get(p2Number);
			} else {
				p1Number = p2Number - 1;
				p1 = shortestPath.get(p1Number);
				waypoints.add(p1);
				p2Number++;
				p2 = shortestPath.get(p2Number);
			}
		}
		waypoints.add(p2);
		return waypoints;
	}

	private boolean lineClear(Position a, Position b) {
		ArrayList<Position> pointsOnLine = Util.bresenhamLine((int) a.getX(), (int) a.getY(), (int) b.getX(),
				(int) b.getY());
		for (Position p : pointsOnLine) {
			if (map.getNode((int) p.getX(), (int) p.getY()).isObstacle) {
				return false;
			}
		}
		return true;
	}
}