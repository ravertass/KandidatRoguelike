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
		return bresenhamLine(pos0.getX(), pos0.getY(), pos1.getX(), pos1.getY());
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
			} else if (error < 0) {
				y += y_inc;
				error += dx;
			} else {
				x += x_inc;
				y += y_inc;
				error += dx;
				error -= dy;
				--n;
			}
		}
		return line;
	}
	
	public static ArrayList<Position> bresenhamLine(int x0, int y0, int x1, int y1){
		ArrayList<Position> templine = new ArrayList<Position>();
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = (x1 < x0) ? 1 : -1;
		int sy = (y1 < y0) ? 1 : -1;

		int err = dx - dy;

		while (true) {
			templine.add(new Position(x1, y1));
		    if (x1 == x0 && y1 == y0) {
		        break;
		    }

		    int e2 = 2 * err;

		    if (e2 > -dy) {
		        err = err - dy;
		        x1 = x1 + sx;
		    }

		    if (e2 < dx) {
		        err = err + dx;
		        y1 = y1 + sy;
		    }
		}
		
		
		
//		int dx = x1-x0;
//		int dy = y1-y0;
//		float error = 0.0f;
//		float derror = 0.0f;
//		if (dx != 0)
//			 derror = Math.abs(dy/dx);
//		else 
//			derror = 0.0f;
//		
//		int y = y0;
//		for (int x = x0; x<=x1; x++ ){
//			line.add(new Position(x,y));
//			error += derror;
//			if (error >= 0.5) {
//				y++;
//				error -= 1.0;
//			}
//		}
		ArrayList<Position> line = new ArrayList<Position>();
		for (Position pos : templine) {
			line.add(0, pos);
		}
		return line;
	}
/*	 function line(x0, x1, y0, y1)
     int deltax := x1 - x0
     int deltay := y1 - y0
     real error := 0
     real deltaerr := abs (deltay / deltax)    // Assume deltax != 0 (line is not vertical),
           // note that this division needs to be done in a way that preserves the fractional part
     int y := y0
     for x from x0 to x1
         plot(x,y)
         error := error + deltaerr
         if error >= 0.5 then
             y := y + 1
             error := error - 1.0 */
	public static ArrayList<Pair<Integer, Integer>> getDirectionPairs() {
		ArrayList<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer,Integer>>();
		result.add(new Pair<Integer,Integer>(1,1));
		result.add(new Pair<Integer,Integer>(1,-1));
		result.add(new Pair<Integer,Integer>(-1,1));
		result.add(new Pair<Integer,Integer>(-1,-1));
		return result;
	}
}
