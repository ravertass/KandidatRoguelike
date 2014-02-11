package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;

import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Entities.Entity;

import static org.lwjgl.opengl.GL11.*;

public class RenderingSystem implements ISystem {

	private ArrayList<Entity> entities;
	
	public RenderingSystem() {
		setupDisplay();
		setupOpenGL();
		entities = new ArrayList<Entity>();
		
		// Fulkod nedan för att testa att generera en entity
		// som sedan ska ritas ut
		Entity testThing = new Entity();
		Sprite sprite = new Sprite("guy", 32, 32);
		Position position = new Position(32, 32);
		testThing.add(position);
		testThing.add(sprite);
		entities.add(testThing);
	}
	
	public void update() {
		// clear the window
		glClear(GL_COLOR_BUFFER_BIT);
		
		// draw all entities in system
		for(Entity entity : entities) {
			drawEntity(entity);
		}
		
		// update and sync display
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
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
	
	private void drawEntity(Entity entity) {
		Sprite sprite = entity.getComponent(Sprite.class);
		Position position = entity.getComponent(Position.class);
		
		Texture texture = sprite.getTexture();
		int width = sprite.getWidth();
		int height = sprite.getHeight();
		
		int x = position.getX();
		int y = position.getY();
		
		texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2d(x, y);
			glTexCoord2f(1, 0);
			glVertex2d(x + width, y);
			glTexCoord2f(1, 1);
			glVertex2d(x + width, y - height);
			glTexCoord2f(0, 1);
			glVertex2d(x, y - height);
		glEnd();
	}
	
	public void exit() {
		Display.destroy();
		System.exit(0);
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}
}
