package se.chalmers.roguelike.util;

import java.util.ArrayList;
import java.util.PriorityQueue;

import se.chalmers.roguelike.World.Dungeon;

public class ShadowCaster {

	private static Dungeon dungeon;

	public ShadowCaster() {
		// TODO
	}

	public static ArrayList<Pair<Integer,Integer>> ComputeFieldOfViewWithShadowCasting(int x, int y,
			int radius, Dungeon d) {
		dungeon = d;
		PriorityQueue<ColumnPortion> queue = new PriorityQueue<ColumnPortion>();
		queue.add(new ColumnPortion(0, new DirectionVector(1, 0),
				new DirectionVector(1, 1)));
		ArrayList<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();
		while (!queue.isEmpty()) {
			ColumnPortion current = queue.poll();
			if (current.x <= radius) {
				computeFoVForColumnPortion(current.x, current.topVector,
						current.bottomVector, radius, queue, result);
			}
		}
		
		return result;

	}

	private static void computeFoVForColumnPortion(int x, DirectionVector top,
			DirectionVector bottom, int radius,
			PriorityQueue<ColumnPortion> queue, ArrayList<Pair<Integer, Integer>> result) {
			int topY, bottomY;
			int remainder = ((2 * x + 1) * top.y) % (2 * top.x);
			if(remainder <= top.x)
				topY = ((2 * x + 1) * top.y) / (2 * top.x);
			else 
				topY = ((2 * x + 1) * top.y) / (2 * top.x) + 1;
			remainder = ((2 * x - 1) * bottom.y) % (2 * bottom.x);
			if(remainder >= bottom.x)
				bottomY = ((2 * x + 1) * top.y) / (2 * top.x);
			else 
				bottomY = ((2 * x + 1) * top.y) / (2 * top.x) - 1;
	
			int wasLastCellOpaque = -1;
			for (int y = topY; y >= bottomY; y--) {
				boolean inRadius = inRadius(x,y,radius);
				if(inRadius) {
					result.add(new Pair<Integer, Integer>(x, y));
				}
				System.out.println(x + " + " +y);
				boolean currentIsOpaque = !inRadius || dungeon.getTile(x, y).blocksLineOfSight();
				if(wasLastCellOpaque != -1) {
					if(currentIsOpaque) {
						if(wasLastCellOpaque == 0) {
							queue.add(new ColumnPortion(x + 1, new DirectionVector(x*2-1, y*2+1),top));
						}
					} else if(wasLastCellOpaque == 1) {
						top = new DirectionVector(x*2+1,y*2+1);
					}
				}
				wasLastCellOpaque = currentIsOpaque ? 1 : 0;
			}
			

		
	}
	
	private static boolean inRadius(int x, int y, int radius) {
		return true; //TODO här kan vi lägga till sedan om man inte vill ha oändligt synfält
	}
	

	private static class ColumnPortion {
		public int x;
		public DirectionVector bottomVector;
		public DirectionVector topVector;

		public ColumnPortion(int x, DirectionVector bottom, DirectionVector top) {
			this.x = x;
			this.bottomVector = bottom;
			this.topVector = top;
		}

	}

	private static class DirectionVector {
		public int x;
		public int y;

		public DirectionVector(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}


