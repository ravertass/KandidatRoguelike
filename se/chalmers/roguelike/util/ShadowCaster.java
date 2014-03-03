package se.chalmers.roguelike.util;

<<<<<<< .merge_file_ysRNxU
import se.chalmers.roguelike.World.Dungeon;

public class ShadowCaster {
	
	private int startx, starty;
	private float radius;
	
	private int width, height;
	
	private int[][] lightMap;
	private Dungeon dungeon;
	/**
	* Calculates the Field Of View for the provided map from the given x, y
	* coordinates. Returns a lightmap for a result where the values represent a
	* percentage of fully lit.
	*
	* A value equal to or below 0 means that cell is not in the
	* field of view, whereas a value equal to or above 1 means that cell is
	* in the field of view.
	*
	* @param resistanceMap the grid of cells to calculate on where 0 is transparent and 1 is opaque
	* @param startx the horizontal component of the starting location
	* @param starty the vertical component of the starting location
	* @param radius the maximum distance to draw the FOV
	* @param radiusStrategy provides a means to calculate the radius as desired
	* @return the computed light grid
	*/
	public int[][] calculateFOV(Dungeon d, int startx, int starty, float radius) {
	    this.startx = startx;
	    this.starty = starty;
	    this.radius = radius;
	    this.dungeon = d;
	 
	    width = dungeon.getWorldWidth();
	    height = dungeon.getWorldHeight();
	    lightMap = new int[width][height];
	    for(int i = 0; i<width; i++) {
	    	for (int j = 0; j<height; j++)
	    		lightMap[i][j] = 0;
	    }
	    
	    lightMap[startx][starty] = 1;//light the starting cell
	    
	    for (Pair<Integer, Integer> p : Util.getDirectionPairs()) {
	        castLight(1, 1.0f, 0.0f, 0, p.getFirst(), p.getSecond(), 0);
	        castLight(1, 1.0f, 0.0f, p.getFirst(), 0, 0, p.getSecond());
	    }
	 
	    return lightMap;
	}
	 
	private void castLight(int row, float start, float end, int xx, int xy, int yx, int yy) {
	    float newStart = 0.0f;
	    if (start < end) {
	        return;
	    }
	    boolean blocked = false;
	    for (int distance = row; distance <= radius && !blocked; distance++) {
	        int deltaY = -distance;
	        for (int deltaX = -distance; deltaX <= 0; deltaX++) {
	            int currentX = startx + deltaX * xx + deltaY * xy;
	            int currentY = starty + deltaX * yx + deltaY * yy;
	            float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
	            float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
	 
	            if (!(currentX >= 0 && currentY >= 0 && currentX < this.width && currentY < this.height) || start < rightSlope) {
	                continue;
	            } else if (end > leftSlope) {
	                break;
	            }
	 
	            //check if it's within the lightable area and light if needed
//	            if (rStrat.radius(deltaX, deltaY) <= radius) {
//	                float bright = (float) (1 - (rStrat.radius(deltaX, deltaY) / radius));
//	                lightMap[currentX][currentY] = bright;
//	            }
	            lightMap[currentX][currentY] = 1;
	 
	            if (blocked) { //previous cell was a blocking one
	                if (dungeon.getTile(currentX, currentY) != null && dungeon.getTile(currentX, currentY).blocksLineOfSight()) {//hit a wall
	                    newStart = rightSlope;
	                    continue;
	                } else {
	                    blocked = false;
	                    start = newStart;
	                }
	            } else {
	                if (dungeon.getTile(currentX, currentY) != null && dungeon.getTile(currentX, currentY).blocksLineOfSight() && distance < radius) {//hit a wall within sight line
	                    blocked = true;
	                    castLight(distance + 1, start, leftSlope, xx, xy, yx, yy);
	                    newStart = rightSlope;
	                }
	            }
	        }
	    }
	}

}
=======
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
				computeFoVForColumnPortion(current.x, x, y,  current.topVector,
						current.bottomVector, radius, queue, result);
			}
		}
		
		return result;

	}

	private static void computeFoVForColumnPortion(int x, int xcord,int ycord, DirectionVector top,
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
				boolean currentIsOpaque = !inRadius || dungeon.getTile(x+xcord, y+ycord).blocksLineOfSight();
				if(wasLastCellOpaque != -1) {
					if(currentIsOpaque) {
						if(wasLastCellOpaque == 0) {
							System.out.println("hej");
							queue.add(new ColumnPortion(x + 1, new DirectionVector((x+xcord)*2-1, (y+ycord)*2+1),top));
						}
					} else if(wasLastCellOpaque == 1) {
						top = new DirectionVector((x+xcord)*2+1,(y+ycord)*2+1);
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


>>>>>>> .merge_file_oR2R5T
