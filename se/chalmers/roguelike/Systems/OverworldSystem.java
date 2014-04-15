package se.chalmers.roguelike.Systems;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.input.Mouse;

import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.PlotAction;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Seed;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.World.CellularLevelGenerator;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.LevelGenerator;
import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.util.TrueTypeFont;

/**
 * A system for the overworld. Will handle the logic and control of it.
 */
public class OverworldSystem implements ISystem, Observer {
	private Engine engine;
	private ArrayList<Rectangle> starRectangles;

	private Entity activeStar;
	private HashMap<String, Entity> stars;
	final Position noClick = new Position(-1, -1);
	private Rectangle playRect, menuRect, popupRect;
	private Entity playButton, menuButton, popupButton;

	public boolean popupActive;
	private ArrayList<String> popupText;
	private Entity popup;
	private LinkedList<String> popupQueue;

	/**
	 * Sets up a new instance of the overworld system.
	 * 
	 * @param engine the engine being used by the game
	 */
	public OverworldSystem(Engine engine) {
		this.engine = engine;
		activeStar = null;
		popupActive = false;
		popupQueue = new LinkedList<String>();
		stars = new HashMap<String, Entity>();
		starRectangles = new ArrayList<Rectangle>();
		playButton = engine.entityCreator.createButton(Engine.screenWidth - 80, 200, "button_play_gray", 80,
				32);
		menuButton = engine.entityCreator.createButton(Engine.screenWidth - 80, 200 - 32, "button_menu", 80,
				32);
		menuRect = new Rectangle(Engine.screenWidth - 80, 200 - 32, 80, 32);
		// setupStars(); //TODO kommer troligt vara onödig då PlotSystem nu
		// gör
		// detta
		unregister();
	}

	/**
	 * update function for the system. Currently handles the checks for plot
	 * related updates.
	 */
	public void update() {
		if (activeStar == null) {
			return;
		}

		Action action = activeStar.getComponent(PlotAction.class).getAction();
		if (action != null) {
			// This is where we check if there's a MEET plot action coupled with
			// the star
			if (action.getActionType() == Action.ActionType.MEET) {
				newPopup(activeStar.getComponent(PlotAction.class).getPlotText());
				activeStar.getComponent(PlotAction.class).setActionPerformed(true);
			}

			Dungeon starDungeon = activeStar.getComponent(DungeonComponent.class).getDungeon();
			// This is where we check if there's a KILL plot action coupled with
			// the star, and if the player has performed it
			if ((action.getActionType() == Action.ActionType.KILL) & (starDungeon != null)) {
				if (starDungeon.getPlotAccomplished()) {
					newPopup(activeStar.getComponent(PlotAction.class).getPlotText());
					activeStar.getComponent(PlotAction.class).setActionPerformed(true);
				}
			}
		}
	}

	/**
	 * Adds an entity to the overworld system
	 * 
	 * @param entity the entity that should be added to the system
	 */
	public void addEntity(Entity entity) {
		int x = entity.getComponent(Position.class).getX();
		int y = entity.getComponent(Position.class).getY();
		starRectangles.add(new Rectangle(x, y, 16, 16));// test case
		stars.put((x + "," + y), entity);
	}

	/**
	 * Removes an entity from the system
	 * 
	 * @param entity the entity that should be removed from the system
	 */
	public void removeEntity(Entity entity) {
		// TODO not necessary?
		// Or should it be possible to visit other galaxies?
	}

	/**
	 * Handles the input and determines if anything needs to run.
	 */
	public void notify(Enum<?> i) {
		if (Engine.gameState == Engine.GameState.OVERWORLD && i.equals(InputManager.InputAction.MOUSECLICK)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			for (Rectangle star : starRectangles) {
				if (!star.contains(mouseX, mouseY))
					continue;
				starClicked(star);
			}
			if (playRect != null && playRect.contains(mouseX, mouseY) && !popupActive) {
				System.out.println("PLAY PRESSED!");
				loadDungeon();
			} else if (menuRect.contains(mouseX, mouseY) && !popupActive) {
				// engine.loadMainMenu();
				newPopup("Whatever nulla incididunt, delectus tousled bespoke Marfa gluten-free. Cliche biodiesel quinoa letterpress incididunt Thundercats keffiyeh hoodie scenester actually. Vice disrupt VHS, pariatur eu esse messenger bag hashtag leggings. Viral velit vegan selfies gluten-free fashion axe, ex deep v Austin culpa skateboard church-key bespoke delectus twee. Pariatur kitsch fixie occaecat excepteur Williamsburg. Next level hoodie distillery fap, non mlkshk blog 8-bit chia minim Etsy. Sunt deserunt actually Banksy deep v.");
			} else if (popupActive && popupRect.contains(mouseX, mouseY)) {
				engine.removeEntity(popupButton);
				engine.removeEntity(popup);
				popupButton = null;
				popup = null;
				popupActive = false;
				if (popupQueue.size() != 0) {
					createTextPopup(popupQueue.poll());
				}
			}
		}
	}

