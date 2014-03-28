package se.chalmers.roguelike.util.pathfinding;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.Position;

/**
 * 
 * @original author svantetobias
 * @original author Mwjuhl
 * @original author janniklind
 * 
 * @link http://code.google.com/p/a-star-java/
 * 
 * New version is edited for use in this project
 *       
 * This class prints a graphic interpretation of AreaMaps with obstacles in it
 */
public class PrintMap {

	public PrintMap(AreaMap map, ArrayList<Position> shortestPath) {
		Node node;
		if (shortestPath == null){
			System.err.println("No path was found; either the path is non-existent or it is temporarily blocked.");
			return;
		}
		for (int y = 0; y < map.getMapHeight(); y++) {

			if (y == 0) {
				for (int i = 0; i <= map.getMapHeight(); i++)
					System.out.print("-");
				System.out.println();
			}
			System.out.print("|");

			for (int x = 0; x < map.getMapWidth(); x++) {
				node = map.getNode(x, y);

				if (node.isObstacle) {
					System.out.print("X");
				} else if (node.isStart) {
					System.out.print("s");
				} else if (node.isGoal) {
					System.out.print("g");
				} else if (shortestPath.contains(new Position(node.getX(), node.getY()))) {
					System.out.print("¤");
				} else {
					System.out.print(" ");
				}
				if (y == map.getMapHeight())
					System.out.print("_");
			}

			System.out.print("|");
			System.out.println();
		}
		for (int i = 0; i <= map.getMapHeight(); i++)
			System.out.print("-");
	}
}