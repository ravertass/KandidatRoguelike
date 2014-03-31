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
import se.chalmers.roguelike.util.pathfinding.PrintMap;

public class AISystem implements ISystem {

	/**
	 * A list of entities that has some kind of AI
	 */
	private ArrayList<Entity> entities;
	private AI ai;
	private Random rand;

	public AISystem(){
		entities = new ArrayList<Entity>();
		rand = new Random();
	}
	
	@Override
	public void update() {
		
	}
	
	public void update(Dungeon world, Entity player){
		for (Entity e : entities){
			//move, attack or sleep?
			int x = e.getComponent(Position.class).getX();
			int y = e.getComponent(Position.class).getY();
			Input input = e.getComponent(Input.class);
			int dist = e.getComponent(FieldOfView.class).getViewDistance();
			//int[][] fov = new ShadowCaster().calculateFOV(world, x, y, dist);
			ai = e.getComponent(AI.class);
			//targetPlayer(ai, fov, player);
			targetPlayer(ai, x, y, dist, player);
			Entity target = ai.getTarget();
			
			if (target != null){
				Position startPos = e.getComponent(Position.class);
				Position targetPos = target.getComponent(Position.class);
				Tile[][] worldTiles = world.getWorld();
				int[][] obstacleMap = createObstacleMap(worldTiles, targetPos);
				AreaMap map = new AreaMap(world.getWorldWidth(), world.getWorldHeight(), obstacleMap);
				
				ArrayList<Position> path = track(startPos, targetPos, map);
				//new PrintMap(map, path);
				//what to do if path is null e.g path is blocked?
				if(path == null){
					System.out.println(e + " DOES NOTHING");
					input.setNextEvent(InputAction.DO_NOTHING);
				} else if (path.size() <= e.getComponent(Weapon.class).getRange()){
					//Attackerar man nu?
					input.setAttackCords(targetPos);
				} else {
					int nextX = path.get(0).getX();
					int nextY = path.get(0).getY();
					
					if(nextX == x - 1 && nextY == y + 1){
						System.out.println(e + " GOES NORTHWEST");
						input.setNextEvent(InputAction.GO_NORTHWEST);
					} else if(nextX == x && nextY == y + 1){
						System.out.println(e + " GOES NORTH");
						input.setNextEvent(InputAction.GO_NORTH);
					} else if(nextX == x + 1 && nextY == y + 1){
						System.out.println(e+" GOES NORTHEAST");
						input.setNextEvent(InputAction.GO_NORTHEAST);
					} else if(nextX == x - 1 && nextY == y){
						System.out.println(e+" GOES WEST");
						input.setNextEvent(InputAction.GO_WEST);
					} else if(nextX == x && nextY == y - 1){
						System.out.println(e + " GOES SOUTH");
						input.setNextEvent(InputAction.GO_SOUTH);
					} else if(nextX == x + 1 && nextY == y){
						System.out.println(e+" GOES EAST");
						input.setNextEvent(InputAction.GO_EAST);
					} else if(nextX == x - 1 && nextY == y - 1){
						System.out.println(e + " GOES SOUTHWEST");
						input.setNextEvent(InputAction.GO_SOUTHWEST);
					} else if(nextX == x + 1 && nextY == y - 1){
						System.out.println(e + " GOES SOUTHEAST");
						input.setNextEvent(InputAction.GO_SOUTHEAST);
					} 
				}
			} else {
				boolean done = false;
				if (!(world.getTile(x - 1, y + 1).isWalkable() 
						|| world.getTile(x, y + 1).isWalkable() 
						|| world.getTile(x + 1, y + 1).isWalkable() 
						|| world.getTile(x - 1, y).isWalkable()
						|| world.getTile(x, y - 1).isWalkable()
						|| world.getTile(x + 1, y).isWalkable()
						|| world.getTile(x - 1, y - 1).isWalkable()
						|| world.getTile(x + 1, y - 1).isWalkable())){
					input.setNextEvent(InputAction.DO_NOTHING);
					done = true;
					System.out.println(e + " DID NOTHING!");
				}
				while (!done) {
					int randNr = rand.nextInt(8);
					if(randNr==0 && world.getTile(x - 1, y + 1).isWalkable()){
						System.out.println(e + " GOES NORTHWEST");
						input.setNextEvent(InputAction.GO_NORTHWEST);
						done = true;
					} else if(randNr==1 && world.getTile(x, y + 1).isWalkable()){
						System.out.println(e + " GOES NORTH");
						input.setNextEvent(InputAction.GO_NORTH);
						done = true;
					} else if(randNr==2 && world.getTile(x + 1, y + 1).isWalkable()){
						System.out.println(e+" GOES NORTHEAST");
						input.setNextEvent(InputAction.GO_NORTHEAST);
						done = true;
					} else if(randNr==3 && world.getTile(x - 1, y).isWalkable()){
						System.out.println(e+" GOES WEST");
						input.setNextEvent(InputAction.GO_WEST);
						done = true;
					} else if(randNr==4 && world.getTile(x, y - 1).isWalkable()){
						System.out.println(e + " GOES SOUTH");
						input.setNextEvent(InputAction.GO_SOUTH);
						done = true;
					} else if(randNr==5 && world.getTile(x + 1, y).isWalkable()){
						System.out.println(e+" GOES EAST");
						input.setNextEvent(InputAction.GO_EAST);
						done = true;
					} else if(randNr==6 && world.getTile(x - 1, y - 1).isWalkable()){
						System.out.println(e + " GOES SOUTHWEST");
						input.setNextEvent(InputAction.GO_SOUTHWEST);
						done = true;
					} else if(randNr==7 && world.getTile(x + 1, y - 1).isWalkable()){
						System.out.println(e + " GOES SOUTHEAST");
						input.setNextEvent(InputAction.GO_SOUTHEAST);
						done = true;
					} 
				}
			}
		}
	}

