package se.chalmers.roguelike.World;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.BlocksWalking;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.MobType;
import se.chalmers.roguelike.Components.FieldOfView;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;

/**
 * Dungeon is an class that holds the game world along with several helper
 * functions for determining if the world can be walked on etc.
 */

public class Dungeon {

	private int worldWidth;
	private int worldHeight;
	private Position startpos;
	private Tile[][] tiles;
	private ArrayList<Entity> entities;
	private boolean currentlyRegistering = false;
	private Dungeon previousDungeonLevel;
	private Dungeon nextDungeonLevel;
	private int plotThingX, plotThingY;
	private boolean plotAccomplished = false;

	/**
	 * Creates a new world object
	 * 
	 * @param worldWidth
	 *            the width of the world
	 * @param worldHeight
	 *            the height of the world
	 * @param tiles
	 *            the array of tiles the world should have
	 */
	public Dungeon(int worldWidth, int worldHeight, Tile[][] tiles,
			Position startpos, ArrayList<Entity> entities) {
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.tiles = tiles;
		this.setStartpos(startpos);
		this.entities = new ArrayList<Entity>();
		for (Entity e : entities) {
			Position pos = e.getComponent(Position.class);
			addEntity(pos.getX(), pos.getY(), e);
		}
	}

	/**
	 * Currently generates a new world, will probably be removed in the future
	 * when a real level generator exists
	 */
	public Dungeon() {

		// Debug world
		worldWidth = 50;
		worldHeight = 50;
		tiles = new Tile[worldHeight][worldWidth];
		entities = new ArrayList<Entity>();
	}

	/**
	 * Looks up if something can walk on a tile
	 * 
	 * @param x
	 *            x coordinate for the tile to look up
	 * @param y
	 *            y coordinate for the tile to look up
	 * @return true if the tile can be walked on, false if it can not (already
	 *         occupied, walls, etc)
	 */
	public boolean isWalkable(int x, int y) {
		return x >= 0 && x < worldWidth && y >= 0 && y < worldHeight
				&& tiles[y][x] != null && tiles[y][x].isWalkable();
	}

	/**
	 * Looks up and returns a tile
	 * 
	 * @param x
	 *            x coordinate for the tile to look up
	 * @param y
	 *            y coordinate for the tile to look up
	 * @return returns the tile if it exists, otherwise null
	 */
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= worldWidth || y < 0 || y >= worldHeight) {
			return null;
		}
		return tiles[y][x];
	}

	public int getWorldWidth() {
		return worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public Tile[][] getWorld() {
		return tiles;
	}

	/**
	 * Method use to changing the world
	 * 
	 * @param worldWidth
	 *            the width of the new world, must match the array
	 * @param worldHeight
	 *            the height of the new world, must match the array
	 * @param tiles
	 *            a 2d-array of all the tiles for the world
	 */
	public void setWorld(int worldWidth, int worldHeight, Tile[][] tiles,
			Position startpos, ArrayList<Entity> entities) {
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.tiles = tiles;
		// this.setEnemies(enemies);
		// entities.addAll(enemies);
		for (Entity e : entities) {
			Position pos = e.getComponent(Position.class);
			addEntity(pos.getX(), pos.getY(), e);
		}
		this.setStartpos(startpos);
	}

	public void addEntity(int x, int y, Entity entity) {
		if (currentlyRegistering) {
			return; // since we want to keep all the entities for the world when
					// switching
		}
		if (x < 0 || y < 0 || x >= worldWidth || y >= worldHeight
				|| tiles[y][x] == null) {
			return; // out of bounds check
		}
		tiles[y][x].addEntity(entity);
		entities.add(entity);
	}

	public void removeEntity(int x, int y, Entity entity) {
		if (currentlyRegistering) {
			return; // since we want to keep all the entities for the world when
					// switching
		}
		if (x < 0 || y < 0 || x > worldWidth || y > worldHeight
				|| tiles[y][x] == null) {
			return; // out of bounds check
		}
		tiles[y][x].removeEntity(entity);
		entities.remove(entity);
	}

	public void unregister(Engine engine) {
		currentlyRegistering = true;
		int player = -1;
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if ((e.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer) {
				player = i;
			}
			engine.removeEntity(e);
		}
		currentlyRegistering = false;
		if (player != -1) {
			engine.removeEntity(entities.get(player));
		}
	}

	public void register(Engine engine) {
		currentlyRegistering = true;
		for (Entity e : entities) {
			engine.addEntity(e);
		}
		currentlyRegistering = false;
	}

	public Position getStartpos() {
		return startpos;
	}

	public void setStartpos(Position startpos) {
		this.startpos = startpos;
	}

	public void setEnemies(ArrayList<Entity> enemies) {
		entities.addAll(enemies);
	}

	public Dungeon getPreviousDungeonLevel() {
		System.out.println("getting prev " + this);
		return previousDungeonLevel;
	}

	public void setPreviousDungeonLevel(Dungeon previousDungeonLevel) {
		System.out.println("setting prev " + this);
		this.previousDungeonLevel = previousDungeonLevel;
	}

	public Dungeon getNextDungeonLevel() {
		return nextDungeonLevel;
	}

	public void setNextDungeonLevel(Dungeon nextDungeonLevel) {
		this.nextDungeonLevel = nextDungeonLevel;
	}

	/**
	 * Add the position at which the plotThing (the boss/the main loot) will be
	 * generated.
	 * 
	 * @param x
	 * @param y
	 */
	public void addPlotThingPosition(int x, int y) {
		plotThingX = x;
		plotThingY = y;
	}

	private void addPlotThing(Entity plotThing) {
		if (nextDungeonLevel == null) {
			addEntity(plotThingX, plotThingY, plotThing);
		} else {
			nextDungeonLevel.addPlotThing(plotThing);
		}
	}

	/**
	 * This is the method with which you add a boss to the dungeon.
	 * 
	 * @param plotThing
	 */
	public void addBoss(Actor actor) {
		ArrayList<IComponent> components = new ArrayList<IComponent>();

		String name = actor.toString();
		String sprite = "mobs/mob_smurf";
		components.add(new MobType(MobType.Type.BOSS));
		components.add(new Health(20));
		components.add(new TurnsLeft(1));
		components.add(new Input());
		components.add(new Sprite(sprite));
		components.add(new Inventory()); // TODO add items that the
											// enemy is carrying here,
											// arraylist<entity> inside
											// constructor
		components.add(new Position(plotThingX, plotThingY));
		components.add(new Direction());
		components.add(new AI());
		Attribute attribute = new Attribute(name, SpaceClass.SPACE_ROGUE,
				SpaceRace.SPACE_DWARF, 1, 50);
		components.add(new BlocksWalking(true));
		components
				.add(new Weapon(2, 6, 0, TargetingSystem.SINGLE_TARGET, 1, 1)); // hardcoded
																				// equals
																				// bad
		components.add(new FieldOfView(8)); // hardcoded equals bad
		components.add(attribute);
		Entity boss = EntityCreator.createEntity("(Boss)" + name, components);
		addPlotThing(boss);
	}

	/**
	 * @return if the boss has been defeated/the plot item has been picked up
	 */
	public boolean getPlotAccomplished() {
		return plotAccomplished;
	}

	public void setPlotAccomplished(boolean plotAccomplished) {
		this.plotAccomplished = plotAccomplished;
		if (previousDungeonLevel != null) {
			previousDungeonLevel.setPlotAccomplished(plotAccomplished);
		}
	}
}
