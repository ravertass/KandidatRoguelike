package se.chalmers.roguelike.Systems;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.util.FontRenderer;
import se.chalmers.roguelike.util.Util;

/**
 * This is the system that draws everything to be drawn.
 * It knows of all entities with both position and sprites, and those are
 * the components which it uses.
 */
public class RenderingSystem implements ISystem {

	// The entities that the RenderingSystem draws
	private ArrayList<Entity> entitiesToDraw;
	private Camera camera;
	private Entity player;
	private FontRenderer fontRenderer;
	
	private final int DISPLAY_WIDTH = 1024;
	private final int DISPLAY_HEIGHT = 768;
	
	public RenderingSystem() { // possibly remove world?
		// Magic tricks done by lwjgl
		setupDisplay();
		setupOpenGL();
		
		try {
			fontRenderer = new FontRenderer(
					new UnicodeFont("/resources/fonts/circula-medium.otf", 28, false, true), Color.white);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		fontRenderer.load();
		
		// Initialize the list of entities to be drawn
		entitiesToDraw = new ArrayList<Entity>();
	}
	
	
	public void update(Dungeon dungeon) { // stupid solution, make it nondependant on world
		// Sets the cameras position to the current position of the player
		int cwidth = camera.getWidth();
		int cheight = camera.getHeight();
		Position playerPos = player.getComponent(Position.class);
		
		// It's a bit confusing that the camera is set here...
		camera.setPosition(new Position(playerPos.getX()-cwidth/2, playerPos.getY()-cheight/2));
		
		// Clear the window
		glClear(GL_COLOR_BUFFER_BIT);
		
		// This seems to be unnecessary
		Position pos = new Position(playerPos.getX()-cwidth/2, playerPos.getY()-cheight/2);
		
		// This code draws out the background sprites for all tiles in the camera's view
		Position drawPos = new Position(pos.getX(), pos.getY());
		for(int x = pos.getX()-cwidth/2; x < pos.getX() + cwidth; x++) {
			for(int y = pos.getY()-cheight/2; y < pos.getY() + cheight; y++) {
				Tile tile = dungeon.getTile(x,y);
				drawPos.set(x, y);
				if(tile != null && visibleForPlayer(dungeon, playerPos, x, y)) {
					draw(tile.getSprite(),drawPos);
				}
			}
		}
	}
	
	public void update() {

		
		for (Entity e : entitiesToDraw) {
			if((e.getComponentKey() & Engine.CompHealth) == Engine.CompHealth){
				drawHealthbar(e);
			}
		}
		// Draw all entities in system
		for(Entity entity : entitiesToDraw) {
			
			draw(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
//			drawHealthbar(entity);
		}
		
		//drawHudBackgorund();
		
		// Update and sync display
		Display.update();
		Display.sync(60);
	}
	



	/**
	 * This method sets up OpenGL. We're not quite sure of what it does.
	 */
	private void setupOpenGL() {
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
//		glEnable(GL_TEXTURE_2D); 
		// Enables the use of transparent PNGs
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * This method sets up and creates a new display.
	 */
	private void setupDisplay() {
		try {
//			DisplayMode displayMode = null;
//	        DisplayMode[] modes = Display.getAvailableDisplayModes();
//
//	        for (int i = 0; i < modes.length; i++)
//	        {
//	            if (modes[i].getWidth() == DISPLAY_WIDTH
//	            && modes[i].getHeight() == DISPLAY_HEIGHT
//	            && modes[i].isFullscreenCapable())
//	              {
//	                   displayMode = modes[i];
//	              }
//	        }
			Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
			Display.setFullscreen(true);
			Display.setTitle("Crimson Poodle");
			Display.create();
		} catch (LWJGLException e) {			
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}	
	}
	
	/**
	 * Method to draw an entity to the game window.
	 * @param entity The entity to be drawn
	 */
	private void draw(Sprite sprite, Position position) {
		if(!sprite.getVisibility())
			return;
		
		Texture texture = sprite.getTexture();
		int size = sprite.getSize(); // Times two, makes sprites twice as large
		
		// Get the camera's position
		Position camPos = camera.getPosition();
		int camX = camPos.getX();
		int camY = camPos.getY();
		// Subtract the coordinates with the camera's coordinates,
		// then multiply that with the SPRITE_SIZE, so that we get 
		// the pixel coordinates, not the tile coordinates.
		int x = (position.getX() - camX) * size;
		int y = (position.getY() - camY) * size;
		
		// Get the coordinates of the current sprite
		// in the spritesheet in a form that OpenGL likes,
		// which is a float between 0 and 1
		float spriteULX = sprite.getUpperLeftX();
		float spriteULY = sprite.getUpperLeftY();
		float spriteLRX = sprite.getLowerRightX();
		float spriteLRY = sprite.getLowerRightY();
		
		// We determine if the entity is within the camera's
		// view; if so, we draw it
		if (x >= 0 && x < camera.getWidth() * size &&
				y >= 0 && y < camera.getHeight() * size) {
			texture.bind();
			glBegin(GL_QUADS);
				glTexCoord2f(spriteULX, spriteLRY);
				glVertex2d(x, y);
				glTexCoord2f(spriteLRX, spriteLRY);
				glVertex2d(x + size, y);
				glTexCoord2f(spriteLRX, spriteULY);
				glVertex2d(x + size, y + size);
				glTexCoord2f(spriteULX, spriteULY);
				glVertex2d(x, y + size);
			glEnd();
		}
	}
	
	public void exit() {
		Display.destroy();
		System.exit(0);
	}

	@Override
	public void addEntity(Entity entity) {
		if((entity.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer) {
			this.player = entity;
		} 
		entitiesToDraw.add(entity);
	}
	
	@Override
	public void removeEntity(Entity entity) {
	    entitiesToDraw.remove(entity);
    }
	
	public void drawMenu(String[] menuItems) {
					
			int x = 64;
			int height = Display.getDisplayMode().getHeight();
			int width = Display.getDisplayMode().getWidth();
					
					
			//Stuff is upside down without this
			glDisable(GL_LIGHTING);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, width, height, 0, 1, -1);
			
			glMatrixMode(GL_MODELVIEW);
			glPushMatrix();
			glLoadIdentity();
					
			int y = height/2;
		 			
			for (String s : menuItems){
				fontRenderer.draw(x, y, s);
				y += 40;
			}
					
			
			/*
			*	//create a button
			*
			*	Button button = new Button();
			*	button.addButton(x, 40, "menu_button");
			*	button.draw();
			*		
			*	//create a rectangle (black?)
			*	glBegin(GL_QUADS);
			*		glVertex2f(width/2, height/2);
			*		glVertex2f(width/2+100, height/2);
			*		glVertex2f(width/2+100, height/2+32);
			*		glVertex2f(width/2, height/2+32);
			*	glEnd();
			*/
			
			Display.update();
			Display.sync(60);
		}
	
	public void setCamera(Camera c) {
		this.camera = c;
	}
	
	private void drawHudBackground() {
		
//		glBegin(GL_QUADS);
//			glVertex2f(x, y);
//		glEnd();
		
	}
	/**
	 * draws healthbars for all entities that have health
	 * @param e
	 */
	private void drawHealthbar(Entity e) {
		Position epos = e.getComponent(Position.class); // tilebased positions
		Position camPos = camera.getPosition();
//		System.out.println(e + ": " +epos);
		int camX = camPos.getX();
		int camY = camPos.getY();

		int x = (epos.getX() - camX) * 16;
		int y = (epos.getY() - camY) * 16;
		
		Health h = e.getComponent(Health.class);
		double healthPercentage = h.getHealthPercentage();
		double healthbarLength = healthPercentage * 16; //calculates how much of the green should be drawn over the red
		
		int hblength = (int)healthbarLength;
		
		//draw the red part of the healthbar
		glColor3f(1.0f, 0.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * 16 &&
				y >= 0 && y < camera.getHeight() * 16) {
			glBegin(GL_QUADS);
				GL11.glVertex2i(x,y+16);
				GL11.glVertex2i(x+16,y+16);
				GL11.glVertex2i(x+16,y+18);
				GL11.glVertex2i(x,y+18);
			glEnd();
		}
		
		//draw the green part of the healthbar ontop of the red
		glColor3f(0.0f, 1.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * 16 &&
				y >= 0 && y < camera.getHeight() * 16) {
			glBegin(GL_QUADS);
				GL11.glVertex2i(x,y+16);
				GL11.glVertex2i(x+hblength,y+16);
				GL11.glVertex2i(x+hblength,y+18);
				GL11.glVertex2i(x,y+18);
			glEnd();
				
		}
		glColor3f(1.0f,1.0f,1.0f);
	}
	
	private boolean visibleForPlayer(Dungeon d, Position playerPos, int x, int y) {
		ArrayList<Position> line = Util.calculateLine(playerPos.getX(), playerPos.getY(), x, y);
			for (Position p : line) {
				if(d.getTile(p.getX(), p.getY()).blocksLineOfSight())
					return false;
			}
			return true;
		
	}

	
}
