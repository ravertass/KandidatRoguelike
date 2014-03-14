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
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.util.FontRenderer;
import se.chalmers.roguelike.util.ShadowCaster;
import se.chalmers.roguelike.util.TrueTypeFont;

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
	// TODO: Move these two away from here
	Texture owBackground = null;
	Texture owMenu = null; 
	
	private int[][] lightMap;
	
	private final int DISPLAY_WIDTH = Engine.screenWidth;
	private final int DISPLAY_HEIGHT = Engine.screenHeight;
	TrueTypeFont font;
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

		// Font
		Font awtFont = new Font("Times New Roman", Font.BOLD, 14);
		font = new TrueTypeFont(awtFont, false);
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
		
		ShadowCaster sc = new ShadowCaster();
		
		lightMap = sc.calculateFOV(dungeon, playerPos.getX(), playerPos.getY(), 25);
		
		// This code draws out the background sprites for all tiles in the camera's view
		Position drawPos = new Position(pos.getX(), pos.getY());

		
		for(int x = pos.getX()-cwidth/2; x < pos.getX() + cwidth; x++) {
			for(int y = pos.getY()-cheight/2; y < pos.getY() + cheight; y++) {
				Tile tile = dungeon.getTile(x,y);
				drawPos.set(x, y);
				if(tile != null && (Engine.debug || lightMap[x][y] == 1)) {
					if(!Engine.debug)
						tile.setHasBeenSeen(true);
					draw(tile.getSprite(),drawPos);

				} else if(tile != null && tile.hasBeenSeen()) {
					glColor3f(0.5f, 0.5f, 0.5f);
					draw(tile.getSprite(), drawPos);
					glColor3f(1.0f, 1.0f, 1.0f);
				}
			}
		}
	}
	
	public void update() {
		if(Engine.gameState == Engine.GameState.DUNGEON){
			// Draws healthbars for all entities that stand on a lit tile.
			for (Entity e : entitiesToDraw) {
				if((e.getComponentKey() & Engine.CompHealth) == Engine.CompHealth){
					Position epos = e.getComponent(Position.class);
					if(Engine.debug || lightMap[epos.getX()][epos.getY()] == 1)
						drawHealthbar(e);
				}
			}
			// Draw all entities in system if they stand on a lit tile
			for(Entity entity : entitiesToDraw) {
				Position epos = entity.getComponent(Position.class);
				if((entity.getComponentKey() & Engine.CompHighlight) == Engine.CompHighlight)
					draw(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
				else if(Engine.debug || lightMap[epos.getX()][epos.getY()] == 1)
					draw(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
			}
		} else if(Engine.gameState == Engine.GameState.OVERWORLD) {
//			glClear(GL_COLOR_BUFFER_BIT); // clearas the window
			drawOWbackground();
			drawMenuOW();
			Entity activeStar = null;
			for(Entity entity : entitiesToDraw) {
				SelectedFlag flag = entity.getComponent(SelectedFlag.class);
				if(flag != null && flag.getFlag()){
					activeStar = entity;
					glColor3f(1.0f, 0.0f, 0.0f);
					drawOverworld(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
					glColor3f(1.0f, 1.0f, 1.0f);
				} else {
					drawOverworld(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
				}
			}
			/*
			 * For some reason if we try to draw the text in the above loop, it will
			 * mess up the textures for all the stars that are after  the one that is
			 * select. That is, if you select star 3, then 1-3 will render fine, while
			 * star >3 will be broken. This is why I'm doing it here instead after the 
			 * loop.
			 */
			if(activeStar != null){
				String visited = activeStar.getComponent(DungeonComponent.class).getDungeon() == null ? "no" : "yes";
				font.drawString(Engine.screenWidth-120, 300, "Selected star: "+activeStar.toString());
				font.drawString(Engine.screenWidth-120, 300, "\nVisited before: "+visited);
			}
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
			Display.setTitle("AstRogue");
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
		int size = Engine.spriteSize;//sprite.getSize(); // Times two, makes sprites twice as large
		//int size = sprite.getSize();
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
			drawQuad(texture, x, y, size, size, spriteULX, spriteULY, spriteLRX, spriteLRY);
			
		}
	}
	/**
	 * Method for drawing outshadowed entities in the gameworld.
	 * @param sprite
	 * @param position
	 */
	private void drawOutShadowed(Sprite sprite, Position position) {
		if(!sprite.getVisibility())
			return;
		
		Texture texture = sprite.getTexture();
		int size = Engine.spriteSize; // Times two, makes sprites twice as large
		
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
				glColor3f(0.5f, 0.5f, 0.5f);
				glTexCoord2f(spriteULX, spriteLRY);
				glVertex2d(x, y);
				glTexCoord2f(spriteLRX, spriteLRY);
				glVertex2d(x + size, y);
				glTexCoord2f(spriteLRX, spriteULY);
				glVertex2d(x + size, y + size);
				glTexCoord2f(spriteULX, spriteULY);
				glVertex2d(x, y + size);
				glColor3f(1.0f, 1.0f, 1.0f);
			glEnd();
		}
	}
	
	/**
	 * Method to draw an the overworld
	 * @param entity The entity to be drawn
	 */
	private void drawOverworld(Sprite sprite, Position position) {

		
		Texture texture = sprite.getTexture();
		int sizeX = sprite.getWidth(); 
		int sizeY = sprite.getHeight();
		int x = position.getX();
		int y = position.getY();
		
		// Get the coordinates of the current sprite
		// in the spritesheet in a form that OpenGL likes,
		// which is a float between 0 and 1
		float spriteULX = sprite.getUpperLeftX();
		float spriteULY = sprite.getUpperLeftY();
		float spriteLRX = sprite.getLowerRightX();
		float spriteLRY = sprite.getLowerRightY();
		
		drawQuad(texture, x, y, sizeX, sizeY, spriteULX, spriteULY, spriteLRX, spriteLRY);

	}

	private void drawOWbackground(){
		glClear(GL_COLOR_BUFFER_BIT); // clearas the window
		
		/* 
		 * This part is just to test it out, figure out a better way of loading 
		 * the texture and where to store it (outside of ECS?) and remove later
		 */
		if(owBackground == null){
			try {
				owBackground = TextureLoader.getTexture("PNG", 
						new FileInputStream(new File("./resources/" + "background_ow" + ".png")));
			} catch (FileNotFoundException e) {
				System.out.println("The file does not exist");
				e.printStackTrace();
				// borde stänga ner displayen och stänga av programmet också
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// borde stänga ner displayen och stänga av programmet också
			}
		}
		int sizeX = Engine.screenWidth; 
		int sizeY = Engine.screenHeight;

		float spriteULX = 0.0f;
		float spriteULY = 0.0f;
		float spriteLRX = ((float) (sizeX)) / owBackground.getTextureWidth();
		float spriteLRY = ((float) (sizeY)) / owBackground.getTextureHeight();
		drawQuad(owBackground, 0, 0, Engine.screenWidth, Engine.screenHeight, spriteULX, spriteULY, spriteLRX, spriteLRY);
	}
	
	private void drawMenuOW(){
		
		/* 
		 * This part is just to test it out, figure out a better way of loading 
		 * the texture and where to store it (outside of ECS?) and remove later
		 */
		if(owMenu == null){
			try {
				owMenu = TextureLoader.getTexture("PNG", 
						new FileInputStream(new File("./resources/" + "menu_background_ow" + ".png")));
			} catch (FileNotFoundException e) {
				System.out.println("The file does not exist");
				e.printStackTrace();
				// borde stänga ner displayen och stänga av programmet också
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// borde stänga ner displayen och stänga av programmet också
			}
		}
		int x = Engine.screenWidth-128;
		int y = 0;
		int sizeX = 128;
		int sizeY = Engine.screenHeight;

		float spriteULX = 0.0f;
		float spriteULY = 0.0f;
		float spriteLRX = ((float) (sizeX)) / owMenu.getTextureWidth();
		float spriteLRY = ((float) (sizeY)) / owMenu.getTextureHeight();
		drawQuad(owMenu, x, y, sizeX, sizeY, spriteULX, spriteULY, spriteLRX, spriteLRY);
	}
	
	private void drawQuad(Texture texture, int x, int y, int sizeX, int sizeY, 
			float spriteULX, float spriteULY, float spriteLRX, float spriteLRY){
		texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(spriteULX, spriteLRY);
			glVertex2d(x, y);
			glTexCoord2f(spriteLRX, spriteLRY);
			glVertex2d(x + sizeX, y);
			glTexCoord2f(spriteLRX, spriteULY);
			glVertex2d(x + sizeX, y + sizeY);
			glTexCoord2f(spriteULX, spriteULY);
			glVertex2d(x, y + sizeY);
		glEnd();
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
		
		int x = (epos.getX() - camX) * Engine.spriteSize;
		int y = (epos.getY() - camY) * Engine.spriteSize;
		
		Health h = e.getComponent(Health.class);
		double healthPercentage = h.getHealthPercentage();
		double healthbarLength = healthPercentage * Engine.spriteSize; //calculates how much of the green should be drawn over the red
		
		int hblength = (int)healthbarLength;
		glDisable(GL11.GL_TEXTURE_2D);
		//draw the red part of the healthbar
		glColor3f(1.0f, 0.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * Engine.spriteSize &&
				y >= 0 && y < camera.getHeight() * Engine.spriteSize) {
			glBegin(GL_QUADS);
				GL11.glVertex2i(x,y+Engine.spriteSize);
				GL11.glVertex2i(x+Engine.spriteSize,y+Engine.spriteSize);
				GL11.glVertex2i(x+Engine.spriteSize,y+Engine.spriteSize+Engine.spriteSize/8);
				GL11.glVertex2i(x,y+Engine.spriteSize+Engine.spriteSize/8);
			glEnd();
		}
		
		//draw the green part of the healthbar ontop of the red
		glColor3f(0.0f, 1.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * Engine.spriteSize &&
				y >= 0 && y < camera.getHeight() * Engine.spriteSize) {
			glBegin(GL_QUADS);
				GL11.glVertex2i(x,y+Engine.spriteSize);
				GL11.glVertex2i(x+hblength,y+Engine.spriteSize);
				GL11.glVertex2i(x+hblength,y+Engine.spriteSize+Engine.spriteSize/8);
				GL11.glVertex2i(x,y+Engine.spriteSize+Engine.spriteSize/8);
			glEnd();
				
		}
		glColor3f(1.0f,1.0f,1.0f);
		glEnable(GL11.GL_TEXTURE_2D);
	}

	
}