	//private void targetPlayer(AI ai, int[][] fieldOfView, Entity player) {
	private void targetPlayer(AI ai, int x, int y, int dist, Entity player) {
		int px = player.getComponent(Position.class).getX();
		int py = player.getComponent(Position.class).getY();
		ArrayList<Position> enemyDist = Util.bresenhamLine(x, y, px, py);
		
		Entity target = ai.getTarget();
		if (enemyDist.size() <= dist){
			ai.setTarget(player);
			if(target != ai.getTarget()){
				System.out.println("TARGET FOUND");
			}
<<<<<<< HEAD
			while (!done) {
				int randNr = rand.nextInt(8);
				if(randNr==0 && world.getTile(x - 1, y + 1).isWalkable()){
//					System.out.println(e + " GOES NORTHWEST");
					input.setNextEvent(InputAction.GO_NORTHWEST);
					done = true;
				} else if(randNr==1 && world.getTile(x, y + 1).isWalkable()){
//					System.out.println(e + " GOES NORTH");
					input.setNextEvent(InputAction.GO_NORTH);
					done = true;
				} else if(randNr==2 && world.getTile(x + 1, y + 1).isWalkable()){
//					System.out.println(e+" GOES NORTHEAST");
					input.setNextEvent(InputAction.GO_NORTHEAST);
					done = true;
				} else if(randNr==3 && world.getTile(x - 1, y).isWalkable()){
//					System.out.println(e+" GOES WEST");
					input.setNextEvent(InputAction.GO_WEST);
					done = true;
				} else if(randNr==4 && world.getTile(x, y - 1).isWalkable()){
//					System.out.println(e + " GOES SOUTH");
					input.setNextEvent(InputAction.GO_SOUTH);
					done = true;
				} else if(randNr==5 && world.getTile(x + 1, y).isWalkable()){
//					System.out.println(e+" GOES EAST");
					input.setNextEvent(InputAction.GO_EAST);
					done = true;
				} else if(randNr==6 && world.getTile(x - 1, y - 1).isWalkable()){
//					System.out.println(e + " GOES SOUTHWEST");
					input.setNextEvent(InputAction.GO_SOUTHWEST);
					done = true;
				} else if(randNr==7 && world.getTile(x + 1, y - 1).isWalkable()){
//					System.out.println(e + " GOES SOUTHEAST");
					input.setNextEvent(InputAction.GO_SOUTHEAST);
					done = true;
				} 
=======
		} else {
			ai.setTarget(null);
			if(target != ai.getTarget()){
				System.out.println("TARGET LOST");
>>>>>>> pathFinding
			}
		}
	}

	private int[][] createObstacleMap(Tile[][] worldTiles, Position target) {
		
		//Svarta tiles är null? Dörrar är walkable? Vad är walkable?
		int[][] obstacleMap = new int[worldTiles.length][worldTiles[1].length];
		for (int i = 0; i < obstacleMap.length; i++){
			for (int j = 0; j < obstacleMap[i].length; j++) {
				if(worldTiles[i][j] != null && worldTiles[i][j].isWalkable() || target.getX() == j && target.getY() == i){
					obstacleMap[i][j] = 0;
				} else{
					obstacleMap[i][j] = 1;
				}
			}
		}
		return obstacleMap;
	}

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
	public void addEntity(Entity e){
		entities.add(e);		
	}
	
	/**
	 * Removes an entity from the AISystem
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
}
