package se.chalmers.roguelike.Components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * 
 * This is a component for sprites. It can take spritesheets and it can keep
 * track of the coordinates for the current sprite.
 * 
 * @author fabian
 *
 */
public class Sprite implements IComponent {
	private static int STANDARD_SIZE = 32; // The size used in DC mode
	
	private Texture spritesheet;
	private int size; // both width and height, in pixels, of individual sprites
	private int spriteX, spriteY; // the (tile) coordinates for current sprite
	
	public Sprite(String fileName) {
		this(fileName, STANDARD_SIZE);
	}
	
	/**
	 * @param fileName The name of the image file. We're assuming
	 * that all images are PNGs and that they're all in the /resources/
	 * directory. Example: For 'resources/guy.png', fileName is 'guy'.
	 * @param spriteSize The size of the individual sprites in the
	 * spritesheet.
	 * @param spriteX The tile x coord in the spritesheet for the 
	 * starting sprite.
	 * @param spriteY The tile y coord in the spritesheet for the 
	 * starting sprite.
	 */
	public Sprite(String fileName, int spriteSize, int spriteX, int spriteY) {
		spritesheet = loadTexture(fileName);
		this.size = spriteSize;
		this.spriteX = spriteX;
		this.spriteY = spriteY;
	}
	
	/**
	 * A constructor where the starting sprite always is the upper-
	 * leftmost one in the spritesheet.
	 * 
	 * @param fileName The name of the image file. We're assuming
	 * that all images are PNGs and that they're all in the /resources/
	 * directory. Example: For 'resources/guy.png', fileName is 'guy'.
	 * @param spriteSize The size of the individual sprites in the
	 * spritesheet.
	 */
	public Sprite(String fileName, int spriteSize) {
		this(fileName, spriteSize, 0, 0);
	}
	
	/**
	 * Loads a texture, raises an error if it goes wrong.
	 * @param fileName
	 * @return the loaded texture
	 */
	private Texture loadTexture(String fileName) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", 
					new FileInputStream(new File("./resources/" + fileName + ".png")));
		} catch (FileNotFoundException e) {
			System.out.println("The file does not exist");
			e.printStackTrace();
			// borde stänga ner displayen och stänga av programmet också
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// borde stänga ner displayen och stänga av programmet också
		}
		
		return texture;
	}
	
	public Texture getTexture() {
		return spritesheet;
	}
	
	/**
	 * @return the size, which is both the width and the height 
	 * of the individual sprites in pixels
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @return The upperleft x coord of the individual sprite according to OpenGL,
	 * which is a float between 0 and 1
	 */
	public float getUpperLeftX() {
		float x = ((float) spriteX * size) / spritesheet.getTextureWidth(); 
		return x;
	}

	/**
	 * @return The upperleft y coord of the individual sprite according to OpenGL,
	 * which is a float between 0 and 1
	 */
	public float getUpperLeftY() {
		float y = ((float) spriteY * size) / spritesheet.getTextureHeight(); 
		return y;
	}
	
	/**
	 * @return The lowerright x coord of the individual sprite according to OpenGL,
	 * which is a float between 0 and 1
	 */
	public float getLowerRightX() {
		float x = ((float) (spriteX * size) + (size - 1)) / spritesheet.getTextureWidth(); 
		return x;
	}
	
	/**
	 * @return The lowerright y coord of the individual sprite according to OpenGL,
	 * which is a float between 0 and 1
	 */
	public float getLowerRightY() {
		float y = ((float) (spriteY * size) + (size - 1)) / spritesheet.getTextureHeight(); 
		return y;
	}
	
	/**
	 * Set the tile x coord fo the current sprite
	 * i.e., change the current sprite.
	 * @param spriteX
	 */
	public void setSpriteX(int spriteX) {
		this.spriteX = spriteX;
	}
	
	/**
	 * Set the tile y coord fo the current sprite
	 * i.e., change the current sprite.
	 * @param spriteY
	 */
	public void setSpriteY(int spriteY) {
		this.spriteY = spriteY;
	}
}
