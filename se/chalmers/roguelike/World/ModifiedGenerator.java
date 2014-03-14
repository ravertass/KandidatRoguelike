package se.chalmers.roguelike.World;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.newdawn.slick.geom.BasicTriangulator;
import org.newdawn.slick.geom.Triangulator;

import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.DelauneyTriangulator;
import se.chalmers.roguelike.util.Edge;
import se.chalmers.roguelike.util.Triangle;

public class ModifiedGenerator {

	private final int amountOfRooms = 100;
	private int width = 80;
	private int height = 80;
	private int xMinDisplacement = 0, yMinDisplacement = 0;
	// private char[][] worldGrid;
	private ArrayList<Rectangle> rooms;
	private ArrayList<Rectangle> largeRooms = new ArrayList<Rectangle>();
	Random rand; // replace with new Random(seed); later, already tried and
					// works

	public ModifiedGenerator() {

		Random seedRand = new Random();
		long seed = seedRand.nextLong();
		// long seed = 3182815830558287750L;
		System.out.println("Using seed: " + seed);
		rand = new Random(seed);
		run();
	}

	private void run() {
		char[][] grid;

		rooms = placeRooms();
		separateRooms();
		grid = initWorldGrid();

		// atm so we can print it
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid[y][x] = ' ';
			}
		}
		drawRooms(grid);
		ArrayList<Edge> edges = triangulateRooms(grid);
		// TODO
		//Kruskal on the edges
		// add 20% of the edges back
		// create corridors from the edges
		print(grid);
	}

	private ArrayList<Edge> triangulateRooms(char[][] grid) {
		// To see where we set nodes
		ArrayList<Position> nodes = generateNodes();
		for (Position point : nodes) {
			grid[point.getY() + Math.abs(yMinDisplacement)][point.getX()
					+ Math.abs(xMinDisplacement)] = 'o';
		}
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
		ArrayList<Rectangle> graph = new ArrayList<>();
		int x, y;
		Rectangle room;
		for (int i = 0; i < amountOfRooms; i++) {
			x = rand.nextInt(width);
			y = rand.nextInt(height);
			room = generateRoom();
			room.setLocation(x, y);
			graph.add(room);
			if (room.width >= 8 && room.height >= 8) {
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
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				worldGrid[y][x] = ' ';
			}
		}

		// DRAW
		for (Rectangle drawRoom : rooms) {
			// Line the sides as walls:
			// new code:
			int roomHeight = (int) drawRoom.getY() + (int) drawRoom.getHeight();
			int roomWidth = (int) drawRoom.getX() + (int) drawRoom.getWidth();
			for (int y = (int) drawRoom.getY(); y < roomHeight; y++) {
				for (int x = (int) drawRoom.getX(); x < roomWidth; x++) {
					if (y == (int) drawRoom.getY() || y == roomHeight - 1
							|| x == (int) drawRoom.getX() || x == roomWidth - 1) {
						worldGrid[y + Math.abs(yMinDisplacement)][x
								+ Math.abs(xMinDisplacement)] = 'X'; // HARDCODED
																		// +10
					} else {
						worldGrid[y + Math.abs(yMinDisplacement)][x
								+ Math.abs(xMinDisplacement)] = '.'; // HARDCODED
																		// +10
					}
				}
			}
		}
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
	 * Generates a room, keep in mind that the way the code is current written
	 * (which will change) the room size will be decreates by two in both width
	 * and height so that it will have borders.
	 * 
	 * @return a Rectangle representing a room
	 */
	private Rectangle generateRoom() {
		int height = 5 + rand.nextInt(7); // will be decreased by two
		int width = 5 + rand.nextInt(7); // will be decreased by two when
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
					tiles[y][x] = new Tile(new Sprite("brick"), false, true);
				} else if (worldGrid[y][x] == '.') {
					tiles[y][x] = new Tile(new Sprite("sand"), true, false);
				}
			}
		}
		return tiles;

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
				.println("_____________________________________________________________");
		for (int y = 0; y < height; y++) {
			System.out.println(worldGrid[y]);
		}
	}

	public static void main(String[] args) {
		new ModifiedGenerator();
	}
}
