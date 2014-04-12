package se.chalmers.roguelike.World;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import se.chalmers.plotgen.NameGen.NameGenerator;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Attribute;
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
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;

public class CellularLevelGenerator {

	private int width;
	private int height;
	private Random rand;
	private int cutoff = 5;
	private int cutoff2 = 2;
	private Dungeon dungeon;
	private char[][] worldGrid;
	private ArrayList<Entity> dungeonEntities = new ArrayList<Entity>();
	private Position startPos;
	private ArrayList<Position> cave;
	private long seed;
	private Position stairsDown = null;

	private String wall = "wall2";
	private String floor = "sand";

	private int numberOfSpawnPoints = 0;

	public CellularLevelGenerator(int width, int height, long seed) {
		this.height = height;
		this.width = width;
		this.seed = seed;
		this.rand = new Random(seed);
		run();
	}

	public void run() {
		worldGrid = new char[height][width];

		initGrid(worldGrid);
		for (int i = 0; i < 5; i++) {
			// print(worldGrid);
			generation(worldGrid);
		}
		// print(worldGrid);
		findPockets();
		generateStartPosition();
		generateEntities();
		generateStairs();
		// print(worldGrid);

		dungeon = toDungeon();

		// Generate nextLevel
		if (stairsDown != null) {

			CellularLevelGenerator nextLevelGen = new CellularLevelGenerator(width - 10, height - 10, seed);
			Dungeon nextDungeonLevel = nextLevelGen.getDungeon(); // was
																	// toDungeon,
																	// would
																	// re-created
																	// the
																	// subdungeon
			dungeon.setNextDungeonLevel(nextDungeonLevel);
			nextDungeonLevel.setPreviousDungeonLevel(dungeon);
			Entity stair = EntityCreator.createStairs(stairsDown.getX(), stairsDown.getY(), nextDungeonLevel
					.getStartpos().getX(), nextDungeonLevel.getStartpos().getY(), "stairs_down",
					nextDungeonLevel);
			dungeon.addEntity(stairsDown.getX(), stairsDown.getY(), stair);
			// System.out.println("Created Subdungeon");
			Entity stairUp = EntityCreator.createStairs(nextDungeonLevel.getStartpos().getX(),
					nextDungeonLevel.getStartpos().getY(), stairsDown.getX(), stairsDown.getY(), "stairs_up",
					nextDungeonLevel.getPreviousDungeonLevel());
			nextDungeonLevel.addEntity(nextDungeonLevel.getStartpos().getX(), nextDungeonLevel.getStartpos()
					.getY(), stairUp);
		}
		if (dungeon.getPreviousDungeonLevel() == null) {
			int x = getStartPos().getX();
			int y = getStartPos().getY();
			Entity stairUp = EntityCreator.createStairs(x, y, -1, -1, "stairs_up", null);
			dungeon.addEntity(x, y, stairUp);
		}

	}

