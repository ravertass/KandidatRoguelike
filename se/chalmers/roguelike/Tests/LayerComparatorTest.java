package se.chalmers.roguelike.Tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.SpriteComparator;


public class LayerComparatorTest {
	public static void main(String[] args) {
		setupDisplay(); // needed for the sprite loading to work
		ArrayList<Entity> entities = new ArrayList<Entity>();
		Random rand = new Random();
		
		for(int i=0;i<10;i++){
			Sprite sprite = new Sprite("brick", 0, 0, 0, rand.nextInt(5)+1);
			Entity entity = new Entity(""+i);
			entity.add(sprite);
			entities.add(entity);
		}
		System.out.println("Unsorted:");
		for(Entity e : entities){
			System.out.println("Entity: "+e + " Sprite layer: "+ e.getComponent(Sprite.class).getLayer());
		}
		Collections.sort(entities,new SpriteComparator());
		System.out.println("\n===========================");
		for(Entity e : entities){
			System.out.println("Entity: "+e + " Sprite layer: "+ e.getComponent(Sprite.class).getLayer());
		}
		
	}
	
	private static void setupDisplay() {
		try {
			DisplayMode displayMode = null;
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == 600 && modes[i].getHeight() == 600
						&& modes[i].isFullscreenCapable()) {
					displayMode = modes[i];
				}
			}
			if (displayMode == null) {
				displayMode = new DisplayMode(600, 600);
			}
			Display.setDisplayMode(displayMode);
			Display.setFullscreen(false);
			Display.setTitle("AstRogue");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}
}
