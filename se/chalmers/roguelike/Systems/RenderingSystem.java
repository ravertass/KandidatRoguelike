package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.awt.Color;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.util.Button;
import se.chalmers.roguelike.util.FontRenderer;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.util.Camera;
import static org.lwjgl.opengl.GL11.*;

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
				if(tile != null) {
					draw(tile.getSprite(),drawPos);
				}
			}
		}
	}
	
	public void update() {

		// Draw all entities in system
		for(Entity entity : entitiesToDraw) {
			draw(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
			if((entity.getComponentKey() & Engine.CompHealth) == Engine.CompHealth)
				drawHealthbar(entity);
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
		glEnable(GL_TEXTURE_2D); 
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
	
	private void drawHealthbar(Entity e) {
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
			glBegin(GL_QUADS);
				GL11.glLineWidth(5.0f);
				GL11.glVertex2f(0,100);
				GL11.glVertex2f(300,100);
			glEnd();
		GL11.glColor3f(1.0f,1.0f,1.0f);
	}
}
