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
import java.util.Collections;

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
import se.chalmers.roguelike.Components.Gold;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.PopupText;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Tile;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.util.FontRenderer;
import se.chalmers.roguelike.util.ShadowCaster;
import se.chalmers.roguelike.util.SpriteComparator;
import se.chalmers.roguelike.util.TrueTypeFont;

/**
 * This is the system that draws everything to be drawn. It knows of all
 * entities with both position and sprites, and those are the components which
 * it uses.
 */
public class RenderingSystem implements ISystem {

	// The entities that the RenderingSystem draws
	private ArrayList<Entity> entitiesToDraw;
	private Camera camera;
	private Entity player;
	private FontRenderer fontRenderer;
	private Dungeon dungeon;
	// TODO: Move these two away from here
	private Texture owBackground = null;
	private Texture owMenu = null;
	private SpriteComparator spriteComparator;

	private int[][] lightMap;

	private TrueTypeFont font;

	private final int DISPLAY_WIDTH = Engine.screenWidth;
	private final int DISPLAY_HEIGHT = Engine.screenHeight;

	private final int menuWidth = 200;
	private final int minimapWidth = menuWidth;
	private final int minimapHeight = menuWidth;

	/**
	 * Constructor for the rendering system. Sets up the necessary things for
	 * LWJGL to work properly.
	 */
	public RenderingSystem() {
		// Magic tricks done by lwjgl
		setupDisplay();
		setupOpenGL();
		try {
			fontRenderer = new FontRenderer(new UnicodeFont("/resources/fonts/circula-medium.otf", 28, false,
					true), Color.white);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		fontRenderer.load();

		// Initialize the list of entities to be drawn
		entitiesToDraw = new ArrayList<Entity>();
		spriteComparator = new SpriteComparator();

		// Font
		Font awtFont = new Font("/resources/fonts/circula-medium.otf", Font.BOLD, 14);
		font = new TrueTypeFont(awtFont, false);

		/*
		 * This part is just to test it out, figure out a better way of loading
		 * the texture and where to store it (outside of ECS?) and remove later
		 */
		try {
			owBackground = TextureLoader.getTexture("PNG", new FileInputStream(new File("./resources/"
					+ "background_ow" + ".png")));
			owMenu = TextureLoader.getTexture("PNG", new FileInputStream(new File("./resources/"
					+ "menu_background_ow_long" + ".png")));
		} catch (FileNotFoundException e) {
			System.out.println("The file does not exist");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method sets up OpenGL. We're not quite sure of what it does.
	 */
	private void setupOpenGL() {

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, 1, -1);

		// gluPerspective((float) 30, 1024f / 768f, 0.001f, 100);
		glMatrixMode(GL_MODELVIEW);
		// glEnable(GL_TEXTURE_2D);
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
				if (modes[i].getWidth() == DISPLAY_WIDTH && modes[i].getHeight() == DISPLAY_HEIGHT
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
	 * Will draw the dungeon
	 */
	private void drawDungeon() {
		// Sets the cameras position to the current position of the player
		int cwidth = camera.getWidth();
		int cheight = camera.getHeight();

		Position playerPos = player.getComponent(Position.class);
		camera.setPosition(new Position(playerPos.getX() - cwidth / 2, playerPos.getY() - cheight / 2));

		// Clear the window
		glClear(GL_COLOR_BUFFER_BIT);

		Position pos = new Position(playerPos.getX() - cwidth / 2, playerPos.getY() - cheight / 2);

		ShadowCaster sc = new ShadowCaster();

		lightMap = sc.calculateFOV(dungeon, playerPos.getX(), playerPos.getY(), 25);

		// This code draws out the background sprites for all tiles in the
		// camera's view
		Position drawPos = new Position(pos.getX(), pos.getY());

		for (int x = 0; x < dungeon.getWorldWidth(); x++) {
			for (int y = 0; y < dungeon.getWorldHeight(); y++) {
				Tile tile = dungeon.getTile(x, y);
				drawPos.set(x, y);
				if (camera.contains(drawPos)) {
					if (tile != null && (Engine.debug || lightMap[x][y] == 1)) {
						if (!Engine.debug)
							tile.setHasBeenSeen(true);
						draw(tile.getSprite(), drawPos);
					} else if (tile != null && tile.hasBeenSeen()) {
						glColor3f(0.5f, 0.5f, 0.5f);
						draw(tile.getSprite(), drawPos);
						glColor3f(1.0f, 1.0f, 1.0f);
					}
					if (tile != null && (Engine.debug || tile.hasBeenSeen())) {
						// Tiles within of the camera view that will be drawn on
						// minimap
						drawMinimap(tile.getSprite(), drawPos);
					}
				} else if (tile != null && (Engine.debug || tile.hasBeenSeen())) {
					// Tiles outside of the camera view that will be drawn on
					// minimap
					glColor3f(0.5f, 0.5f, 0.5f);
					drawMinimap(tile.getSprite(), drawPos);
					glColor3f(1.0f, 1.0f, 1.0f);
				}
			}
		}
	}

	/**
	 * update should be run of every iteration of the game loop.
	 * 
	 * Based on what the state the game engine is set to it will draw different
	 * things.
	 */
	public void update() {
		if (Engine.gameState == Engine.GameState.DUNGEON) {
			drawDungeon();
			// Draws healthbars for all entities that stand on a lit tile.
			for (Entity e : entitiesToDraw) {
				if ((e.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer) {
					drawHud(e);
				}
				if ((e.getComponentKey() & Engine.CompHealth) == Engine.CompHealth) {
					Position epos = e.getComponent(Position.class);
					if (Engine.debug || lightMap[epos.getX()][epos.getY()] == 1)
						drawHealthbar(e);
				}
			}
			// Draw all entities in system if they stand on a lit tile
			for (Entity entity : entitiesToDraw) {
				Position epos = entity.getComponent(Position.class);
				if ((entity.getComponentKey() & Engine.CompHighlight) == Engine.CompHighlight)
					draw(entity.getComponent(Sprite.class), entity.getComponent(Position.class));
				else if (Engine.debug || lightMap[epos.getX()][epos.getY()] == 1)
					draw(entity.getComponent(Sprite.class), entity.getComponent(Position.class));
			}
		} else if (Engine.gameState == Engine.GameState.OVERWORLD) {
			drawBackground();
			drawMenuOW();
			Entity activeStar = null;
			for (Entity entity : entitiesToDraw) {
				SelectedFlag flag = entity.getComponent(SelectedFlag.class);
				if (flag != null && flag.getFlag()) {
					activeStar = entity;
					glColor3f(1.0f, 0.0f, 0.0f);
					drawNonTile(entity.getComponent(Sprite.class), entity.getComponent(Position.class));
					glColor3f(1.0f, 1.0f, 1.0f);

				} else if (entity.getComponent(PopupText.class) != null) {
					drawNonTile(entity.getComponent(Sprite.class), entity.getComponent(Position.class));
					ArrayList<String> popupText = entity.getComponent(PopupText.class).getText();
					Position drawPos = new Position(entity.getComponent(Position.class).getX(), entity
							.getComponent(Position.class).getY() + 300 - font.getHeight());
					for (String s : popupText) {
						font.drawString(drawPos.getX(), drawPos.getY(), s);
						drawPos.setY(drawPos.getY() - font.getHeight());

					}
				} else {
					drawNonTile(entity.getComponent(Sprite.class), entity.getComponent(Position.class));
				}
			}
			/*
			 * For some reason if we try to draw the text in the above loop, it
			 * will mess up the textures for all the stars that are after the
			 * one that is select. That is, if you select star 3, then 1-3 will
			 * render fine, while star >3 will be broken. This is why I'm doing
			 * it here instead after the loop.
			 */
			if (activeStar != null) {
				String visited = activeStar.getComponent(DungeonComponent.class).getDungeon() == null ? "no"
						: "yes";
				font.drawString(Engine.screenWidth - 170, 300, "Selected star:");
				glColor3f(0.06f, 0.61f, 0.65f);
				font.drawString(Engine.screenWidth - 170, 300, "\n" + activeStar.toString());
				glColor3f(1.0f, 1.0f, 1.0f);
				font.drawString(Engine.screenWidth - 170, 300, "\n\nVisited before:");
				glColor3f(0.06f, 0.61f, 0.65f);
				font.drawString(Engine.screenWidth - 78, 300, "\n\n" + visited);
				glColor3f(1.0f, 1.0f, 1.0f);	}
		} else if (Engine.gameState == Engine.GameState.MAIN_MENU) {
			drawBackground();
			for (Entity e : entitiesToDraw) {
				drawNonTile(e.getComponent(Sprite.class), e.getComponent(Position.class));
			}
		}

		// Update and sync display
		Display.update();
		Display.sync(60);
	}

	/**
	 * Method to draw an entity to the game window.
	 * 
	 * @param sprite
	 *            The sprite that should be drawn on the screen
	 * @param position
	 *            The position where the sprite should be drawn
	 */
	private void draw(Sprite sprite, Position position) {
		if (!sprite.getVisibility())
			return;

		int size = Engine.spriteSize;

		// Get the camera's position
		Position camPos = camera.getPosition();
		int camX = camPos.getX();
		int camY = camPos.getY();
		// Subtract the coordinates with the camera's coordinates,
		// then multiply that with the SPRITE_SIZE, so that we get
		// the pixel coordinates, not the tile coordinates.
		int x = (position.getX() - camX) * size;
		int y = (position.getY() - camY) * size;

		// We determine if the entity is within the camera's
		// view; if so, we draw it
		if (x >= 0 && x < camera.getWidth() * size && y >= 0 && y < camera.getHeight() * size) {
			drawSprite(sprite, x, y, size, size);
		}
	}

	/**
	 * Method to draw non-tile objects
	 * 
	 * @param sprite
	 *            The sprite that should be drawn on the screen
	 * @param position
	 *            The position where the sprite should be drawn
	 */
	private void drawNonTile(Sprite sprite, Position position) {
		int sizeX = sprite.getWidth();
		int sizeY = sprite.getHeight();
		int x = position.getX();
		int y = position.getY();

		drawSprite(sprite, x, y, sizeX, sizeY);
	}

	/**
	 * Will draw the background used in the overworld and menu
	 */
	private void drawBackground() {
		glClear(GL_COLOR_BUFFER_BIT);

		int sizeX = Engine.screenWidth;
		int sizeY = Engine.screenHeight;

		float spriteULX = 0.0f;
		float spriteULY = 0.0f;
		float spriteLRX = ((float) (sizeX)) / owBackground.getTextureWidth();
		float spriteLRY = ((float) (sizeY)) / owBackground.getTextureHeight();
		drawTexturedQuad(owBackground, 0, 0, Engine.screenWidth, Engine.screenHeight, spriteULX, spriteULY,
				spriteLRX, spriteLRY);
	}

	/**
	 * Draws the menu for the overworld
	 */
	private void drawMenuOW() {

		int x = Engine.screenWidth - 180;
		int y = 0;
		int sizeX = 180;
		int sizeY = Engine.screenHeight;

		float spriteULX = 0.0f;
		float spriteULY = 0.0f;
		float spriteLRX = ((float) (sizeX)) / owMenu.getTextureWidth();
		float spriteLRY = ((float) (sizeY)) / owMenu.getTextureHeight();
		// Make owMenu a sprite later?
		drawTexturedQuad(owMenu, x, y, sizeX, sizeY, spriteULX, spriteULY, spriteLRX, spriteLRY);
	}

	/**
	 * Draws the minimap
	 * 
	 * @param sprite
	 *            the sprite of the tile you want to draw
	 * @param position
	 *            the position of the tile (will automatically be adjusted to
	 *            the minimap location
	 */
	private void drawMinimap(Sprite sprite, Position position) {
		if (!sprite.getVisibility())
			return;

		int minimapX = (Engine.screenWidth - minimapWidth);
		int minimapY = Engine.screenHeight - minimapHeight;
		int size = 1;
		int camX = camera.getPosition().getX();
		int camY = camera.getPosition().getY();

		int x = (position.getX() - camX) * size + minimapX + minimapWidth / 2 - camera.getWidth() * size
				/ (2);
		int y = (position.getY() - camY) * size + minimapY + minimapHeight / 2 - camera.getHeight() * size
				/ (2);
		if (x < minimapX || y < minimapY) {
			return;
		}
		drawSprite(sprite, x, y, size, size);
	}

	/**
	 * Draws a sprite on the screen
	 * 
	 * @param sprite
	 *            the sprite that should be drawn
	 * @param x
	 *            the X-coordinate for the quad
	 * @param y
	 *            the Y-coordinate for the quad
	 * @param width
	 *            the width of the quad
	 * @param height
	 *            the height of the quad
	 */
	private void drawSprite(Sprite sprite, int x, int y, int width, int height) {
		Texture texture = sprite.getTexture();

		float spriteULX = sprite.getUpperLeftX();
		float spriteULY = sprite.getUpperLeftY();
		float spriteLRX = sprite.getLowerRightX();
		float spriteLRY = sprite.getLowerRightY();

		drawTexturedQuad(texture, x, y, width, height, spriteULX, spriteULY, spriteLRX, spriteLRY);
	}

	/**
	 * Draws a textured quad on the screen
	 * 
	 * @param texture
	 *            the texture the quad is using
	 * @param x
	 *            the X-coordinate for the quad
	 * @param y
	 *            the Y-coordinate for the quad
	 * @param width
	 *            the width of the quad
	 * @param height
	 *            the height of the quad
	 * @param spriteULX
	 *            upper left X sprite value
	 * @param spriteULY
	 *            upper left Y sprite value
	 * @param spriteLRX
	 *            lower right X sprite value
	 * @param spriteLRY
	 *            lower right Y sprite value
	 */
	private void drawTexturedQuad(Texture texture, int x, int y, int width, int height, float spriteULX,
			float spriteULY, float spriteLRX, float spriteLRY) {

		texture.bind();
		glEnable(GL_BLEND); // remove? this should make sure that textures are
		// enabled
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

	/**
	 * Draws an untextured quad
	 * 
	 * @param x
	 *            the X-coordinate for the quad
	 * @param y
	 *            the Y-coordinate for the quad
	 * @param width
	 *            the width of the quad
	 * @param height
	 *            the height of the quad
	 */
	private void drawUntexturedQuad(int x, int y, int width, int height) {
		glDisable(GL11.GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		glVertex2d(x, y);
		glVertex2d(x + width, y);
		glVertex2d(x + width, y + height);
		glVertex2d(x, y + height);
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Exits the application and shuts down the display settings
	 */
	public void exit() {
		Display.destroy();
		System.exit(0);
	}

	/**
	 * Adds an entity to the system
	 * 
	 * @param entity
	 *            the entity that should be added
	 */
	public void addEntity(Entity entity) {
		if ((entity.getComponentKey() & Engine.CompPlayer) == Engine.CompPlayer) {
			this.player = entity;
		}
		entitiesToDraw.add(entity);
		Collections.sort(entitiesToDraw,spriteComparator);
	}

	/**
	 * Removes an entity from the system
	 * 
	 * @param entity
	 *            the entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		entitiesToDraw.remove(entity);
	}

	/**
	 * Sets the camera
	 * 
	 * @param c
	 *            the camera
	 */
	public void setCamera(Camera c) {
		this.camera = c;
	}

	/**
	 * draws healthbars for all entities that have health
	 * 
	 * @param e
	 *            the entity that should have its healthbar drawn
	 */
	private void drawHealthbar(Entity e) {
		Position epos = e.getComponent(Position.class); // tilebased positions
		Position camPos = camera.getPosition();
		int camX = camPos.getX();
		int camY = camPos.getY();

		int x = (epos.getX() - camX) * Engine.spriteSize;
		int y = (epos.getY() - camY) * Engine.spriteSize;

		Health h = e.getComponent(Health.class);
		double healthPercentage = h.getHealthPercentage();
		// calculates how much of the green should be drawn over the red
		double healthbarLength = healthPercentage * Engine.spriteSize;

		int hblength = (int) healthbarLength;
		// draw the red part of the healthbar
		glColor3f(1.0f, 0.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * Engine.spriteSize && y >= 0
				&& y < camera.getHeight() * Engine.spriteSize) {
			drawUntexturedQuad(x, y + Engine.spriteSize, Engine.spriteSize, Engine.spriteSize / 8);
		}

		// draw the green part of the healthbar ontop of the red
		glColor3f(0.0f, 1.0f, 0.0f);
		if (x >= 0 && x < camera.getWidth() * Engine.spriteSize && y >= 0
				&& y < camera.getHeight() * Engine.spriteSize) {
			drawUntexturedQuad(x, y + Engine.spriteSize, hblength, Engine.spriteSize / 8);
		}
		glColor3f(1.0f, 1.0f, 1.0f);
	}

	/**
	 * Draws the HUD
	 * 
	 * @param e
	 *            the player entity that will have its information printed on
	 *            the HUD
	 */
	private void drawHud(Entity e) {

		// e will always be the player here
		Attribute attributes = e.getComponent(Attribute.class);
		Weapon weapon = e.getComponent(Weapon.class);
		Health health = e.getComponent(Health.class);
		Gold gold = e.getComponent(Gold.class);
		double hp = health.getHealth();
		double hpPercentage = health.getHealthPercentage();
		double maxHp = health.getMaxHealth();

		String info = "Name: " + attributes.getName() + "\nGold: " + gold.getGold() + "\nLevel: "
				+ attributes.getLevel() + "\nXP: " + attributes.experience() + "\nStrength: "
				+ attributes.strength() + "\nEndurance: " + attributes.endurance() + "\nPerception: "
				+ attributes.perception() + "\nIntelligence: " + attributes.intelligence() + "\nCharisma: "
				+ attributes.charisma() + "\nAgility: " + attributes.agility() + "\n"
				+ "\nWeapon information: " + "\nDamage: " + weapon.getNumberOfDice() + "D6"
				+ (weapon.getModifier() == 0 ? "" : "+" + weapon.getModifier()) + "\nRange: "
				+ weapon.getRange() + "\nTargeting system: " + weapon.getTargetingSystemString();

		// draw hp bar
		glColor3f(1.0f, 0.0f, 0.0f); // red part
		drawUntexturedQuad(Engine.screenWidth - (menuWidth - 10), Engine.screenHeight - menuWidth - 20,
				(int) (menuWidth * 0.9), 17);
		glColor3f(0.0f, 1.0f, 0.0f); // green part
		drawUntexturedQuad(Engine.screenWidth - (menuWidth - 10), Engine.screenHeight - menuWidth - 20,
				(int) (menuWidth * 0.9 * hpPercentage), 17);
		glColor3f(1.0f, 1.0f, 1.0f);
		font.drawString(Engine.screenWidth - menuWidth / 2, Engine.screenHeight - menuWidth - 22, (int) hp
				+ "/" + (int) maxHp);
		font.drawString(Engine.screenWidth - menuWidth, Engine.screenHeight - menuWidth - 20 - 20, info);
		
		if (Engine.debug) {
			Position position = e.getComponent(Position.class);
			String debug = "Player position: " + position.getX() + "x" + position.getY();
			font.drawString(0, 0, debug);
		}
		// draw inventory
		Inventory inv = e.getComponent(Inventory.class);
		ArrayList<Entity> items = inv.getItems();
		// below the background to the inventory-positions will be drawn
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				drawSprite(new Sprite("noslipfloor"), Engine.screenWidth - 200 + 4 + i * Engine.spriteSize
						* 2, 20 + j * Engine.spriteSize * 2, Engine.spriteSize * 2, Engine.spriteSize * 2);
			}
		}
		int drawX = Engine.screenWidth - menuWidth + 4;
		int drawY = 20 + Engine.spriteSize * 2 * 5; // times two for double
													// size, and times 5 for
													// size of inventory (6x6)
		for (Entity ent : items) { // this will draw all the items currently in
									// the inventory
			drawSprite(ent.getComponent(Sprite.class), drawX, drawY, Engine.spriteSize * 2,
					Engine.spriteSize * 2);
			if (drawX + Engine.spriteSize >= Engine.screenWidth) {
				drawX = Engine.screenWidth - menuWidth + 4;
				drawY += Engine.spriteSize * 2;
			} else {
				drawX += Engine.spriteSize * 2;
			}

		}
	}

	/**
	 * Updates the dungeon being rendered.
	 * 
	 * @param dungeon
	 *            the current dungeon being used
	 */
	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
}
