package se.chalmers.roguelike.Systems;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.input.Mouse;

import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.PlotAction;
import se.chalmers.roguelike.Components.PlotLoot;
import se.chalmers.roguelike.Components.Pocketable;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Seed;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
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
	private Entity player;
	private boolean registering;
	private String firstPlotText;

	/**
	 * Sets up a new instance of the overworld system.
	 * 
	 * @param engine the engine being used by the game
	 * @param firstPlotText
	 * @param firstScene 
	 */
	public OverworldSystem(Engine engine, String firstPlotText) {

		this.engine = engine;
		activeStar = null;
		this.firstPlotText = firstPlotText;
		popupActive = false;
		registering = false;
		popupQueue = new LinkedList<String>();
		stars = new HashMap<String, Entity>();
		starRectangles = new ArrayList<Rectangle>();
		playButton = engine.entityCreator.createButton(Engine.screenWidth - 80, 200, "button_play_gray", 80,
				32);
		menuButton = engine.entityCreator.createButton(Engine.screenWidth - 80, 200 - 32, "button_menu", 80,
				32);
		engine.addEntity(playButton);
		engine.addEntity(menuButton);
		menuRect = new Rectangle(Engine.screenWidth - 80, 200 - 32, 80, 32);
		unregister();
	}

	/**
	 * update function for the system. Currently handles the checks for plot
	 * related updates.
	 */
	public void update() {
		if (firstPlotText != null) {
			newPopup(firstPlotText);
			firstPlotText = null;
		}
		if (activeStar == null) {
			return;
		}

		Action action = activeStar.getComponent(PlotAction.class).getAction();
		if (action != null) {
			// This is where we check if there's a LAST plot action coupled with
			// the star
			if (action.getActionType() == Action.ActionType.LAST) {
				newPopup(activeStar.getComponent(PlotAction.class).getPlotText());
				activeStar.getComponent(PlotAction.class).setActionPerformed(true);
			}

			// This is where we check if there's a MEET plot action coupled with
			// the star
			if (action.getActionType() == Action.ActionType.MEET) {
				newPopup(activeStar.getComponent(PlotAction.class).getPlotText());
				activeStar.getComponent(PlotAction.class).setActionPerformed(true);
			}

			// This is where we check if there's a GIVE plot action coupled with
			// the star
			if (action.getActionType() == Action.ActionType.GIVE) {

				if (activeStar.getComponent(PlotAction.class).isMainCharacterSubject()) {
					// If the main character should give the item to another character

					// Check if the player has the relevant item
					boolean itemExists = false;
					ArrayList<Entity> items = player.getComponent(Inventory.class).getItems();
					for (Entity item : items) {
						if (item.containsComponent(Engine.CompPlotLoot)) {
							if (item.getComponent(PlotLoot.class).getProp().equals(action.getObjectProp())) {
								itemExists = true;
								break;
							}
						}
					}

					if (itemExists) {
						newPopup(activeStar.getComponent(PlotAction.class).getPlotText());
						activeStar.getComponent(PlotAction.class).setActionPerformed(true);
					}
				} else {
					// If the main character should be given the item

					// Create the item
					ArrayList<IComponent> components = new ArrayList<IComponent>();
					String name = "(Loot) " + action.getObjectProp();
					String sprite = "keycard_red";
					components.add(new Sprite(sprite));
					components.add(new Pocketable());
					components.add(new PlotLoot(action.getObjectProp()));
					Entity item = EntityCreator.createEntity(name, components);

					player.getComponent(Inventory.class).addItem(item);
					newPopup(activeStar.getComponent(PlotAction.class).getPlotText());
					activeStar.getComponent(PlotAction.class).setActionPerformed(true);
				}
			}

			Dungeon starDungeon = activeStar.getComponent(DungeonComponent.class).getDungeon();
			// This is where we check if there's a KILL or TAKE plot action coupled with
			// the star, and if the player has performed it
			if (((action.getActionType() == Action.ActionType.KILL) || (action.getActionType() == Action.ActionType.TAKE))
					&& (starDungeon != null)) {
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
		if (!registering) {
			if (entity.containsComponent(Engine.CompPlayer)) {
				// The entity is the player
				player = entity;
			} else {
				// It's a star
				int x = entity.getComponent(Position.class).getX();
				int y = entity.getComponent(Position.class).getY();
				Rectangle starRectangle = new Rectangle(x, y, 16, 16);
				starRectangles.add(starRectangle);
				stars.put((x + "," + y), entity);
				if (entity.containsComponent(Engine.CompFirstStarFlag)) {
					starClicked(starRectangle);
				}
			}
		}
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
				loadDungeon();
			} else if (menuRect.contains(mouseX, mouseY) && !popupActive) {
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
			long seed = activeStar.getComponent(Seed.class).getSeed();

			Random rand = new Random(seed);
			//percentage of dungeons to be generated as caves vs regular dungeons
			if (rand.nextInt(100) + 1 <= 50) {
				CellularLevelGenerator generator = new CellularLevelGenerator(50, 50, seed);
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
				if (activeStar.getComponent(PlotAction.class).getAction().getActionType() == Action.ActionType.TAKE) {
					starDungeon.addPlotLoot(activeStar.getComponent(PlotAction.class).getAction()
							.getObjectProp());
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
			engine.addEntity(playButton);
			playRect = new Rectangle(Engine.screenWidth - 80, 200, 80, 32);
		} else {
			activeStar.getComponent(SelectedFlag.class).setFlag(false); // deactivates current star
		}
		String coords = star.x + "," + star.y;
		activeStar = stars.get(coords);
		activeStar.getComponent(SelectedFlag.class).setFlag(true);

		// This is where we check if there's a VISIT plot action coupled with the star
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
		registering = true;
		for (Entity star : stars.values()) {
			engine.addEntity(star);
		}
		if (playButton != null) {
			engine.addEntity(playButton);
		}
		if (menuButton != null) {
			engine.addEntity(menuButton);
		}
		registering = false;
	}

	/**
	 * Creates a number of stars
	 * 
	 * Currently not used, plotsystem now spawns the stars
	 */
	@SuppressWarnings("unused")
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
		Entity star = engine.entityCreator.createStar(x, y, seed, starname, false);
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
				"ok_button", 242, 64);
		engine.addEntity(popupButton);
		popupRect = new Rectangle((Engine.screenHeight / 2), (Engine.screenWidth / 3), 242, 64);
		Font font = new Font("Times New Roman", Font.BOLD, 14);
		TrueTypeFont ttf = new TrueTypeFont(font, false);
		StringBuilder sb = new StringBuilder();
		for (String word : s.split(" ")) {
			if (word.equals("\n")) {
				sequencedString.add(sb.toString());
				sb = new StringBuilder();
				continue;
			}
			if (ttf.getWidth(sb.toString() + " " + word) > 960) { // magic number
				//TODO why is this happening and why should it be so high?
				sequencedString.add(sb.toString());
				sb = new StringBuilder();
			}
			sb.append(word + " ");
		}
		sequencedString.add(sb.toString());
		popupText = sequencedString;
		popup = engine.entityCreator.createPopup(popupText, Engine.screenWidth / 2 - 250,
				Engine.screenHeight / 2 - 150, 540, 340);
		engine.addEntity(popup);
	}
}
