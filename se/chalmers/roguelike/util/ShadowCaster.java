package se.chalmers.roguelike.util;

import se.chalmers.roguelike.World.Dungeon;

public class ShadowCaster {
	
	private int startx, starty;
	private float radius;
	
	private int width, height;
	
	private int[][] lightMap;
	private Dungeon dungeon;
	/**
	* Calculates the Field Of View for the provided map from the given x, y
	* coordinates. Returns a lightmap for a result where 1 is lit and 0 dark.
	* A value equal to or below 0 means that cell is not in the
	* field of view, whereas a value equal to or above 1 means that cell is
	* in the field of view.
	*
	* @param d the dungeon, used for checking if a certain tile blocks line of sight
	* @param startx the horizontal component of the starting location
	* @param starty the vertical component of the starting location
	* @param radius the maximum distance to draw the FOV
	* @param radiusStrategy provides a means to calculate the radius as desired //TODO
	* @return the grid of tiles to draw
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
	/**
	 * Helper method for calculating FOV, casts light between two blocks.
	 * @param row 
	 * @param start
	 * @param end
	 * @param xx
	 * @param xy
	 * @param yx
	 * @param yy
	 */
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
	            //TODO implement radius here
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

