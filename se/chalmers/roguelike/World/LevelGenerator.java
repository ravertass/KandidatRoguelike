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
import se.chalmers.roguelike.Components.BlocksWalking;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.FieldOfView;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.MobType;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;
import se.chalmers.roguelike.util.DelauneyTriangulator;
import se.chalmers.roguelike.util.Edge;
import se.chalmers.roguelike.util.KruskalMST;
import se.chalmers.roguelike.util.Triangle;

public class LevelGenerator {

	// Variables that alter the creation of a dungeon
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
	private ArrayList<Position> treasurePositions = new ArrayList<Position>();

	private ArrayList<Entity> dungeonEntities = new ArrayList<Entity>();
	private String floor;
	private String wall;

	private long seed;

	private Dungeon dungeon;
	private int plotThingY;
	private int plotThingX;

	/**
	 * 
	 * @param seed
	 *            Will be used for a new Random
	 * @param baseAmountOfRooms
	 *            amount of rooms to generate
	 * @param maxRoomSize
	 *            The max size of a room, will be increased by two
	 * @param enoughRoomSize
	 *            The height and width a room should at least have, will be
	 *            decreased by two (walls)
	 * @param corridorDensity
	 *            The percentage of edges that should be re-added after MST
	 */
	public LevelGenerator(long seed, int baseAmountOfRooms, int maxRoomSize,
			int enoughRoomSize, int corridorDensity, int stairProbability,
			String wall, String floor) {
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
		height = 1 + Math.abs(rand.nextInt(amountOfRooms) - 20);
		width = 1 + Math.abs(rand.nextInt(amountOfRooms) - 20);
		run();
	}

	public LevelGenerator(long seed) {
		System.out.println("Using seed: " + seed);
		this.seed = seed;
		rand = new Random(seed);
		amountOfRooms = 10 + rand.nextInt(50);
		generatedRoomSize = 3 + rand.nextInt(10);
		largeEnoughRoom = 2 + rand.nextInt(generatedRoomSize - 2);
		corridorDensity = 5 + rand.nextInt(96);
		height = 1 + Math.abs(rand.nextInt(amountOfRooms) - 20);
		width = 1 + Math.abs(rand.nextInt(amountOfRooms) - 20);
		stairProbability = rand.nextInt(101);
		generateSprites();
		run();
	}

	private void generateSprites() {
		ArrayList<String> walls = new ArrayList<String>();
		walls.add("tiles/brick");
		walls.add("tiles/wall2");
		walls.add("tiles/wallfan");
		walls.add("tiles/wall_red");
		walls.add("tiles/wall_blue");
		wall = walls.get(rand.nextInt(walls.size()));

		ArrayList<String> floors = new ArrayList<String>();
		floors.add("tiles/sand");
		floors.add("tiles/snow");
		floors.add("tiles/snowy_stone");
		floors.add("tiles/stone");
		floors.add("tiles/stone2");
		floors.add("tiles/wood_floor");
		floors.add("tiles/floor_tiled_white");
		floors.add("tiles/floor_tiled_diamond");
		floors.add("tiles/floor2");
		floors.add("tiles/grass");
		floors.add("tiles/grass_djungle");
		floors.add("tiles/noslipfloor");
		floors.add("tiles/ice");
		floors.add("tiles/brown_floor");
		floors.add("tiles/light_brown_floor");
		floors.add("tiles/floor_purple");
		floors.add("tiles/floor_hexagon");
		floors.add("tiles/checkerdfloor");
		floors.add("tiles/floor");
		floors.add("tiles/floor_granite");
		floors.add("tiles/floor_hourglass_yellow");
		floors.add("tiles/floor_tiled_whiteandblack");
		floors.add("tiles/floor_spiral");
		floor = floors.get(rand.nextInt(floors.size()));
	}

