package se.chalmers.roguelike.World;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import se.chalmers.roguelike.Components.Sprite;

public class Generator {
	
	
	private int width=50;
	private int height=50;
	private char[][] worldGrid;
	Random rand; // replace with new Random(seed); later, already tried and works
	public Generator(){

		Random seedRand = new Random();
		long seed = seedRand.nextLong();
//		long seed = 3182815830558287750L;
		System.out.println("Using seed: "+seed);
		rand = new Random(seed);
		run();
	}
	
	private void run(){
		char[][] grid = new char[width][height]; // change to tile later, char atm so we can print it
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				grid[y][x] = ' ';
			}
		}
		ArrayList<Rectangle> rooms = generateRooms(grid);
		connectRooms(grid, rooms);
		worldGrid = grid;
		/*
		for(int x=0;x<width;x++){
			//for(int y=0;y<height;y++){
				System.out.println(grid[x]);
			//}
		}*/
	}
	
	/**
	 * Generates a room, keep in mind that the way the code is current written 
	 * (which will change) the room size will be decreates by two in both
	 * width and height so that it will have borders.
	 * 
	 * @return a Rectangle representing a room
	 */
	private Rectangle generateRoom(){
		int height = 5+rand.nextInt(7); // will be decreased by two
		int width = 5+rand.nextInt(7); // will be decreased by two when rendered
		Rectangle newRoom = new Rectangle(width,height);
		return newRoom;
	}
	
	/**
	 * Generates the rooms and places them on the grid
	 * 
	 * @param grid to place the rooms on
	 * @return a list of the rooms that have been placed
	 */
	private ArrayList<Rectangle> generateRooms(char[][] grid){
		Rectangle room = generateRoom();
		ArrayList<Rectangle> placedRooms = new ArrayList<Rectangle>();
		ArrayList<Rectangle> bufferedSpace = new ArrayList<Rectangle>(); // includes the area for placedRooms as well as some border area
		for(int y=0;y<height;y++){ // starts from one to preserve the border
			for(int x=0;x<width;x++){
				room.setLocation(x,y);
				boolean placeRoom = true;
				for(Rectangle placedRoom : bufferedSpace){
					placeRoom = placeRoom & !room.intersects(placedRoom) 
							& (room.getWidth()+room.getX() < width)
							& (room.getHeight()+room.getY() < height);
				}
				if(placeRoom){

					room.setLocation(x,y);
					placedRooms.add(room);
					Rectangle bufferRoom = new Rectangle(room);
					int bufferX = rand.nextInt(4)+1;
					int bufferY = rand.nextInt(4)+1;
					bufferRoom.setSize((int)room.getWidth()+bufferX,(int)room.getHeight()+bufferY);
					bufferedSpace.add(bufferRoom);
					//System.out.println("Buffer room width:"+bufferRoom.getWidth());
					//x+=room.getWidth()+rand.nextInt(5)+1;
					x+=bufferRoom.getWidth();
					room = generateRoom();
				}
			}
		}
		
		// Places the rooms
		for(Rectangle drawRoom : placedRooms){
			// Line the sides as walls:
			// new code:
			int roomHeight = (int)drawRoom.getY()+(int)drawRoom.getHeight();
			int roomWidth = (int)drawRoom.getX()+(int)drawRoom.getWidth();
			for(int y=(int)drawRoom.getY(); y<roomHeight;y++){
				for(int x=(int)drawRoom.getX(); x<roomWidth;x++){
					if(y==(int)drawRoom.getY() || y==roomHeight-1 ||
							x==(int)drawRoom.getX() || x==roomWidth-1){
						grid[y][x] = 'X';
					} else {
						grid[y][x] = '.';
					}
				}
			}
		}
		return placedRooms;
		
	}
	
	
	/**
	 * Makes straight connections between random rooms.
	 * @param grid game world
	 * @param placedRooms rooms that should be connected.
	 */
	public void connectRooms(char[][] grid, ArrayList<Rectangle> placedRooms){
		// Connect the rooms:
		for(Rectangle placedRoom : placedRooms){
			// Get a random spot in the room, reason for -1 is to remove the border,
			// and +1 is outside of the RNG to make sure it doesn't start on the border
			int randX = rand.nextInt(((int)placedRoom.getWidth()-2))+1+(int)placedRoom.getX();
			int randY = rand.nextInt(((int)placedRoom.getHeight()-2))+1+(int)placedRoom.getY();
			int randRoom = rand.nextInt(placedRooms.size());
			Rectangle roomToConnect = placedRooms.get(randRoom);
			// Get a random spot in the room thats being connected
			int randXnextRoom = rand.nextInt(((int)roomToConnect.getWidth()-2))+1+(int)roomToConnect.getX();
			int randYnextRoom = rand.nextInt(((int)roomToConnect.getHeight()-2))+1+(int)roomToConnect.getY();
			// The following two if-cases runs -1 because of making sure that the
			// array doesn't go out of bounds, and -1 to add a border
			if(randX > width-2){
				randX=width-2;
			} 
			if(randY > height-2){
				randY=height-2;
			}
			// Draws line in Y-axis
			while(randY!=randYnextRoom && randY > 0 && randY < width-2){
				drawWallsAround(grid,randX,randY);
				if(randY<=randYnextRoom){
					randY++;
				} else {
					randY--;
				}
			}
			// Draws line in X-axis
			while(randX!=randXnextRoom && randX > 0 && randX < height-2){
				drawWallsAround(grid,randX,randY);
				if(randX<=randXnextRoom){
					randX++;
				} else {
					randX--;
				}
			}
		}
	}
	
	/**
	 * Puts down a floor tile at the coordinates and places walls around it
	 * @param grid world grid
	 * @param x x coordinate for floor tile
	 * @param y y coordinate for floor tile
	 */
	private void drawWallsAround(char[][] grid, int x, int y){
		// Remember to change to tile later
		if(grid[y-1][x] == ' '){ 
			grid[y-1][x] = 'X';	// above  (in test, above ingame)
		}
		if(grid[y+1][x] == ' '){ 
			grid[y+1][x] = 'X'; // below (in test, above ingame)
		}
		if(grid[y][x+1] == ' '){ 
			grid[y][x+1] = 'X'; // to the right
		}
		if(grid[y][x-1] == ' '){ 
			grid[y][x-1] = 'X'; // to the left
		}
		if(grid[y-1][x-1] == ' '){ 
			grid[y-1][x-1] = 'X'; // NW
		}
		if(grid[y+1][x-1] == ' '){ 
			grid[y+1][x-1] = 'X'; // SW
		}
		if(grid[y-1][x+1] == ' '){ 
			grid[y-1][x+1] = 'X'; // NE
		}
		if(grid[y+1][x+1] == ' '){ 
			grid[y+1][x+1] = 'X'; // SE
		}
		grid[y][x] = '.';
	}

	
	/**
	 * Gives a world made up in tiles
	 * @return Tile[][] with the game world
	 */
	// In the future, do all in tiles directly instead of translating
	public Tile[][] toTiles(){
		Tile[][] tiles = new Tile[height][width];
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){

				if(worldGrid[y][x] == 'X'){
					if(x==18 && y==18){
						System.out.println("Bugging tile: "+worldGrid[y][x]);
					}
//					Sprite sprite = new Sprite("brick");
					tiles[y][x] = new Tile(new Sprite("brick"), false, true);
				} else if(worldGrid[y][x] == '.'){
//					Sprite sprite = new Sprite("sand");
					tiles[y][x] = new Tile(new Sprite("sand"), true, false);
				}
			}
		}
		return tiles;
		
	}
	public void print(){
		for(int y=0;y<height;y++){
			System.out.println(worldGrid[y]);
		}
	}
	public static void main(String[] args) {
		new Generator().print();
	}
}
