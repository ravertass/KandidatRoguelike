package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.Random;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.FieldOfView;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.ShadowCaster;
import se.chalmers.roguelike.util.Util;
import se.chalmers.roguelike.util.pathfinding.AStar;
import se.chalmers.roguelike.util.pathfinding.AreaMap;
import se.chalmers.roguelike.util.pathfinding.DiagonalHeuristic;

public class AISystem implements ISystem {

	// A list of entities that has some kind of AI
	private ArrayList<Entity> entities;
	private AI ai;
	private Random rand;

	public AISystem() {
		entities = new ArrayList<Entity>();
		rand = new Random();
	}

	@Override
	public void update() {
	}

	/**
	 * The update loop that runs every turn
	 * 
	 * @param world
	 * @param player
	 */
	public void update(Dungeon world, Entity player) {
		for (Entity e : entities) {
			int x = e.getComponent(Position.class).getX();
			int y = e.getComponent(Position.class).getY();
			Input input = e.getComponent(Input.class);
			int dist = e.getComponent(FieldOfView.class).getViewDistance();
			int[][] fov = new ShadowCaster().calculateFOV(world, x, y, dist);
			ai = e.getComponent(AI.class);
			targetPlayer(ai, fov, player);
			//targetPlayer(ai, x, y, dist, player);
			Entity target = ai.getTarget();

			if (target != null) {
				Position startPos = e.getComponent(Position.class);
				Position targetPos = target.getComponent(Position.class);
				Tile[][] worldTiles = world.getWorld();
				int[][] obstacleMap = createObstacleMap(worldTiles, targetPos);
				AreaMap map = new AreaMap(world.getWorldWidth(), world.getWorldHeight(), obstacleMap);

				ArrayList<Position> path = track(startPos, targetPos, map);
				// new PrintMap(map, path);
				// what to do if path is null e.g path is blocked?
				if (path == null) {
					randomMove(e, world);
				} else if (path.size() <= e.getComponent(Weapon.class).getRange()) {
					input.setAttackCords(targetPos);
				} else {
					int nextX = path.get(0).getX();
					int nextY = path.get(0).getY();

					if (nextX == x - 1 && nextY == y + 1) {
						input.setNextEvent(InputAction.GO_NORTHWEST);
					} else if (nextX == x && nextY == y + 1) {
						input.setNextEvent(InputAction.GO_NORTH);
					} else if (nextX == x + 1 && nextY == y + 1) {
						input.setNextEvent(InputAction.GO_NORTHEAST);
					} else if (nextX == x - 1 && nextY == y) {
						input.setNextEvent(InputAction.GO_WEST);
					} else if (nextX == x && nextY == y - 1) {
						input.setNextEvent(InputAction.GO_SOUTH);
					} else if (nextX == x + 1 && nextY == y) {
						input.setNextEvent(InputAction.GO_EAST);
					} else if (nextX == x - 1 && nextY == y - 1) {
						input.setNextEvent(InputAction.GO_SOUTHWEST);
					} else if (nextX == x + 1 && nextY == y - 1) {
						input.setNextEvent(InputAction.GO_SOUTHEAST);
					}
				}
			} else {
				randomMove(e, world);
			}
		}
	}

	/**
	 * Moves to a random position
	 * 
	 * @param e
	 * @param world
	 */
	private void randomMove(Entity e, Dungeon world) {
		int x = e.getComponent(Position.class).getX();
		int y = e.getComponent(Position.class).getY();
		Input input = e.getComponent(Input.class);
		boolean done = false;
		if (!(world.getTile(x - 1, y + 1).isWalkable() || world.getTile(x, y + 1).isWalkable()
				|| world.getTile(x + 1, y + 1).isWalkable() || world.getTile(x - 1, y).isWalkable()
				|| world.getTile(x, y - 1).isWalkable() || world.getTile(x + 1, y).isWalkable()
				|| world.getTile(x - 1, y - 1).isWalkable() || world.getTile(x + 1, y - 1).isWalkable())) {
			input.setNextEvent(InputAction.DO_NOTHING);
			done = true;
		}
		while (!done) {
			int randNr = rand.nextInt(8);
			if (randNr == 0 && world.getTile(x - 1, y + 1).isWalkable()) {
				input.setNextEvent(InputAction.GO_NORTHWEST);
				done = true;
			} else if (randNr == 1 && world.getTile(x, y + 1).isWalkable()) {
				input.setNextEvent(InputAction.GO_NORTH);
				done = true;
			} else if (randNr == 2 && world.getTile(x + 1, y + 1).isWalkable()) {
				input.setNextEvent(InputAction.GO_NORTHEAST);
				done = true;
			} else if (randNr == 3 && world.getTile(x - 1, y).isWalkable()) {
				input.setNextEvent(InputAction.GO_WEST);
				done = true;
			} else if (randNr == 4 && world.getTile(x, y - 1).isWalkable()) {
				input.setNextEvent(InputAction.GO_SOUTH);
				done = true;
			} else if (randNr == 5 && world.getTile(x + 1, y).isWalkable()) {
				input.setNextEvent(InputAction.GO_EAST);
				done = true;
			} else if (randNr == 6 && world.getTile(x - 1, y - 1).isWalkable()) {
				input.setNextEvent(InputAction.GO_SOUTHWEST);
				done = true;
			} else if (randNr == 7 && world.getTile(x + 1, y - 1).isWalkable()) {
				input.setNextEvent(InputAction.GO_SOUTHEAST);
				done = true;
			}
		}
	}