	private void findPockets() {
		ArrayList<ArrayList<Position>> groups = new ArrayList<ArrayList<Position>>();

		// Floodfill every relevant node to get each group as a list of
		// positions
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				if (worldGrid[y][x] != 'X') {
					ArrayList<Position> group = floodFill(x, y);
					if (group != null) {
						groups.add(group);
					}
				}
			}
		}

		// print(worldGrid);
		// System.out.println("Number of groups: " + groups.size());
		// Sort the groups by size, biggest to smallest
		Collections.sort(groups, new Comparator<ArrayList<Position>>() {
			public int compare(ArrayList<Position> a1, ArrayList<Position> a2) {
				return a2.size() - a1.size();
			}
		});
		// Fill the largest group with floor and save it
		cave = groups.get(0);
		fill(cave, '.');
		// Fill the rest of the groups with walls
		for (int i = 1; i < groups.size(); i++) {
			fill(groups.get(i), 'X');
		}

	}

	private void fill(ArrayList<Position> group, char c) {
		for (Position position : group) {
			worldGrid[position.getY()][position.getX()] = c;
		}
	}

	private ArrayList<Position> floodFill(int x, int y) {
		// If the tile is filled or a wall, return null
		if ((worldGrid[y][x] == ',') || (worldGrid[y][x] == 'X'))
			return null;

		ArrayList<Position> group = new ArrayList<Position>();
		// mark the current tile as filled and add it to the group
		group.add(new Position(x, y));
		worldGrid[y][x] = ',';

		ArrayList<Position> group2;
		// For each of N,S,W,E floodFill recursively, then add the list (if it
		// exists) together
		group2 = floodFill(x, y - 1);
		if (group2 != null)
			group.addAll(group2);

		group2 = floodFill(x, y + 1);
		if (group2 != null)
			group.addAll(group2);

		group2 = floodFill(x + 1, y);
		if (group2 != null)
			group.addAll(group2);

		group2 = floodFill(x - 1, y);
		if (group2 != null)
			group.addAll(group2);

		return group;
	}

	public void initGrid(char[][] grid) {
		// randomizes the grid
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// 45% of the tiles are walls
				if (rand.nextInt(100) + 1 <= 45)
					grid[y][x] = 'X';
				else
					grid[y][x] = '.';
			}
		}

		// set the edgetiles to walls
		for (int x = 0; x < width; x++) {
			grid[0][x] = 'X';
			grid[height - 1][x] = 'X';
		}
		for (int y = 0; y < height; y++) {
			grid[y][0] = 'X';
			grid[y][width - 1] = 'X';
		}

	}

	public void generation(char[][] grid) {
		char[][] grid2 = new char[height][width];
		int adjacentcount1;
		int adjacentcount2;

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {

				// number of adjacent walls among the 8 closest neightbours
				adjacentcount1 = 0;
				for (int xi = -1; xi <= 1; xi++) {
					for (int yi = -1; yi <= 1; yi++) {
						if (grid[y + yi][x + xi] == 'X')
							adjacentcount1++;
					}
				}

				// number of walls among the 24 closest neighbors
				adjacentcount2 = 0;
				for (int xi = x - 2; xi <= x + 2; xi++) {
					for (int yi = y - 2; yi <= y + 2; yi++) {

						if (Math.abs(yi - y) == 2 && Math.abs(xi - x) == 2)
							continue;
						if (yi < 0 || xi < 0 || yi >= height || xi >= width)
							continue;
						if (grid[yi][xi] == 'X')
							adjacentcount2++;
					}
				}

				// If the closest neighbourhood contains 5(cutoff) or more walls
				// Or if the wider neighbourhood contains 2(cutoff2) or less
				// walls
				// the current tile turns into a wall
				// else it turns into a floor
				if (adjacentcount1 >= cutoff || adjacentcount2 <= cutoff2)
					grid2[y][x] = 'X';
				else
					grid2[y][x] = '.';
			}
		}

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				grid[y][x] = grid2[y][x];
			}
		}
	}

	public void generateEntities() {
		int adjacentcount1;
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {

				adjacentcount1 = 0;
				for (int xi = -1; xi <= 1; xi++) {
					for (int yi = -1; yi <= 1; yi++) {
						if (worldGrid[y + yi][x + xi] == 'X')
							adjacentcount1++;
					}
				}

				if (adjacentcount1 <= 0) {
					if (rand.nextInt(100) + 1 <= 4)
						dungeonEntities.add(spawnEnemy(x, y));
					if (rand.nextInt(100) + 1 <= 8) {
						Entity gold = EntityCreator.createGold(x, y, 100);
						dungeonEntities.add(gold);
					}
				}

			}
		}
	}

	private void generateStairs() {
		System.out.println("generateStairs() running");
		if (rand.nextInt(100) + 1 <= (width + height / 2)) {
			Position pos = cave.get(rand.nextInt(cave.size()));
			stairsDown = pos;
		}
	}

	private Entity spawnEnemy(int x, int y) {
		ArrayList<IComponent> components = new ArrayList<IComponent>();
		NameGenerator ng = new NameGenerator(3, seed);
		String name = ng.generateName();
		// String name = "Bat";
		String sprite = "mobs/mob_bat";
		components.add(new MobType(MobType.Type.GRUNT));
		components.add(new Health(10));
		components.add(new TurnsLeft(1));
		components.add(new Input());
		components.add(new Sprite(sprite));
		components.add(new Inventory()); // TODO add items that the
											// enemy is carrying here,
											// arraylist<entity> inside
											// constructor
		components.add(new Position(x, y));
		components.add(new Direction());
		components.add(new AI());
		Attribute attribute = new Attribute(name, SpaceClass.SPACE_ROGUE, SpaceRace.SPACE_DWARF, 1, 50);
		components.add(new BlocksWalking(true));
		components.add(new Weapon(2, 6, 0, TargetingSystem.SINGLE_TARGET, 1, 1)); // hardcoded
																					// equals
																					// bad
		components.add(new FieldOfView(8)); // hardcoded equals bad
		components.add(attribute);
		return EntityCreator.createEntity("(Enemy)" + name, components);
	}

	public Dungeon toDungeon() {
		Dungeon dungeon = new Dungeon();
		Tile[][] tiles = toTiles();
		dungeon.setWorld(tiles[0].length, tiles.length, tiles, getStartPos(), dungeonEntities);
		return dungeon;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public Tile[][] toTiles() {
		Tile[][] tiles = new Tile[worldGrid.length][worldGrid[0].length];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// X to walls
				if (worldGrid[y][x] == 'X') {
					tiles[y][x] = new Tile(new Sprite(wall), false, true);
					// . to floor
				} else if (worldGrid[y][x] == '.') {
					tiles[y][x] = new Tile(new Sprite(floor), true, false);
				}
			}
		}
		return tiles;
	}

	public Position getStartPos() {
		return startPos;
	}

	public void generateStartPosition() {
		// int x;
		// int y;
		// //Just pick a random position that is a floor
		// while(true){
		// x = rand.nextInt(width);
		// y = rand.nextInt(height);
		// if(worldGrid[y][x] == '.')
		// break;
		// }
		Position pos = cave.get(rand.nextInt(cave.size()));
		// System.out.println("Startpos:(x:"+x+",y:"+y+")");
		startPos = pos;
	}

	public void print(char[][] worldGrid) {
		System.out.println("____________________________________________________");
		for (int y = 0; y < height; y++) {
			System.out.println(worldGrid[y]);
		}
	}

	// public static void main(String[] args) {
	// new CellularLevelGenerator(50, 50, 123456789L);
	// }

}
