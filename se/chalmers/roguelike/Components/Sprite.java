package se.chalmers.roguelike.Components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * This is a component for sprites. It can take spritesheets and it can keep track of the coordinates for the
 * current sprite.
 * 
 * @author fabian
 */
public class Sprite implements IComponent {

	private static int STANDARD_SIZE = 16; // The size used in DC mode

	private Texture spritesheet;
	private int sizeX, sizeY; // both width and height, in pixels, of individual sprites
	private int spriteX, spriteY; // the (tile) coordinates for current sprite
	private boolean visible;
	private static HashMap<String, Texture> loadTextures = new HashMap<String, Texture>();
	private String spritename;
	private int layer;

	/**
	 * A constructor where the starting sprite always is the upper- leftmost one in the spritesheet and the
	 * sprite size is STANDARD_SIZE (see above).
	 * 
	 * @param fileName The name of the image file. We're assuming that all images are PNGs and that they're
	 *            all in the /resources/ directory. Example: For 'resources/guy.png', fileName is 'guy'.
	 */
	public Sprite(String fileName) {
		this(fileName, STANDARD_SIZE);
	}

	/**
	 * Sprite constructor used for square textures
	 * 
	 * @param fileName The name of the image file. We're assuming that all images are PNGs and that they're
	 *            all in the /resources/ directory. Example: For 'resources/guy.png', fileName is 'guy'.
	 * @param spriteSize The size of the individual sprites in the spritesheet.
	 * @param spriteX The tile x coord in the spritesheet for the starting sprite.
	 * @param spriteY The tile y coord in the spritesheet for the starting sprite.
	 * @param layer THe layer that the sprite should reside on
	 */
	public Sprite(String fileName, int spriteSize, int spriteX, int spriteY, int layer) {
		spritename = fileName;
		spritesheet = loadTexture(fileName);
		this.sizeX = spriteSize;
		this.sizeY = spriteSize;
		this.spriteX = spriteX;
		this.spriteY = spriteY;
		visible = true;
		this.layer = layer;
	}

	/**
	 * A constructor where the starting sprite always is the upper- leftmost one in the spritesheet.
	 * 
	 * @param fileName The name of the image file. We're assuming that all images are PNGs and that they're
	 *            all in the /resources/ directory. Example: For 'resources/guy.png', fileName is 'guy'.
	 * @param spriteSize The size of the individual sprites in the spritesheet.
	 */
	public Sprite(String fileName, int spriteSize) {
		this(fileName, spriteSize, 0, 0, 1);
	}

	/**
	 * Constructor used for non-square sprites.
	 * 
	 * @param fileName The name of the image file. We're assuming that all images are PNGs and that they're
	 *            all in the /resources/ directory. Example: For 'resources/guy.png', fileName is 'guy'.
	 * @param spriteWidth the width of the sprite
	 * @param spriteHeight the height of the sprite
	 */
	public Sprite(String fileName, int spriteWidth, int spriteHeight) {
		spritesheet = loadTexture(fileName);
		this.sizeX = spriteWidth;
		this.sizeY = spriteHeight;
		spriteX = 0;
		spriteY = 0;
		visible = true;
	}

	/**
	 * Loads a texture, raises an error if it goes wrong.
	 * 
	 * @param fileName
	 * @return the loaded texture
	 */
	private Texture loadTexture(String fileName) {
		Texture texture = loadTextures.get(fileName);
		if (texture == null) {
			try {
				texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("./resources/"
						+ fileName + ".png")));
				loadTextures.put(fileName, texture);
			} catch (FileNotFoundException e) {
				System.out.println("The file " + fileName + " does not exist");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return texture;
	}

	public Texture getTexture() {
		return spritesheet;
	}

	/**
	 * @deprecated Use getWidth and getHeight instead
	 * @return the size, which is both the width and the height of the individual sprites in pixels
	 */
	public int getSize() {
		return sizeX;
	}

	/**
	 * @return The upperleft x coord of the individual sprite according to OpenGL, which is a float between 0
	 *         and 1
	 */
	public float getUpperLeftX() {
		float x = ((float) spriteX * sizeX) / spritesheet.getTextureWidth();
		return x;
	}

	/**
	 * @return The upperleft y coord of the individual sprite according to OpenGL, which is a float between 0
	 *         and 1
	 */
	public float getUpperLeftY() {
		float y = ((float) spriteY * sizeY) / spritesheet.getTextureHeight();
		return y;
	}

	/**
	 * @return The lowerright x coord of the individual sprite according to OpenGL, which is a float between 0
	 *         and 1
	 */
	public float getLowerRightX() {
		float x = ((float) (spriteX * sizeX) + (sizeX)) / spritesheet.getTextureWidth();
		return x;
	}

	/**
	 * @return The lowerright y coord of the individual sprite according to OpenGL, which is a float between 0
	 *         and 1
	 */
	public float getLowerRightY() {
		float y = ((float) (spriteY * sizeY) + (sizeY)) / spritesheet.getTextureHeight();
		return y;
	}

	/**
	 * 
	 * @return If the sprite should be drawn or not
	 */
	public boolean getVisibility() {
		return this.visible;
	}

	/**
	 * Set the tile x coord fo the current sprite i.e., change the current sprite.
	 * 
	 * @param spriteX
	 */
	public void setSpriteX(int spriteX) {
		this.spriteX = spriteX;
	}

	/**
	 * Set the tile y coord fo the current sprite i.e., change the current sprite.
	 * 
	 * @param spriteY
	 */
	public void setSpriteY(int spriteY) {
		this.spriteY = spriteY;
	}

	/**
	 * Set a boolean value if the sprite should be drawn.
	 * 
	 * @param visible
	 */
	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Returns the height of the sprite
	 * 
	 * @return the height of the sprite
	 */
	public int getHeight() {
		return sizeY;
	}

	/**
	 * Returns the width of the sprite
	 * 
	 * @return the width of the sprite
	 */
	public int getWidth() {
		return sizeX;
	}

	/**
	 * Sets a new texture for the sprite from a fileName, make sure the new one has the same dimensions.
	 * 
	 * @param fileName
	 */
	public void setSpritesheet(String fileName) {
		spritename = fileName;
		spritesheet = loadTexture(fileName);
	}

	/**
	 * Returns the filename of the texture
	 * 
	 * @return texture name
	 */
	public String getSpriteName() {
		return spritename;
	}

	/**
	 * Clones the sprite
	 */
	public IComponent clone() {
		Sprite s = new Sprite(spritename, sizeX, sizeY);
		s.setVisibility(visible);
		s.setLayer(layer);
		return s;
	}

	/**
	 * Returns the layer that the sprite resides on
	 * 
	 * @return the layer the sprite resides on
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * Sets the layer that the sprite should reside on
	 * 
	 * @param layer the new layer number
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}
}