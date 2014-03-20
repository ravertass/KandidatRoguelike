package se.chalmers.roguelike.World;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import se.chalmers.plotgen.NameGen.NameGenerator;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.util.DelauneyTriangulator;
import se.chalmers.roguelike.util.Edge;
import se.chalmers.roguelike.util.KruskalMST;
import se.chalmers.roguelike.util.Triangle;

public class LevelGenerator {

	//Variables that alter the creation of a dungeon
	private int amountOfRooms;
	private int width;
	private int height;
	private int largeEnoughRoom;
	private int generatedRoomSize;
	private int corridorDensity;
	
	private int xMinDisplacement = 0, yMinDisplacement = 0;
	private char[][] worldGrid;
	private ArrayList<Rectangle> rooms;
	private ArrayList<Rectangle> largeRooms = new ArrayList<Rectangle>();
	Random rand;
	
	private int stairProbability;
	private Position stairsDown = null;
	
	private String floor;
	private String wall;
	
	private long seed;
	private ArrayList<Entity> enemies;
	
	private Dungeon dungeon;
	/**
	 * 
	 * @param seed					Will be used for a new Random
	 * @param baseAmountOfRooms		amount of rooms to generate
	 * @param maxRoomSize			The max size of a room, will be increased by two
	 * @param enoughRoomSize 		The height and width a room should at least have, will be decreased by two (walls) 
	 * @param corridorDensity		The percentage of edges that should be re-added after MST
	 */
	public LevelGenerator(long seed, int baseAmountOfRooms, int maxRoomSize, int enoughRoomSize, int corridorDensity, int stairProbability, String wall, String floor) {
		System.out.println("Using seed: " + seed);
		this.seed = seed;
		rand = new Random(seed);
		amountOfRooms = baseAmountOfRooms;
		generatedRoomSize = maxRoomSize;
		largeEnoughRoom = enoughRoomSize;
		this.corridorDensity = corridorDensity;
		this.stairProbability = stairProbability;
		this.wall = wall;
		this.floor = floor;
		height = 1 + Math.abs(rand.nextInt(amountOfRooms)-20);
		width = 1 + Math.abs(rand.nextInt(amountOfRooms)-20);
		run();
	}

	public LevelGenerator(long seed) { 
		System.out.println("Using seed: " + seed);
		this.seed = seed;
		rand = new Random(seed);	
		amountOfRooms = 10 + rand.nextInt(50);
		generatedRoomSize = 3 + rand.nextInt(10);
		largeEnoughRoom = 2 + rand.nextInt(generatedRoomSize-2);
		corridorDensity = 5 + rand.nextInt(96);
		height = 1 + Math.abs(rand.nextInt(amountOfRooms)-20);
		width = 1 + Math.abs(rand.nextInt(amountOfRooms)-20);
		stairProbability = rand.nextInt(101);
		generateSprites();
		run();
	}
	