	/**
	 * Target the player using the shadowcasting algorithm so the detection feels more natural (currently
	 * broken)
	 * 
	 * @param ai
	 * @param fieldOfView
	 * @param player
	 */
	private void targetPlayer(AI ai, int[][] fieldOfView, Entity player) {
		int px = player.getComponent(Position.class).getX();
		int py = player.getComponent(Position.class).getY();

		Entity target = ai.getTarget();
		if (fieldOfView[px][py] == 1) {
			ai.setTarget(player);
			if (target != ai.getTarget()) {
				System.out.println("YOU HAVE BEEN FOUND BY AN ENEMY");
			}
		} else {
			ai.setTarget(null);
			if (target != ai.getTarget()) {
				System.out.println("THE ENEMY LOST INTEREST IN TRACKING YOU");
			}
		}
	}

	/**
	 * Targets the player if the entity is close enough using bresenham's line, which means that walls doesn't
	 * prevent detection
	 * 
	 * @param ai
	 * @param x
	 * @param y
	 * @param dist
	 * @param player
	 */
	private void targetPlayer(AI ai, int x, int y, int dist, Entity player) {
		//TODO remove if no faults found in previous
		int px = player.getComponent(Position.class).getX();
		int py = player.getComponent(Position.class).getY();
		ArrayList<Position> enemyDist = Util.bresenhamLine(x, y, px, py);

		Entity target = ai.getTarget();
		if (enemyDist.size() <= dist) {
			ai.setTarget(player);
			if (target != ai.getTarget()) {
				System.out.println("YOU HAVE BEEN FOUND BY AN ENEMY");
			}
		} else {
			ai.setTarget(null);
			if (target != ai.getTarget()) {
				System.out.println("THE ENEMY LOST INTEREST IN TRACKING YOU");
			}
		}
	}

	/**
	 * Creates a mapping over the world for what tiles are walkable and which are not
	 * 
	 * @param worldTiles
	 * @param target
	 * @return
	 */
	private int[][] createObstacleMap(Tile[][] worldTiles, Position target) {
		int[][] obstacleMap = new int[worldTiles.length][worldTiles[1].length];
		for (int i = 0; i < obstacleMap.length; i++) {
			for (int j = 0; j < obstacleMap[i].length; j++) {
				if (worldTiles[i][j] != null && worldTiles[i][j].isWalkable() || target.getX() == j
						&& target.getY() == i) {
					obstacleMap[i][j] = 0;
				} else {
					obstacleMap[i][j] = 1;
				}
			}
		}
		return obstacleMap;
	}

	/**
	 * Returns the shortest path from the entity to its target using a*
	 * 
	 * @param start
	 * @param target
	 * @param map
	 * @return
	 */
	private ArrayList<Position> track(Position start, Position target, AreaMap map) {
		DiagonalHeuristic heuristic = new DiagonalHeuristic();
		AStar findPath = new AStar(map, heuristic);
		ArrayList<Position> shortestPath = findPath.calcShortestPath(start.getX(), start.getY(),
				target.getX(), target.getY());
		return shortestPath;
	}

	/**
	 * Add an entity to the AISystem
	 * 
	 * @param e
	 */
	public void addEntity(Entity e) {
		entities.add(e);
	}

	/**
	 * Remove an entity from the AISystem
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
}