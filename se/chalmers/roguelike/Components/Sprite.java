package se.chalmers.roguelike.Components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Sprite implements IComponent {
	private Texture texture;
	private int width, height;
	private int STANDARD_SIZE = 32;
	
	public Sprite(String fileName) {
		texture = loadTexture(fileName);
		width = STANDARD_SIZE;
		height = STANDARD_SIZE;
	}
	
	public Sprite(String fileName, int width, int height) {
		texture = loadTexture(fileName);
		this.width = width;
		this.height = height;
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
		return texture;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
