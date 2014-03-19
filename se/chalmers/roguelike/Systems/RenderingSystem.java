package se.chalmers.roguelike.Systems;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
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
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Weapon;
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

		// These for-loops are more effective, but doesn't show the whole map for the minimap:
//		for(int x = pos.getX()-cwidth/2; x < pos.getX() + cwidth; x++) {
//			for(int y = pos.getY()-cheight/2; y < pos.getY() + cheight; y++) {
		
		for(int x = 0; x < dungeon.getWorldWidth(); x++) {
			for(int y = 0; y < dungeon.getWorldHeight(); y++) {
				Tile tile = dungeon.getTile(x,y);
				drawPos.set(x, y);
				if(x < pos.getX() + cwidth && y < pos.getY() + cheight){
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
				if(tile != null && tile.hasBeenSeen()){
					drawMinimap(tile.getSprite(), drawPos);
				}
			}
		}
	}
	
	public void update() {
		if(Engine.gameState == Engine.GameState.DUNGEON){
			// Draws healthbars for all entities that stand on a lit tile.
			for (Entity e : entitiesToDraw) {
				if((e.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer){
					drawHud(e);
				}
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
			drawBackground();
			drawMenuOW();
			Entity activeStar = null;
			for(Entity entity : entitiesToDraw) {
				SelectedFlag flag = entity.getComponent(SelectedFlag.class);
				if(flag != null && flag.getFlag()){
					activeStar = entity;
					glColor3f(1.0f, 0.0f, 0.0f);
//					font.drawString(300, 300, "buggy line");
					drawNonTile(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
					glColor3f(1.0f, 1.0f, 1.0f);
				} else {
					drawNonTile(entity.getComponent(Sprite.class),entity.getComponent(Position.class));
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
		} else if(Engine.gameState == Engine.GameState.MAIN_MENU) {
			drawBackground();
			for (Entity e : entitiesToDraw) {
				drawNonTile(e.getComponent(Sprite.class),e.getComponent(Position.class));
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

//        gluPerspective((float) 30, 1024f / 768f, 0.001f, 100);
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
			DisplayMode displayMode = null;
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == DISPLAY_WIDTH
						&& modes[i].getHeight() == DISPLAY_HEIGHT
						&& modes[i].isFullscreenCapable()) {
					displayMode = modes[i];
				}
			}
			if (displayMode == null) {
				displayMode = new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT);
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
			drawTexturedQuad(texture, x, y, size, size, spriteULX, spriteULY, spriteLRX, spriteLRY);
			
		}
	}
	
	/**
	 * Method to draw an the overworld
	 * @param entity The entity to be drawn
	 */
	private void drawNonTile(Sprite sprite, Position position) {

		
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
		
		drawTexturedQuad(texture, x, y, sizeX, sizeY, spriteULX, spriteULY, spriteLRX, spriteLRY);

	}

	private void drawBackground(){
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
		drawTexturedQuad(owBackground, 0, 0, Engine.screenWidth, Engine.screenHeight, spriteULX, spriteULY, spriteLRX, spriteLRY);
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
		drawTexturedQuad(owMenu, x, y, sizeX, sizeY, spriteULX, spriteULY, spriteLRX, spriteLRY);
	}
	private void drawMinimap(Sprite sprite, Position position) {
		if(!sprite.getVisibility())
			return;
		// TODO: Fix so that the values -200 etcs arent just hardcoded but can easily
		// be changed, and fixed so the minimap doesn't cover anything else and that it
		// only shows a radius of the players
		Texture texture = sprite.getTexture();
		int size = 2; //Engine.spriteSize;//sprite.getSize(); // Times two, makes sprites twice as large

		int x = position.getX() * size + (Engine.screenWidth-200);
		int y = position.getY() * size + (Engine.screenHeight-200);
		
//		int x = (position.getX() - camX) * size;
//		int y = (position.getY() - camY) * size;
		
		// Get the coordinates of the current sprite
		// in the spritesheet in a form that OpenGL likes,
		// which is a float between 0 and 1
		float spriteULX = sprite.getUpperLeftX();
		float spriteULY = sprite.getUpperLeftY();
		float spriteLRX = sprite.getLowerRightX();
		float spriteLRY = sprite.getLowerRightY();
		
		
		drawTexturedQuad(texture, x, y, size, size, spriteULX, spriteULY, spriteLRX, spriteLRY);
	}
	private void drawTexturedQuad(Texture texture, int x, int y, int width, int height, 
			float spriteULX, float spriteULY, float spriteLRX, float spriteLRY){
		
		texture.bind();
		glEnable(GL_BLEND); // remove? this should make sure that textures are enabled
		glBegin(GL_QUADS);
			glTexCoord2f(spriteULX, spriteLRY);
			glVertex2d(x, y);
			glTexCoord2f(spriteLRX, spriteLRY);
			glVertex2d(x + width, y);
			glTexCoord2f(spriteLRX, spriteULY);
			glVertex2d(x + width, y + height);
			glTexCoord2f(spriteULX, spriteULY);
			glVertex2d(x, y + height);
		glEnd();
	}
	
	private void drawUntexturedQuad(int x, int y, int width, int height){
		glDisable(GL11.GL_TEXTURE_2D);
		glBegin(GL_QUADS);
//			GL11.glVertex2i(x,y+height);
//			GL11.glVertex2i(x+width,y+height);
//			GL11.glVertex2i(x+width,y+height);
//			GL11.glVertex2i(x,y+height);
			glVertex2d(x, y);
			glVertex2d(x + width, y);
			glVertex2d(x + width, y + height);
			glVertex2d(x, y + height);
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
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
	
	public void setCamera(Camera c) {
		this.camera = c;
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
		//draw the red part of the healthbar
		glColor3f(1.0f, 0.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * Engine.spriteSize &&
				y >= 0 && y < camera.getHeight() * Engine.spriteSize) {
			drawUntexturedQuad(x, y+Engine.spriteSize, Engine.spriteSize, Engine.spriteSize/8);
		}
		
		//draw the green part of the healthbar ontop of the red
		glColor3f(0.0f, 1.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * Engine.spriteSize &&
				y >= 0 && y < camera.getHeight() * Engine.spriteSize) {
			drawUntexturedQuad(x, y+Engine.spriteSize, hblength, Engine.spriteSize/8);
				
		}
		glColor3f(1.0f,1.0f,1.0f);
	}

	private void drawHud(Entity e){
		int menuWidth = 200;
		
		// e will always be the player here
		Attribute attributes = e.getComponent(Attribute.class);
		Weapon weapon = e.getComponent(Weapon.class);
		Health health = e.getComponent(Health.class); 
		double hp = health.getHealth();
		double hpPercentage = health.getHealthPercentage();
		double maxHp = health.getMaxHealth();

		String info = "Name: "+attributes.getName() +
				"\nLevel "+attributes.getLevel()+
				"\nXP: "+attributes.experience()+
				"\nStrength: " + attributes.strength() + 
				"\nEndurance: " + attributes.endurance() + 
				"\nPerception: " + attributes.perception() +
				"\nIntelligence: " + attributes.intelligence() + 
				"\nCharisma: " + attributes.charisma() + 
				"\nAgility: " + attributes.agility() +
				"\n" +
				"\nWeapon information: " +
				"\nDamage: " + weapon.getNumberOfDice() +"D6"+
				(weapon.getModifier() == 0 ? "" : "+"+weapon.getModifier()) +
				"\nRange: " + weapon.getRange() +
				"\nTargeting system: " + weapon.getTargetingSystemString(); 
		
		
		
		// draw hp bar
		glColor3f(1.0f, 0.0f, 0.0f); // red part
		drawUntexturedQuad(Engine.screenWidth-(menuWidth-10),Engine.screenHeight-menuWidth-20,(int)(menuWidth*0.9),17);
		glColor3f(0.0f, 1.0f, 0.0f); // green part
		drawUntexturedQuad(Engine.screenWidth-(menuWidth-10),Engine.screenHeight-menuWidth-20,(int)(menuWidth*0.9*hpPercentage),17);
		glColor3f(1.0f,1.0f,1.0f);
		font.drawString(Engine.screenWidth-menuWidth/2,Engine.screenHeight-menuWidth-22,(int)hp+"/"+(int)maxHp);
		font.drawString(Engine.screenWidth-menuWidth, Engine.screenHeight-menuWidth-20-20, info); // -20 due to HP bar 
	}
}