	private void run() {
		char[][] grid;

		rooms = placeRooms();
		separateRooms();
		grid = initWorldGrid();

		// For printing purposes
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid[y][x] = ' ';
			}
		}

		ArrayList<Edge> edges = triangulateRooms(grid);

		// Kruskal on the edges
		ArrayList<Edge> minimumSpanning = new KruskalMST().createMST(edges); // =
																				// Kruskal(edges);
		// add 20% of the edges back
		for (Edge edge : edges) {
			if (!minimumSpanning.contains(edge))
				if (rand.nextInt(100) + 1 <= corridorDensity)
					minimumSpanning.add(edge);
		}

		// create corridors from the edges
		drawCorridors(grid, minimumSpanning);
		drawRooms(grid);

		generateEnemies();
		generateUnlockedDoors(grid);
		generateStairs();
		generateTreasurePositions();
		generatePlotThingPosition();

		worldGrid = grid;

		dungeon = toDungeon();
		dungeon.addPlotThingPosition(plotThingX, plotThingY);

		//Generate nextLevel
		if (stairsDown != null){
			LevelGenerator nextLevelGen = new LevelGenerator(seed, (int) (amountOfRooms*0.7), generatedRoomSize, largeEnoughRoom, corridorDensity, stairProbability-20, wall, floor);
			Dungeon nextDungeonLevel = nextLevelGen.getDungeon(); // was toDungeon, would re-created the subdungeon
			dungeon.setNextDungeonLevel(nextDungeonLevel);
			nextDungeonLevel.setPreviousDungeonLevel(dungeon);
			Entity stair = EntityCreator.createStairs(
					stairsDown.getX(), stairsDown.getY(),
					nextDungeonLevel.getStartpos().getX(), 
					nextDungeonLevel.getStartpos().getY(),
					"stairs_down",nextDungeonLevel);
			dungeon.addEntity(stairsDown.getX(), stairsDown.getY(), stair);

			Entity stairUp = EntityCreator.createStairs(nextDungeonLevel.getStartpos().getX(), 
					nextDungeonLevel.getStartpos().getY(),  stairsDown.getX(), stairsDown.getY(),
					"stairs_up", nextDungeonLevel
					.getPreviousDungeonLevel());
			nextDungeonLevel.addEntity(nextDungeonLevel.getStartpos().getX(), nextDungeonLevel.getStartpos().getY(), stairUp);
		} 
		if(dungeon.getPreviousDungeonLevel() == null){
			int x = getStartPos().getX();
			int y = getStartPos().getY();
			Entity stairUp = EntityCreator.createStairs(x, y, -1, -1, "stairs_up",null);
			dungeon.addEntity(x, y, stairUp);
		}

		if(largeRooms.size() == 0)
			run();
	}

	private void generatePlotThingPosition() {
		plotThingX = 0;
		while (plotThingX == 0) {
			for (Rectangle room : largeRooms) {
				//Skip the start room
				if (room == largeRooms.get(0)) {
					continue;
				}

				// Magic number determines the chance
				if (rand.nextInt(10) == 1) {
					plotThingX = room.x + 2 + Math.abs(xMinDisplacement);
					plotThingY = room.y + 2 + Math.abs(yMinDisplacement);
				}
			}
		}
	}

	private void generateEnemies() {
		NameGenerator ng = new NameGenerator(3, seed);
		for (Rectangle room : largeRooms) {
			
			if (room.equals(largeRooms.get(0)))
					continue;
			
			if (rand.nextInt(4) == 0) {
				ArrayList<IComponent> components = new ArrayList<IComponent>();

				String name = ng.generateName();
				String spriteName = "mobs/mob_bat";
				components.add(new MobType(MobType.Type.GRUNT));
				components.add(new Health(10));
				components.add(new TurnsLeft(1));
				components.add(new Input());
				Sprite sprite = new Sprite(spriteName);
				sprite.setLayer(2);
				components.add(sprite);
				components.add(new Inventory()); // TODO add items that the
													// enemy is carrying here,
													// arraylist<entity> inside
													// constructor
				int x = room.x + 2 + Math.abs(xMinDisplacement);
				int y = room.y + 2 + Math.abs(yMinDisplacement);
				components.add(new Position(x, y));
				components.add(new Direction());
				components.add(new AI());
				Attribute attribute = new Attribute(name,
						SpaceClass.SPACE_ROGUE, SpaceRace.SPACE_DWARF, 1, 50);
				components.add(new BlocksWalking(true));
				components.add(new Weapon(2, 6, 0,
						TargetingSystem.SINGLE_TARGET, 1, 1)); // hardcoded
																// equals bad
				components.add(new FieldOfView(8)); // hardcoded equals bad
				components.add(attribute);
				dungeonEntities.add(EntityCreator.createEntity("(Enemy) " + name,
						components));
			}
		}
	}

	private void generateUnlockedDoors(char[][] worldGrid) {

		for (Rectangle room : largeRooms) {
			int x = room.x + Math.abs(xMinDisplacement);
			int y = room.y + Math.abs(yMinDisplacement);
			if (rand.nextInt(100) + 1 <= 80) {

				for (int i = x; i < x + room.width; i++) {
					for (int j = y; j < y + room.height; j++) {
						if ((i == x) || (i == x + room.width - 1) || (j == y)
								|| (j == y + room.height - 1)) {
							if (worldGrid[j][i] != 'X') {
								if (worldGrid[j][i - 1] == 'X'
										&& worldGrid[j][i + 1] == 'X'
										&& worldGrid[j - 1][i] != '-'
										&& worldGrid[j + 1][i] != '-')
									worldGrid[j][i] = '-';
								else if (worldGrid[j - 1][i] == 'X'
										&& worldGrid[j + 1][i] == 'X'
										&& worldGrid[j][i - 1] != '|'
										&& worldGrid[j][i + 1] != '|')
									worldGrid[j][i] = '|';
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Tries to generate a stair with the success rate of stairProbability
	 */

	private void generateStairs() {
		if (rand.nextInt(100) + 1 <= stairProbability) {
			int stairsDownRoom = rand.nextInt(largeRooms.size()-1) + 1;
			Rectangle room = largeRooms.get(stairsDownRoom);
			int x = room.x + 1 + Math.abs(xMinDisplacement)
					+ rand.nextInt(room.width - 2);
			int y = room.y + 1 + Math.abs(yMinDisplacement)
					+ rand.nextInt(room.height - 2);
			stairsDown = new Position(x, y);
		}
	}

	/**
	 * Generates positions for treasures on 5% of the tiles in 20% of the rooms
	 */
	private void generateTreasurePositions() {
		for (Rectangle room : largeRooms) {
			if (rand.nextInt(100) + 1 <= 21) {
				for (int i = 1; i < room.width - 1; i++) {
					for (int j = 1; j < room.height - 1; j++) {
						if (rand.nextInt(100) + 1 <= 6) {
							int x = i + room.x + Math.abs(xMinDisplacement);
							int y = j + room.y + Math.abs(yMinDisplacement);
							treasurePositions.add(new Position(x, y));
						}
					}
				}
			}
		}
	}

	private ArrayList<Edge> triangulateRooms(char[][] grid) {
		// To see where we set nodes
		ArrayList<Position> nodes = generateNodes();
		Collections.sort(nodes);

		DelauneyTriangulator dTriangulator = new DelauneyTriangulator(
				Triangle.getSuperTriangle2(height, width, 0, 0));
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
		return graph;
	}

	private void separateRooms() {
		
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
		}
	}

	private void drawRooms(char[][] worldGrid) {


		// DRAW
		for (Rectangle drawRoom : largeRooms) {
			// Line the sides as walls:
			// new code:
			int roomHeight = (int) drawRoom.getY() + (int) drawRoom.getHeight();
			int roomWidth = (int) drawRoom.getX() + (int) drawRoom.getWidth();
			for (int y = (int) drawRoom.getY(); y < roomHeight; y++) {
				for (int x = (int) drawRoom.getX(); x < roomWidth; x++) {
					if (y == (int) drawRoom.getY() || y == roomHeight - 1
							|| x == (int) drawRoom.getX() || x == roomWidth - 1) {
						if (worldGrid[y + Math.abs(yMinDisplacement)][x
								+ Math.abs(xMinDisplacement)] == ' ')
							worldGrid[y + Math.abs(yMinDisplacement)][x
									+ Math.abs(xMinDisplacement)] = 'X'; // HARDCODED
					} else {
						if (worldGrid[y + Math.abs(yMinDisplacement)][x
								+ Math.abs(xMinDisplacement)] != 'o')
							worldGrid[y + Math.abs(yMinDisplacement)][x
									+ Math.abs(xMinDisplacement)] = '.'; // HARDCODED
					}
				}
			}
		}
	}

	private void drawCorridors(char[][] grid, ArrayList<Edge> minimumSpanning) {
		for (Edge edge : minimumSpanning) {
			ArrayList<Position> line = calculateCorridor(edge);
			line.remove(0);
			line.remove(line.size() - 1);
			for (Position position : line) {

				int x = position.getX() + Math.abs(xMinDisplacement);
				int y = position.getY() + Math.abs(yMinDisplacement);

				grid[y][x] = '.';
				if (rand.nextInt(100) + 1 <= 10) {
					grid[y][x] = 'T';
				}
				//
				for (Rectangle rectangle : rooms) {
					if (rectangle.contains(position.getX(), position.getY())
							&& (!largeRooms.contains(rectangle))) {
						largeRooms.add(rectangle);
					}
				}

				if (grid[y + 1][x] == ' ')
					grid[y + 1][x] = 'X';
				if (grid[y + 1][x + 1] == ' ')
					grid[y + 1][x + 1] = 'X';
				if (grid[y - 1][x] == ' ')
					grid[y - 1][x] = 'X';
				if (grid[y - 1][x + 1] == ' ')
					grid[y - 1][x + 1] = 'X';
				if (grid[y][x + 1] == ' ')
					grid[y][x + 1] = 'X';
				if (grid[y + 1][x - 1] == ' ')
					grid[y + 1][x - 1] = 'X';
				if (grid[y][x - 1] == ' ')
					grid[y][x - 1] = 'X';
				if (grid[y - 1][x - 1] == ' ')
					grid[y - 1][x - 1] = 'X';
			}
		}
	}

	private ArrayList<Position> calculateCorridor(Edge edge) {
		ArrayList<Position> corridor = new ArrayList<Position>();
		int x1 = edge.getX1();
		int y1 = edge.getY1();
		int x2 = edge.getX2();
		int y2 = edge.getY2();
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int x_inc = (x2 > x1) ? 1 : -1;
		int y_inc = (y2 > y1) ? 1 : -1;
		for (int i = 0; i <= dx; i++) {
			corridor.add(new Position(x1 + i * x_inc, y1));
		}
		for (int i = 0; i <= dy; i++) {
			corridor.add(new Position(x1 + dx * x_inc, y1 + i * y_inc));
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
		return worldGrid;
	}

	/**
	 * Generates a room, keep in mind that the way the code is currently written
	 * the room size will be decreased by two in both width and height so that
	 * it will have borders.
	 * 
	 * @return a Rectangle representing a room
	 */
	private Rectangle generateRoom() {
		int height = 5 + rand.nextInt(generatedRoomSize); // will be decreased
															// by two
		int width = 5 + rand.nextInt(generatedRoomSize); // will be decreased by
															// two when
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
					tiles[y][x] = new Tile(new Sprite(wall), false, true);
				} else if (worldGrid[y][x] == '.') {
					tiles[y][x] = new Tile(new Sprite(floor), true, false);

				}
				else if (worldGrid[y][x] == '-') {
					tiles[y][x] = new Tile(new Sprite(floor), true, false);
					Entity door = EntityCreator.createDoor(x,y,"door_horizontal_closed",false);
					dungeonEntities.add(door);
				}
				else if (worldGrid[y][x] == '|') {
					tiles[y][x] = new Tile(new Sprite(floor), true, false);
					Entity door = EntityCreator.createDoor(x,y,"door_vertical_closed",false);
					dungeonEntities.add(door);
				}
				else if (worldGrid[y][x] == 'T') {
					tiles[y][x] = new Tile(new Sprite("mobs/mob_bear"), true, false);
				}
			}
		}
		
		for(Position treasure : treasurePositions){
			Entity gold = EntityCreator.createGold(treasure.getX(),treasure.getY(),100);
			dungeonEntities.add(gold);
		}

		return tiles;
	}

	public Dungeon toDungeon() {
		Dungeon dungeon = new Dungeon();
		Tile[][] tiles = toTiles();
		dungeon.setWorld(tiles[0].length,tiles.length, tiles, getStartPos(), dungeonEntities);
		return dungeon;
	}

	public Dungeon getDungeon() {
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
		System.out
				.println("____________________________________________________");
		for (int y = 0; y < height; y++) {
			System.out.println(worldGrid[y]);
		}
	}

	public Position getStartPos() {
		int x = largeRooms.get(0).x + 1 + Math.abs(xMinDisplacement);
		int y = largeRooms.get(0).y + 1 + Math.abs(yMinDisplacement);
		return new Position(x, y);
	}

}