	private void generateSprites(){
		ArrayList<String> walls = new ArrayList<String>();
		walls.add("brick");
		walls.add("wall2");
		walls.add("wallfan");
		walls.add("wall_red");
		walls.add("wall_blue");
		wall = walls.get(rand.nextInt(walls.size()));
		
		ArrayList<String> floors = new ArrayList<String>();
		floors.add("sand");
		floors.add("snow");
		floors.add("snowy_stone");
		floors.add("stone");
		floors.add("stone2");
		floors.add("wood_floor");
		floors.add("floor_tiled_whiteandblack");
		floors.add("floor_tiled_diamond");
		floors.add("floor2");
		floors.add("grass");
		floors.add("grass_djungle");
		floors.add("noslipfloor");
		floors.add("ice");
		floors.add("brown_floor");
		floors.add("light_brown_floor");
		floors.add("floor_purple");
		
		
		floor = floors.get(rand.nextInt(floors.size()));
	}
	private void run() {
		char[][] grid;

		rooms = placeRooms();
		separateRooms();
		grid = initWorldGrid();

		//For printing purposes
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid[y][x] = ' ';
			}
		}
		
		ArrayList<Edge> edges = triangulateRooms(grid);
		
		//Kruskal on the edges
		ArrayList<Edge> minimumSpanning = new KruskalMST().createMST(edges); //= Kruskal(edges);
		// add 20% of the edges back
		for (Edge edge : edges) {
			if (!minimumSpanning.contains(edge))
				if(rand.nextInt(100)+1 <= corridorDensity)
					minimumSpanning.add(edge);
		}
		
		// create corridors from the edges
		drawCorridors(grid, minimumSpanning);
		drawRooms(grid);
		
		generateEnemies();
		generateStairs();
		
		
		
		print(grid);
		worldGrid = grid;
		
		dungeon  = toDungeon();
		//Generate nextLevel
		if (stairsDown != null){
			LevelGenerator nextLevelGen = new LevelGenerator(seed, (int) (amountOfRooms*0.7), generatedRoomSize, largeEnoughRoom, corridorDensity, stairProbability-20, wall, floor);
			Dungeon nextDungeonLevel = nextLevelGen.toDungeon();
			dungeon.setNextDungeonLevel(nextDungeonLevel);
			nextDungeonLevel.setPreviousDungeonLevel(dungeon);

			System.out.println("Created Subdungeon");
		}
		
		if(largeRooms.size() == 0)
			run();
	}

	private void generateEnemies() {
		enemies = new ArrayList<Entity>();
		NameGenerator ng = new NameGenerator(3, seed);
		for (Rectangle room : largeRooms) {
			if(rand.nextInt(4) == 0) {
				ArrayList<IComponent> components = new ArrayList<IComponent>();
				
				String name = ng.generateName();
				String sprite = "mobs/mob_blue";
				components.add(new Health(10));
				components.add(new TurnsLeft(1));
				components.add(new Input());
				components.add(new Sprite(sprite));
				int x = room.x + 2 + Math.abs(xMinDisplacement);
				int y = room.y + 2 + Math.abs(yMinDisplacement);
				components.add(new Position(x,y));
				components.add(new Direction());
				components.add(new AI());
				Attribute attribute = new Attribute(name, SpaceClass.SPACE_ROGUE, SpaceRace.SPACE_DWARF, 1, 50);
				components.add(attribute);
				enemies.add(EntityCreator.createEntity("(Enemy)" + name, components));
			}
		}
		System.out.println(enemies);
		
	}
	private void generateStairs(){
		if (rand.nextInt(100)+1 <= stairProbability){
			int stairsDownRoom = rand.nextInt(largeRooms.size() + 1);
			Rectangle room = largeRooms.get(stairsDownRoom);
			int x = room.x + 1 + Math.abs(xMinDisplacement) + rand.nextInt(room.width - 2);
			int y = room.y + 1 + Math.abs(yMinDisplacement) + rand.nextInt(room.height - 2);
			stairsDown = new Position(x, y);
		}
	}
	
	private ArrayList<Edge> triangulateRooms(char[][] grid) {
		// To see where we set nodes
		ArrayList<Position> nodes = generateNodes();
//		for (Position point : nodes) {
//			grid[point.getY() + Math.abs(yMinDisplacement)][point.getX()
//					+ Math.abs(xMinDisplacement)] = 'o';
//		}
		// Sort the list of nodes (sorts by X-value, low to high)
		Collections.sort(nodes);
		
		DelauneyTriangulator dTriangulator = new DelauneyTriangulator(Triangle.getSuperTriangle2(height, width, 0, 0));
		return dTriangulator.triangulate(nodes);
	}
	
	/**
	 * 
	 * 
	 * @param grid
	 * @return
	 */
	private ArrayList<Rectangle> placeRooms() {
		ArrayList<Rectangle> graph = new ArrayList<Rectangle>();
		int x, y;
		Rectangle room;
		for (int i = 0; i < amountOfRooms; i++) {
			x = rand.nextInt(width);
			y = rand.nextInt(height);
			room = generateRoom();
			room.setLocation(x, y);
			graph.add(room);
			if (room.width >= largeEnoughRoom && room.height >= largeEnoughRoom) {
				largeRooms.add(room);
			}
		}
		// Stats for how the room size is distributed
		System.out.println("Amount of large rooms: " + largeRooms.size()
				+ "\nAmount of small rooms: "
				+ (amountOfRooms - largeRooms.size()));
		return graph;
	}

	private void separateRooms() {
		int iterations = 0;
		boolean intersectingRooms = true;
		while (intersectingRooms) {
			intersectingRooms = false;
			for (Rectangle rectangle : rooms) {
				for (Rectangle colliding : rooms) {
					if (rectangle != colliding
							&& rectangle.intersects(colliding)) {
						intersectingRooms = true;

						int random = rand.nextInt(2);
						if (random == 0) {
							if (rectangle.getX() >= colliding.getX()) {
								colliding.x--;
							} else {
								colliding.x++;
							}
						} else {
							if (rectangle.getY() >= colliding.getY()) {
								colliding.y--;
							} else {
								colliding.y++;
							}
						}
					}
				}
			}
			iterations++;
		}
		System.out.println("Amount of iterations: " + iterations);
	}

	private void drawRooms(char[][] worldGrid) {

		// Clear the array
//		for (int x = 0; x < width; x++) {
//			for (int y = 0; y < height; y++) {
//				worldGrid[y][x] = ' ';
//			}
//		}

		// DRAW
		for (Rectangle drawRoom : largeRooms) {
			// Line the sides as walls:
			// new code:
			int roomHeight = (int) drawRoom.getY() + (int) drawRoom.getHeight();
			int roomWidth = (int) drawRoom.getX() + (int) drawRoom.getWidth();
			for (int y = (int) drawRoom.getY(); y < roomHeight; y++) {
				for (int x = (int) drawRoom.getX(); x < roomWidth; x++) {
					if (y == (int) drawRoom.getY() || y == roomHeight - 1 || x == (int) drawRoom.getX() || x == roomWidth - 1) {
						if(worldGrid[y + Math.abs(yMinDisplacement)][x + Math.abs(xMinDisplacement)] != '.')
							worldGrid[y + Math.abs(yMinDisplacement)][x + Math.abs(xMinDisplacement)] = 'X'; // HARDCODED
					} else {
						if(worldGrid[y + Math.abs(yMinDisplacement)][x + Math.abs(xMinDisplacement)] != 'o')
							worldGrid[y + Math.abs(yMinDisplacement)][x + Math.abs(xMinDisplacement)] = '.'; // HARDCODED
					}
				}
			}
		}
	}

	private void drawCorridors(char[][] grid, ArrayList<Edge> minimumSpanning) {
		for (Edge edge : minimumSpanning) {
			ArrayList<Position> line = calculateCorridor(edge);
			line.remove(0);
			line.remove(line.size()-1);
			for (Position position : line) {
				
				int x = position.getX() + Math.abs(xMinDisplacement);
				int y = position.getY() + Math.abs(yMinDisplacement);
				grid[y][x] = '.';
				for (Rectangle rectangle : rooms) {
					if (rectangle.contains(position.getX(), position.getY()) && (!largeRooms.contains(rectangle))) {
						largeRooms.add(rectangle);
					}
				}
				
				if (grid[y+1][x] == ' ') 
					grid[y+1][x] = 'X';
				if (grid[y+1][x+1] == ' ') 
					grid[y+1][x+1] = 'X';
				if (grid[y-1][x] == ' ') 
					grid[y-1][x] = 'X';
				if (grid[y-1][x+1] == ' ') 
					grid[y-1][x+1] = 'X';
				if (grid[y][x+1] == ' ') 
					grid[y][x+1] = 'X';
				if (grid[y+1][x-1] == ' ') 
					grid[y+1][x-1] = 'X';
				if (grid[y][x-1] == ' ') 
					grid[y][x-1] = 'X';
				if (grid[y-1][x-1] == ' ') 
					grid[y-1][x-1] = 'X';
			}
		}
	}
	
	private ArrayList<Position> calculateCorridor (Edge edge) {
		ArrayList<Position> corridor = new ArrayList<Position>();
		int x1 = edge.getX1();
		int y1 = edge.getY1();
		int x2 = edge.getX2();
		int y2 = edge.getY2();
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int x_inc = (x2 > x1) ? 1 : -1;
		int y_inc = (y2 > y1) ? 1 : -1;
		for (int i = 0; i <=dx; i++){
			corridor.add(new Position(x1 + i*x_inc, y1));
		}
		for (int i = 0; i <=dy; i++){
			corridor.add(new Position(x1 + dx*x_inc, y1 + i*y_inc));
		}
		return corridor;
	}
	
	private char[][] initWorldGrid() {
		char[][] worldGrid;
		int yMaxDisplacement = 0;
		int xMaxDisplacement = 0;
		// Move the indices for the array to prevent OutOfBounds
		for (Rectangle room : rooms) {
			if (room.getX() < xMinDisplacement)
				xMinDisplacement = (int) room.getX();
			if (room.getY() < yMinDisplacement)
				yMinDisplacement = (int) room.getY();
			if (room.getX() + room.width > xMaxDisplacement)
				xMaxDisplacement = (int) room.getX() + room.width;
			if (room.getY() + room.height > yMaxDisplacement)
				yMaxDisplacement = (int) room.getY() + room.height;
		}
		yMaxDisplacement += Math.abs(yMinDisplacement);
		xMaxDisplacement += Math.abs(xMinDisplacement);
		worldGrid = new char[yMaxDisplacement][xMaxDisplacement];
		height = yMaxDisplacement;
		width = xMaxDisplacement;
		System.out.println("Size of the world: " + width + "x" + height);
		return worldGrid;
	}

	/**
	 * Generates a room, keep in mind that the way the code is currently written
	 * the room size will be decreased by two in both width
	 * and height so that it will have borders.
	 * 
	 * @return a Rectangle representing a room
	 */
	private Rectangle generateRoom() {
		int height = 5 + rand.nextInt(generatedRoomSize); // will be decreased by two
		int width = 5 + rand.nextInt(generatedRoomSize); // will be decreased by two when
											// rendered
		Rectangle newRoom = new Rectangle(width, height);
		return newRoom;
	}

	/**
	 * Gives a world made up in tiles
	 * 
	 * @return Tile[][] with the game world
	 */
	// In the future, do all in tiles directly instead of translating
	public Tile[][] toTiles(char[][] worldGrid) {
		
		Tile[][] tiles = new Tile[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				if (worldGrid[y][x] == 'X') {
					if (x == 18 && y == 18) {
						System.out.println("Bugging tile: " + worldGrid[y][x]);
					}
					tiles[y][x] = new Tile(new Sprite(wall), false, true);
				} else if (worldGrid[y][x] == '.') {
					tiles[y][x] = new Tile(new Sprite(floor), true, false);
				}
			}
		}
		
		// TODO: instead of just change the sprite of the tile, add a stairEntity?
		tiles[getStartPos().getY()][getStartPos().getX()].getSprite().setSpritesheet("stairs_up");
		if(stairsDown !=null)
			tiles[stairsDown.getY()][stairsDown.getX()].getSprite().setSpritesheet("stairs_down");
		
		return tiles;
	}
	
	public Dungeon toDungeon() {
		Dungeon dungeon = new Dungeon();
		Tile[][] tiles = toTiles();
		dungeon.setWorld(tiles[0].length,tiles.length, tiles, getStartPos(), enemies);
		
		return dungeon;
	}
	
	public Dungeon getDungeon(){
		return dungeon;
	}
	
	public Tile[][] toTiles() {
		return toTiles(worldGrid);
	}

	public ArrayList<Position> generateNodes() {
		ArrayList<Position> nodes = new ArrayList<Position>();
		for (Rectangle room : largeRooms) {
			int x = room.x + 1 + rand.nextInt(room.width - 2);
			int y = room.y + 1 + rand.nextInt(room.height - 2);
			Position node = new Position(x, y);
			nodes.add(node);
		}
		return nodes;
	}

	public void print(char[][] worldGrid) {
		System.out.println("____________________________________________________");
		for (int y = 0; y < height; y++) {
			System.out.println(worldGrid[y]);
		}
	}
	
	public Position getStartPos() {
		int x = largeRooms.get(0).x + 1 + Math.abs(xMinDisplacement);
		int y = largeRooms.get(0).y + 1 + Math.abs(yMinDisplacement);
		return new Position(x,y);
	}
	
//	public static void main(String[] args) {
//		new LevelGenerator(123456789L, 100, 8, 7, 20);
//	}

}