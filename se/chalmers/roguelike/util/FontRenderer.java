package se.chalmers.roguelike.util;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class FontRenderer {
	
	private UnicodeFont font;
	private Color color;
	
	public FontRenderer()
	{
		this(new UnicodeFont(new Font("Times New Roman", Font.BOLD, 20)),
							 Color.white);
	}
	public FontRenderer(UnicodeFont font, Color color)
	{
		this.font = font;
		this.color = color;
	}
	
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

	public void draw(int x, int y, String text) {
		font.drawString(x, y, text);
	}
}