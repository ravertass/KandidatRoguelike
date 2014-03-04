package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.Player;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Weapon;
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
	
	final Position noClick = new Position(-1,-1);
	
	int buttonClicked;
	
	public HighlightSystem(EntityCreator ec, Engine engine) {
		entities = new ArrayList<Entity>();
		clickPos = noClick;
		buttonClicked = -1;
		this.ec = ec;
		this.engine = engine;
	}
	/**
	 * Will calculate on which tile to draw the highlight-sprite and then set its visibility to true.
	 */
	@Override
	public void update() {
		if (Mouse.isButtonDown(1)) {
			ArrayList<Position> line = Util.calculateLine(player.getComponent(Position.class),
					new Position((Mouse.getX() / Engine.spriteSize)
							+ camera.getPosition().getX(),
							(Mouse.getY() / Engine.spriteSize)
									+ camera.getPosition().getY()));

			// System.out.println("Line: " + line);
			for (Entity e : entities) {
				engine.removeEntity(e);
			}
			entities.clear();
			
			int range = player.getComponent(Weapon.class).getRange();
			int i = 0;
			for (Position pos : line) {
				if (i>range)
					break;
				// Insert code for highlighting all the tiles
				entities.add(ec.createHighlight(pos));
				i++;
			}
		}
		
		if (!Mouse.isButtonDown(1)) {
			for (Entity e : entities) {
				engine.removeEntity(e);
			}
			entities.clear();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		
		for (Entity e : entities) {
			if (Mouse.isButtonDown(1) && buttonClicked == 0) {
//				System.out.println("FIRE!!");
				resetMouse();
			} else if (buttonClicked == 0) {
				e.getComponent(Position.class).set(
						(clickPos.getX() / 16)
								+ camera.getPosition().getX(),
						(clickPos.getY() / 16)
								+ camera.getPosition().getY());
//				e.getComponent(Sprite.class).setVisibility(true);
				resetMouse();

			} else if (buttonClicked == 1) { 
				
			} else if (!Mouse.isButtonDown(1)) {
				
			} else if (Mouse.isButtonDown(1)) {
			
					
				
				//Gets all the positions between the player (currently just 1,1) and the Mouse
				ArrayList<Position> line = Util.calculateLine(new Position(1, 1),
						new Position((Mouse.getX() / 16)
								+ camera.getPosition().getX(),
								(Mouse.getY() / 16)
										+ camera.getPosition().getY()));

				// System.out.println("Line: " + line);

				for (Position pos : line) {
					// Insert code for highlighting all the tiles
//					ec.createHighlight();
				}

				e.getComponent(Position.class).set(
						(Mouse.getX() / 16) + camera.getPosition().getX(),
						(Mouse.getY() / 16) + camera.getPosition().getY());
//				e.getComponent(Sprite.class).setVisibility(true);

			}
			
		}*/
		
	}

	@Override
	public void addEntity(Entity entity) {
		
		if((entity.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer) {
			this.player = entity;
		} else if ((entity.getComponentKey() & Engine.CompHighlight) == Engine.CompHighlight){
			entities.add(entity);
		}
	}

	@Override
	public void removeEntity(Entity entity) {
//		entities.remove(entity);

	}

	public void setCamera(Camera c) {
		this.camera = c;
	}

	@Override
	public void notify(Enum<?> i) {
		if(i.equals(InputManager.InputAction.MOUSECLICK)) {
			clickPos = new Position(Mouse.getX(),Mouse.getY());
			buttonClicked = Mouse.getEventButton();
			
		}
		
	}
	
	public void resetMouse() {
		this.buttonClicked = -1;
		this.clickPos = noClick;
	}

}
