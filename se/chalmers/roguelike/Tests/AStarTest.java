package se.chalmers.roguelike.Tests;

import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.util.Util;
import se.chalmers.roguelike.util.pathfinding.AStar;
import se.chalmers.roguelike.util.pathfinding.AreaMap;
import se.chalmers.roguelike.util.pathfinding.DiagonalHeuristic;
import se.chalmers.roguelike.util.pathfinding.PathFinder;
import se.chalmers.roguelike.util.pathfinding.PrintMap;

public class AStarTest {

	private static int mapWidth = 20;
	private static int mapHeight = 20;
	private static int[][] obstacleMap = {
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
			{ 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },
			{ 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private static int startX = 0;
	private static int startY = 1;
	private static int goalX = 19;
	private static int goalY = 15;

	public void aStarTest() {
		AreaMap map = new AreaMap(mapWidth, mapHeight, obstacleMap);
		DiagonalHeuristic heuristic = new DiagonalHeuristic();
		AStar aStar = new AStar(map, heuristic);
		ArrayList<Position> shortestPath = aStar.calcShortestPath(startX, startY,
				goalX, goalY);
		new PrintMap(map, shortestPath);
	}

	public void bresenhamsLineTest() {
		Position goal = new Position(19, 0);
		Position start = new Position(0, 19);

		AreaMap map = new AreaMap(mapWidth, mapHeight);

		ArrayList<Position> line = Util.bresenhamLine(goal.getX(), goal.getY(), start.getX(), start.getY());
		StringBuilder str = new StringBuilder("");

		for (Position pos : line) {
			str.append(" (" + pos.getX() + "," + pos.getY() + ")");
			Point point = new Point(pos.getX(), pos.getY());
			map.setObstacle((int) point.getX(), (int) point.getY(), true);
		}
		System.out.println("Bresenham pierces following coords:" + str.toString());
		new PrintMap(map, new ArrayList<Position>());
	}

	public void pathFinderTest() {
		AreaMap map = new AreaMap(mapWidth, mapHeight, obstacleMap);
		map.setStartLocation(startX, startY);
		map.setGoalLocation(goalX, goalY);

		PathFinder pathfinder = new PathFinder();
		ArrayList<Position> optimizedWaypoints = pathfinder.getWaypoints(map);

		new PrintMap(map, optimizedWaypoints);
	}

	public static void main(String[] args) {
		AStarTest aStar = new AStarTest();
//		aStar.aStarTest();
//		aStar.bresenhamsLineTest();
//		aStar.pathFinderTest();
	}
}