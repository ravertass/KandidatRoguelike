package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;

import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Entities.Entity;

import static org.lwjgl.opengl.GL11.*;

/**
 * This is the system that draws everything to be drawn.
 * It knows of all entities with both position and sprites, and those are
 * the components which it uses.
 */
public class RenderingSystem implements ISystem {

	// Tills vidare ligger dessa här, känns fel	
	private final int CAMERA_WIDTH = 20; // in tiles
	private final int CAMERA_HEIGHT = 15; // in tiles
	
	// The entities that the RenderingSystem draws
	private ArrayList<Entity> entitiesToDraw;
	private Entity camera;
	
	public RenderingSystem() {
		// Magic tricks done by lwjgl
		setupDisplay();
		setupOpenGL();
		
		// Create a camera entity
		camera = new Entity();
		// Kamerans position får helt enkelt vara noll-noll, tills vidare
		Position camPosition = new Position(0,0);
		camera.add(camPosition);
		
		// Initialize the list of entities to be drawn
		entitiesToDraw = new ArrayList<Entity>();
	}
	
	public void update() {
		// Clear the window
		glClear(GL_COLOR_BUFFER_BIT);
		
		// Draw all entities in system
		for(Entity entity : entitiesToDraw) {
			drawEntity(entity);
		}
		
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
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D); 
		// Enables the use of transparent PNGs
		//glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * This method sets up and creates a new display.
	 */
	private void setupDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
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
	private void drawEntity(Entity entity) {
		// Get the relevant components from the entity
		Sprite sprite = entity.getComponent(Sprite.class);
		Position position = entity.getComponent(Position.class);
		
		Texture texture = sprite.getTexture();
		int size = sprite.getSize();
		
		// Get the camera's position
		Position camPos = camera.getComponent(Position.class);
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
		if (x >= 0 && x < CAMERA_WIDTH * size &&
				y >= 0 && y < CAMERA_HEIGHT * size) {
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
		entitiesToDraw.add(entity);
	}
}
