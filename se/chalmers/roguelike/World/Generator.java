package se.chalmers.roguelike.World;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import se.chalmers.roguelike.Components.Sprite;

public class Generator {
	
	
	private int width=50;
	private int height=50;
	private char[][] worldGrid;
	Random rand = new Random();
	public Generator(){
		run();
	}
	
	private void run(){
		char[][] grid = new char[width][height]; // change to tile later, char atm so we can print it
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				grid[x][y] = ' ';
			}
		}
		ArrayList<Rectangle> rooms = generateRooms(grid);
		connectRooms(grid, rooms);
		//connectRoomsLine(grid, rooms);
		worldGrid = grid;
		for(int x=0;x<width;x++){
			//for(int y=0;y<height;y++){
				System.out.println(grid[x]);
			//}
		}
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
			int randX = rand.nextInt(((int)placedRoom.getWidth()-1))+1+(int)placedRoom.getX();
			int randY = rand.nextInt(((int)placedRoom.getHeight()-1))+1+(int)placedRoom.getY();
			int randRoom = rand.nextInt(placedRooms.size());
			Rectangle roomToConnect = placedRooms.get(randRoom);
			// Get a random spot in the room thats being connected
			int randXnextRoom = rand.nextInt(((int)roomToConnect.getWidth()-1))+1+(int)roomToConnect.getX();
			int randYnextRoom = rand.nextInt(((int)roomToConnect.getHeight()-1))+1+(int)roomToConnect.getY();
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
				grid[randY][randX] = '.';
				// Places walls next to the line
				if(grid[randY][randX-1] == ' '){ // Remember to change to tile later
					grid[randY][randX-1] = 'X';	
				}
				if(grid[randY][randX+1] == ' '){ 
					grid[randY][randX+1] = 'X';
				}

				if(randY<=randYnextRoom){
					randY++;
				} else {
					randY--;
				}
			}
			// Draws line in X-axis
			while(randX!=randXnextRoom && randX > 0 && randX < height-2){
				if(grid[randY-1][randX] == ' '){ // Remember to change to tile later
					grid[randY-1][randX] = 'X';	
				}
				if(grid[randY+1][randX] == ' '){ 
					grid[randY+1][randX] = 'X';
				}

				grid[randY][randX] = '.';
				if(randX<=randXnextRoom){
					randX++;
				} else {
					randX--;
				}
			}
		}
	}
	
	/*
	 * Currently working on, should be a better version
	 */
	public void connectRoomsLine(char[][] grid, ArrayList<Rectangle> placedRooms){
		ArrayList<Rectangle> hallways = new ArrayList<Rectangle>();
		for(Rectangle placedRoom : placedRooms){
			int randX = rand.nextInt(((int)placedRoom.getWidth()-1))+1+(int)placedRoom.getX();
			int randY = rand.nextInt(((int)placedRoom.getHeight()-1))+1+(int)placedRoom.getY();
			int randRoom = rand.nextInt(placedRooms.size());
			Rectangle roomToConnect = placedRooms.get(randRoom);
			// Get a random spot in the room thats being connected
			int randXnextRoom = rand.nextInt(((int)roomToConnect.getWidth()-1))+1+(int)roomToConnect.getX();
			int randYnextRoom = rand.nextInt(((int)roomToConnect.getHeight()-1))+1+(int)roomToConnect.getY();
			
			//Rectangle path = new Rectangle(randX, randY,)
		}
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
					Sprite sprite = new Sprite("brick");
					tiles[x][y] = new Tile(sprite, false, true);
				} else if(worldGrid[y][x] == '.'){
					Sprite sprite = new Sprite("sand");
					tiles[x][y] = new Tile(sprite, true, true);
				}
			}
		}
		return tiles;
		
	}
	public static void main(String[] args) {
		new Generator();
	}
}
