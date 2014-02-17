package se.chalmers.roguelike.util;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


/**
 * This class makes it possible to write a string in the graphical display
 *
 */
public class FontRenderer {
	
	private UnicodeFont font;
	private Color color;
	
	/**
	 * as default this prints in Times New Roman
	 */
	public FontRenderer()
	{
		this(new UnicodeFont(new Font("Times New Roman", Font.BOLD, 20)),
							 Color.white);
	}
	
	/**
	 * this constructor makes it possible to use custom fonts
	 * 
	 * @param font	the font you want the text to be printed in 
	 * @param color
	 */
	public FontRenderer(UnicodeFont font, Color color)
	{
		this.font = font;
		this.color = color;
	}
	
	/**
	 * loads color and glyphs to the text you want to print in the future
	 * use before draw() to make the text graphically visible
	 */
	@SuppressWarnings("unchecked")
	public void load() {
		font.getEffects().add(new ColorEffect(color));
		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * draw the text					//possible to do this in the RenderingSystem?
	 * 
	 * @param x		x-coordinate on your screen where you want to print
	 * @param y 	y-coordinate on your screen where you want to print
	 * @param text	the string you want to print
	 */
	public void draw(int x, int y, String text) {
		font.drawString(x, y, text);
	}
}