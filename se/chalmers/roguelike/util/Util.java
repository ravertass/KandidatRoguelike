package se.chalmers.roguelike.util;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.Position;

public class Util {
	
	/**
	 * Returns a list of all the positions between and including the two input positions.
	 * @param pos0 The First Position.
	 * @param pos1 The Target Position.
	 * @return Returns a list of positions.
	 */
	public static ArrayList<Position> calculateLine(Position pos0, Position pos1) {
		return calculateLine(pos0.getX(), pos0.getY(), pos1.getX(), pos1.getY());
	}
	
	/**
	 * Returns a list of all the positions between and including the two input positions.
	 * @param x0 X value of the First Position.
	 * @param y0 Y value of the First Position.
	 * @param x1 X value of the Second Position.
	 * @param y1 Y value of the Second Position.
	 * @return Returns a list of positions.
	 */
	public static ArrayList<Position> calculateLine(int x0, int y0, int x1, int y1) {
		ArrayList<Position> line = new ArrayList<Position>();
		//Wizardry below
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int x = x0;
		int y = y0;
		int n = 1 + dx + dy;
		int x_inc = (x1 > x0) ? 1 : -1;
		int y_inc = (y1 > y0) ? 1 : -1;
		int error = dx - dy;
		dx *= 2;
		dy *= 2;
		for (; n > 0; --n) {
			line.add(new Position(x, y));

			if (error > 0) {
				x += x_inc;
				error -= dy;
			} else if (error == 0) {
				x += x_inc;
				y += y_inc;
				error += dx;
				error -= dy;
				--n;
			} else {
				y += y_inc;
				error += dx;
			}
		}
		return line;
	}
	
	public static ArrayList<Pair<Integer, Integer>> getDirectionPairs() {
		ArrayList<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer,Integer>>();
		result.add(new Pair<Integer,Integer>(1,1));
		result.add(new Pair<Integer,Integer>(1,-1));
		result.add(new Pair<Integer,Integer>(-1,1));
		result.add(new Pair<Integer,Integer>(-1,-1));
		return result;
	}
	/**
	 * Given a position and a radius this method calculates all the tiles that are inside a circle vid radius radius around the position.
	 * @param p the starting position
	 * @param radius the length of the radius
	 * @return
	 */
	public static ArrayList<Position> circlePositions(Position p, int radius) {
		ArrayList<Position> result = new ArrayList<Position>();
		for(int x = p.getX()-radius; x <= radius*2; x++) {
			for (int y = p.getY()-radius; y <= radius*2; y++) {
				int xlength = Math.abs(x-p.getX());
				int ylength = Math.abs(y-p.getY());
				if(Math.sqrt(xlength^2 + ylength^2) <= radius)
					result.add(new Position(x,y));
			}
		}
		return result;
			
	}
}
