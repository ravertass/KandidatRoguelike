package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Engine.GameState;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Util;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.util.Observer;

/**
 * A system used for highlighting certain tiles in the Dungeon.
 */
public class HighlightSystem implements ISystem, Observer {

	ArrayList<Entity> entities;
	EntityCreator ec;
	Engine engine;
	Camera camera;
	Entity player;

	Position clickPos;

	final Position noClick = new Position(-1, -1);

	int buttonClicked;

	public HighlightSystem(EntityCreator ec, Engine engine) {
		entities = new ArrayList<Entity>();
		clickPos = noClick;
		buttonClicked = -1;
		this.ec = ec;
		this.engine = engine;
	}

	@Override
	public void update() {
	}

	/**
	 * Will calculate on which tile to draw the highlight-sprite and then set
	 * its visibility to true.
	 */
	public void update(Dungeon dungeon) {
		if(Engine.gameState != GameState.DUNGEON){
			// Prevents more from being added after state has changed
			return;
		}
		if (Mouse.isButtonDown(1)) {
			Position mousePos = new Position((Mouse.getX() / Engine.spriteSize)
					+ camera.getPosition().getX(),
					(Mouse.getY() / Engine.spriteSize)
							+ camera.getPosition().getY());
			ArrayList<Position> line = Util.calculateLine(
					player.getComponent(Position.class), mousePos);
			
			boolean brokenLine = false; //if the line has been broken or not

			Weapon weapon = player.getComponent(Weapon.class);
			
			ArrayList<Position> aoe = new ArrayList<Position>();
			
			for (Entity e : entities) {
				engine.removeEntity(e);
			}
			entities.clear();
			
			int range = player.getComponent(Weapon.class).getRange();
			int i = 0;
			for (Position pos : line) {
				if (i > range) {
					brokenLine = true;
					break;
				}
				Tile tile = dungeon.getTile(pos.getX(), pos.getY());
				if (!tile.isWalkable() && tile.blocksLineOfSight()) {
					brokenLine = true;
					break;
				}
				entities.add(ec.createHighlight(pos));
				i++;
			}
			
			
			if(weapon.getTargetingSystem() == TargetingSystem.BOX && !brokenLine) {
				for(int x = mousePos.getX()-weapon.getAoESize(); x <= mousePos.getX() + weapon.getAoESize(); x++) {
					for(int y = mousePos.getY()-weapon.getAoESize(); y <= mousePos.getY() + weapon.getAoESize(); y++) {
						aoe.add(new Position(x,y));
					}
				}
			} else if(weapon.getTargetingSystem() == TargetingSystem.CIRCLE) {
				; //TODO shall we even have this at all? doesn't look good
			} else if(weapon.getTargetingSystem() == TargetingSystem.CONE) {
				; //TODO
			} else if(weapon.getTargetingSystem() == TargetingSystem.NOVA) {
				; //TODO
			} else if(weapon.getTargetingSystem() == TargetingSystem.LINE) {
				; //TODO maybe add stuff here?
			}
			
			if(!brokenLine) {
				for(Position p : aoe) {
					Tile tile = dungeon.getTile(p.getX(), p.getY());
					if (!(tile == null) && !tile.blocksLineOfSight()) {
						entities.add(ec.createHighlight(p));
					}
				}
			}
		}

		if (!Mouse.isButtonDown(1)) {
			for (Entity e : entities) {
				engine.removeEntity(e);
			}
			entities.clear();
		}
	}

	/**
	 * Adds an entity from the system
	 * @param entity entity that should be added
	 */
	public void addEntity(Entity entity) {

		if ((entity.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer) {
			this.player = entity;
		} else if ((entity.getComponentKey() & Engine.CompHighlight) == Engine.CompHighlight) {
			System.out.println("BORK!");
			entities.add(entity);
		}
	}

	/**
	 * Removes an entity from the system
	 * @param entity entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		// entities.remove(entity);
	}

	/**
	 * Sets a camera that is used by the highlight system
	 * @param c the camera that should be used
	 */
	public void setCamera(Camera c) {
		this.camera = c;
	}

	/**
	 * notify receives messages from the input system
	 */
	public void notify(Enum<?> i) {
		if (i.equals(InputManager.InputAction.MOUSECLICK)) {
			clickPos = new Position(Mouse.getX(), Mouse.getY());
			buttonClicked = Mouse.getEventButton();
		}
	}

	/**
	 * Resets the previous mouse click to -1
	 */
	public void resetMouse() {
		this.buttonClicked = -1;
		this.clickPos = noClick;
	}
	
	public void unregister(){
		System.out.println("Unregister");
		for(Entity e : entities){
			System.out.println("remove");
			engine.removeEntity(e);
		}
	}

}