	/**
	 * Triggers the loading of a dungeon.
	 */
	private void loadDungeon() {
		Dungeon starDungeon = activeStar.getComponent(DungeonComponent.class).getDungeon();
		if (starDungeon == null) {
			System.out.println("No dungeon found! Generating one.");
			long seed = activeStar.getComponent(Seed.class).getSeed();
			
			Random rand = new Random(seed);
			//percentage of dungeons to be generated as caves vs regular dungeons
			if (rand.nextInt(100)+1 <= 50) {
				CellularLevelGenerator generator = new CellularLevelGenerator(50,50,seed);
				starDungeon = generator.getDungeon();
			} else {
				LevelGenerator generator = new LevelGenerator(seed);
				starDungeon = generator.getDungeon();
			}
			activeStar.getComponent(DungeonComponent.class).setDungeon(starDungeon);

			if (activeStar.getComponent(PlotAction.class).getAction() != null) {
				if (activeStar.getComponent(PlotAction.class).getAction().getActionType() == Action.ActionType.KILL) {
					starDungeon.addBoss(activeStar.getComponent(PlotAction.class).getAction()
							.getObjectActor());
				}
			}
		}

		unregister();
		engine.loadDungeon(starDungeon, starDungeon.getStartpos().getX(), starDungeon.getStartpos().getY());
	}

	/**
	 * Makes a star active if it was clicked and adds a play-button.
	 * 
	 * @param star the star rectangle that was clicked
	 */
	private void starClicked(Rectangle star) {
		if (activeStar == null) {
			engine.removeEntity(playButton);
			playButton = engine.entityCreator.createButton(Engine.screenWidth - 80, 200, "button_play", 80,
					32);
			playRect = new Rectangle(Engine.screenWidth - 80, 200, 80, 32);
		} else {
			activeStar.getComponent(SelectedFlag.class).setFlag(false); // deactivates
																		// current
																		// star
		}
		String coords = star.x + "," + star.y;
		activeStar = stars.get(coords);
		activeStar.getComponent(SelectedFlag.class).setFlag(true);

		// This is where we check if there's a VISIT plot action coupled with
		// the star
		Action action = activeStar.getComponent(PlotAction.class).getAction();
		if (action != null) {
			if (action.getActionType() == Action.ActionType.VISIT) {
				newPopup(activeStar.getComponent(PlotAction.class).getPlotText());
				activeStar.getComponent(PlotAction.class).setActionPerformed(true);
			}
		}
	}

	/**
	 * Unregisters all the entities from the engine and its systems. Used when
	 * switching from the overworld.
	 */
	public void unregister() {

		for (Entity star : stars.values()) {
			engine.removeEntity(star);
		}
		engine.removeEntity(playButton);
		engine.removeEntity(menuButton);
	}

	/**
	 * Registers all the stars and other entities. Used when restoring the
	 * overworld system.
	 */
	public void register() {
		// Register all the stars
		for (Entity star : stars.values()) {
			engine.addEntity(star);
		}
		if (playButton != null) {
			engine.addEntity(playButton);
		}
		if (menuButton != null) {
			engine.addEntity(menuButton);
		}
	}

	/**
	 * Creates a number of stars
	 */
	private void setupStars() {
		int radius = 50;

		Random rand = new Random(1234L); // Make seed dependent later

		for (int i = 1; i < 360 * 3; i += 45) {
			double rad = i * Math.PI / 180;
			int x = (int) (radius * Math.cos(rad) + 400);
			int y = (int) (radius * Math.sin(rad) + 400);
			createStar(x, y, rand.nextLong(), ("Star" + i / 15));
			radius += 10;
		}
	}

	/**
	 * Creates a star for the overworld and adds it to the star hashmap.
	 * 
	 * @param x x-coordinate for the star
	 * @param y y-coordinate for the star
	 * @param seed the seed that the stars dungeon will be using
	 * @param starname the name of the star
	 */
	private void createStar(int x, int y, long seed, String starname) {
		Entity star = engine.entityCreator.createStar(x, y, seed, starname);
		starRectangles.add(new Rectangle(x, y, 16, 16));// test case
		stars.put((x + "," + y), star);
	}

	private void newPopup(String s) {
		if (!popupActive) {
			createTextPopup(s);
		} else {
			popupQueue.add(s);
		}
	}

	private void createTextPopup(String s) {

		ArrayList<String> sequencedString = new ArrayList<String>();

		popupActive = true;
		popupButton = engine.entityCreator.createButton(Engine.screenHeight / 2, Engine.screenWidth / 3,
				"play_button_selected", 242, 64);
		popupRect = new Rectangle(Engine.screenHeight / 2, Engine.screenWidth / 3, 242, 64);
		Font font = new Font("Times New Roman", Font.BOLD, 14);
		TrueTypeFont ttf = new TrueTypeFont(font, false);
		StringBuilder sb = new StringBuilder();
		for (String word : s.split(" ")) {
			if (ttf.getWidth(sb.toString() + " " + word) > 1350) { // magic
																	// number
																	// //TODO
																	// why is
																	// this
																	// happening
																	// and why
																	// should it
																	// be so
																	// high?
				sequencedString.add(sb.toString());
				sb = new StringBuilder();
			}
			sb.append(word + " ");
		}
		popupText = sequencedString;
		popup = engine.entityCreator.createPopup(popupText, Engine.screenWidth / 2 - 250,
				Engine.screenHeight / 2 - 150, 500, 300);
		engine.addEntity(popup);
	}
}
