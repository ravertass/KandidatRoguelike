package se.chalmers.roguelike.Systems;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Engine.GameState;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Text;
import se.chalmers.roguelike.util.Observer;

/**
 * The main menu system sets up and manages the main menu of the game.
 */
public class MenuSystem implements ISystem, Observer {

	private Rectangle playRect, optionsRect, exitRect, loadRect, tutorialPlayRect, newGameRect,
			gameOverNewGameRect, gameOverExitRect, race1Rect, race2Rect, race3Rect, class1Rect, class2Rect,
			class3Rect;
	private Entity playButton, optionsButton, exitButton, loadButton, tutorialPlayButton, seedInfo, seedBox,
			newGameButton, tutorial, gameOverLogo, gameOverNewGame, gameOverExit, race1, race2, race3,
			raceText, classText, class1, class2, class3;

	private ArrayList<Entity> activeButtons;

	public enum MenuState {
		MAINMENU, NEWGAME, TUTORIAL, GAMEOVER
	}

	private MenuState state = MenuState.MAINMENU;

	private Engine engine;

	private String tmpSeed;
	private Entity selectedRace, selectedClass;

	/**
	 * Sets up the buttons and internal logic required for the main menu
	 * 
	 * @param e
	 *            engine being used by the game
	 */
	public MenuSystem(Engine e) {
		this.engine = e;
		tmpSeed = String.valueOf(Engine.seed);
		activeButtons = new ArrayList<Entity>();

		playButton = engine.entityCreator.createButton(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 + 120, "play_button", 242, 62);
		activeButtons.add(playButton);
		engine.addEntity(playButton);
		playRect = new Rectangle(Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 + 120, 242, 60);

		loadButton = engine.entityCreator.createButton(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 + 60, "load_button", 242, 62);
		activeButtons.add(loadButton);
		engine.addEntity(loadButton);
		loadRect = new Rectangle(Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 + 60, 242, 60);

		optionsButton = engine.entityCreator.createButton(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30, "options_button", 242, 62);
		activeButtons.add(optionsButton);
		engine.addEntity(optionsButton);
		optionsRect = new Rectangle(Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30, 242, 60);

		exitButton = engine.entityCreator.createButton(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 - 60, "exit_button", 242, 62);
		engine.addEntity(exitButton);
		activeButtons.add(exitButton);
		exitRect = new Rectangle(Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 - 60, 242, 60);

	}

	/**
	 * Runs with each iteration of the game loop when the game state is set to MAIN_MENU.
	 * 
	 * Will highlight the buttons when hovered over
	 */
	public void update() {
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		resetButtonTextures();
		if (state == MenuState.MAINMENU) {
			if (playRect != null && playRect.contains(mouseX, mouseY)) {
				playButton.getComponent(Sprite.class).setSpritesheet("play_button_selected");
			} else if (loadRect != null && loadRect.contains(mouseX, mouseY)) {
				loadButton.getComponent(Sprite.class).setSpritesheet("load_button_selected");
			} else if (optionsRect != null && optionsRect.contains(mouseX, mouseY)) {
				optionsButton.getComponent(Sprite.class).setSpritesheet("options_button_selected");
			} else if (exitRect != null && exitRect.contains(mouseX, mouseY)) {
				exitButton.getComponent(Sprite.class).setSpritesheet("exit_button_selected");
			}
		} else if (state == MenuState.TUTORIAL) {
			if (tutorialPlayRect != null && tutorialPlayRect.contains(mouseX, mouseY)) {
				tutorialPlayButton.getComponent(Sprite.class).setSpritesheet("play_button_selected");
			} else if (tutorialPlayRect != null && !tutorialPlayRect.contains(mouseX, mouseY)) {
				tutorialPlayButton.getComponent(Sprite.class).setSpritesheet("play_button");
			}
		} else if (state == MenuState.NEWGAME) {
			if (newGameRect != null && newGameRect.contains(mouseX, mouseY)) {
				newGameButton.getComponent(Sprite.class).setSpritesheet("play_button_selected");
			} else if (newGameRect != null && !newGameRect.contains(mouseX, mouseY)) {
				newGameButton.getComponent(Sprite.class).setSpritesheet("play_button");
			}
		} else if (state == MenuState.GAMEOVER) {
			if (gameOverNewGameRect != null && gameOverNewGameRect.contains(mouseX, mouseY)) {
				gameOverNewGame.getComponent(Sprite.class).setSpritesheet("play_button_selected");
			} else if (gameOverNewGameRect != null && !gameOverNewGameRect.contains(mouseX, mouseY)) {
				gameOverNewGame.getComponent(Sprite.class).setSpritesheet("play_button");
			} 
			if (gameOverExitRect != null && gameOverExitRect.contains(mouseX, mouseY)) {
				gameOverExit.getComponent(Sprite.class).setSpritesheet("exit_button_selected");
			} else if (gameOverExitRect != null && !gameOverExitRect.contains(mouseX, mouseY)) {
				gameOverExit.getComponent(Sprite.class).setSpritesheet("exit_button");
			} 
		}
	}

	/**
	 * Adds an entity to the main menu system, currently not used
	 */
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub

	}

	/**
	 * Removes an entity to the main menu system, currently not used
	 */
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub

	}

	/**
	 * Runs when input is received
	 */
	public void notify(Enum<?> i) {
		if ((Engine.gameState == Engine.GameState.MAIN_MENU || Engine.gameState == Engine.GameState.GAMEOVER)
				&& i.equals(InputManager.InputAction.MOUSECLICK)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			if (state == MenuState.MAINMENU) {
				if (playRect != null && playRect.contains(mouseX, mouseY)) {
					newGameMenu();
				} else if (loadRect != null && loadRect.contains(mouseX, mouseY)) {
					// Temporarily using the load button for tutorial
					tutorial();
				} else if (optionsRect != null && optionsRect.contains(mouseX, mouseY)) {
					System.out.println("Someone implement the options plz");
				} else if (exitRect != null && exitRect.contains(mouseX, mouseY)) {
					System.exit(1); // TODO not the right thing to do but will
									// do for now
				}
			} else if (state == MenuState.TUTORIAL) {
				if (tutorialPlayRect != null && tutorialPlayRect.contains(mouseX, mouseY)) {
					mainMenu();
				}
			} else if (state == MenuState.NEWGAME) {
				if (newGameRect != null && newGameRect.contains(mouseX, mouseY)) {
					loadNewGame();
				} else if (race1Rect.contains(mouseX, mouseY)) {
					// This code gets a bit copy-pasty
					if (selectedRace != null) {
						selectedRace.getComponent(SelectedFlag.class).setFlag(false);
					}
					race1.getComponent(SelectedFlag.class).setFlag(true);					
					selectedRace = race1;
				} else if (race2Rect.contains(mouseX, mouseY)) {
					if (selectedRace != null) {
						selectedRace.getComponent(SelectedFlag.class).setFlag(false);
					}
					race2.getComponent(SelectedFlag.class).setFlag(true);
					selectedRace = race2;
				} else if (race3Rect.contains(mouseX, mouseY)) {
					if (selectedRace != null) {
						selectedRace.getComponent(SelectedFlag.class).setFlag(false);
					}
					race3.getComponent(SelectedFlag.class).setFlag(true);
					selectedRace = race3;
				} else if (class1Rect.contains(mouseX, mouseY)) {
					// This code gets a bit copy-pasty
					if (selectedClass != null) {
						selectedClass.getComponent(SelectedFlag.class).setFlag(false);
					}
					class1.getComponent(SelectedFlag.class).setFlag(true);
					selectedClass = class1;
				} else if (class2Rect.contains(mouseX, mouseY)) {
					if (selectedClass != null) {
						selectedClass.getComponent(SelectedFlag.class).setFlag(false);
					}
					class2.getComponent(SelectedFlag.class).setFlag(true);
					selectedClass = class2;
				} else if (class3Rect.contains(mouseX, mouseY)) {
					if (selectedClass != null) {
						selectedClass.getComponent(SelectedFlag.class).setFlag(false);
					}
					class3.getComponent(SelectedFlag.class).setFlag(true);
					selectedClass = class3;
				}
			} else if (state == MenuState.GAMEOVER) {
				if (gameOverNewGameRect.contains(mouseX, mouseY)) {
					mainMenu();
				} else if (gameOverExitRect.contains(mouseX, mouseY)) {
					System.exit(1);
				}
			}
		}

		// Keyboard input
		if (Engine.gameState == Engine.GameState.MAIN_MENU && state == MenuState.NEWGAME) {
			if (i == InputAction.NUM_0) {
				seedAdd(0);
			} else if (i == InputAction.NUM_1) {
				seedAdd(1);
			} else if (i == InputAction.NUM_2) {
				seedAdd(2);
			} else if (i == InputAction.NUM_3) {
				seedAdd(3);
			} else if (i == InputAction.NUM_4) {
				seedAdd(4);
			} else if (i == InputAction.NUM_5) {
				seedAdd(5);
			} else if (i == InputAction.NUM_6) {
				seedAdd(6);
			} else if (i == InputAction.NUM_7) {
				seedAdd(7);
			} else if (i == InputAction.NUM_8) {
				seedAdd(8);
			} else if (i == InputAction.NUM_9) {
				seedAdd(9);
			} else if (i == InputAction.BACKSPACE && tmpSeed.length() > 0) {
				tmpSeed = tmpSeed.substring(0, tmpSeed.length() - 1);
			} else if (i == InputAction.ENTER) {
				loadNewGame();
			}
			if (seedBox != null) {
				seedBox.getComponent(Text.class).setText(tmpSeed);
			}
		}

	}

	/**
	 * This method will be called when switching to the MainMenu gamestate and will register all the entities
	 * with engine.
	 */
	public void register() {
		for (Entity e : activeButtons) {
			if (e != null)
				engine.addEntity(e);
		}

	}

	/**
	 * Unregisters all the menu buttons from the engine
	 */
	public void unregister() {
		for (Entity e : activeButtons) {
			engine.removeEntity(e);
		}
	}

	/**
	 * Resets the texture of the menu buttons
	 */
	private void resetButtonTextures() {
		playButton.getComponent(Sprite.class).setSpritesheet("play_button");
		loadButton.getComponent(Sprite.class).setSpritesheet("load_button");
		optionsButton.getComponent(Sprite.class).setSpritesheet("options_button");
		exitButton.getComponent(Sprite.class).setSpritesheet("exit_button");
	}

	/**
	 * Sets up the tutorial screen
	 */
	private void tutorial() {
		state = MenuState.TUTORIAL;

		unregister();
		// showMenuButtons(false);

		int width = 242;
		int height = 64;
		int x = Engine.screenWidth / 2 - width / 2;
		int y = 20;

		if (tutorialPlayButton == null) {
			tutorialPlayButton = engine.entityCreator.createButton(x, y, "play_button", width, height);
			tutorialPlayRect = new Rectangle(x, y, width, height);
		}

		if (tutorial == null) {
			tutorial = new Entity("Tutorial");
			Sprite sprite = new Sprite("misc/tutorial", 800, 600);
			sprite.setLayer(5);
			Position pos = new Position(50, 150);
			tutorial.add(sprite);
			tutorial.add(pos);
		}
		engine.addEntity(tutorial);
		engine.addEntity(tutorialPlayButton);
		activeButtons.add(tutorialPlayButton);
		activeButtons.add(tutorial);
	}

	/**
	 * Sets up the new game screen where seed can be entered
	 */
	private void newGameMenu() {
		// showMenuButtons(false);
		unregister();
		state = MenuState.NEWGAME;
		int bufferHeight = 150;
		if (seedInfo == null) {
			seedInfo = new Entity("Seed information");
			seedInfo.add(new Sprite("misc/seedtext", 260, 26));
			seedInfo.add(new Position(Engine.screenWidth / 2 - 260 / 2, Engine.screenHeight - 100
					- bufferHeight));
		}
		if (seedBox == null) {
			seedBox = new Entity("Seed box");
			seedBox.add(new Sprite("misc/seedbox", 260, 26));
			seedBox.add(new Position(Engine.screenWidth / 2 - 260 / 2, Engine.screenHeight - 146
					- bufferHeight));
			seedBox.add(new Text(tmpSeed));
		}
		if (newGameButton == null) {
			int width = 242;
			int height = 64;
			int x = Engine.screenWidth / 2 - width / 2;
			int y = Engine.screenHeight - 146 - height - 20 - 120 - bufferHeight; // Hard coded values for now
			newGameRect = new Rectangle(x, y, width, height);
			newGameButton = engine.entityCreator.createButton(x, y, "play_button", width, height);
		}
		if (race1 == null) {
			// If race1 is null, all three will be null

			int raceWidth = 84;
			int classWidth = 100;
			int logoHeight = 26;
			int height = 14;
			int race1X = Engine.screenWidth / 2 - raceWidth - raceWidth / 3;
			int race1Y = Engine.screenHeight - 146 - 26 - 40 - bufferHeight; // Based on the seed box
			int classX = race1X + raceWidth + 30;

			raceText = new Entity("RaceText");
			raceText.add(new Sprite("misc/race", raceWidth, logoHeight));
			raceText.add(new Position(race1X, race1Y));

			// Race 1
			race1 = new Entity("Race1");
			race1.add(new Sprite("misc/spacealien", raceWidth, height));
			race1.add(new Position(race1X, race1Y - logoHeight));
			race1.add(new SelectedFlag(true));
			race1Rect = new Rectangle(race1X, race1Y - logoHeight, raceWidth, height);
			selectedRace = race1;

			// Race 2
			race2 = new Entity("Race2");
			race2.add(new Sprite("misc/spacehuman", raceWidth, height));
			race2.add(new Position(race1X, race1Y - height - logoHeight));
			race2.add(new SelectedFlag(false));
			race2Rect = new Rectangle(race1X, race1Y - height - logoHeight, raceWidth, height);

			// Race 3
			race3 = new Entity("Race3");
			race3.add(new Sprite("misc/spacedwarf", raceWidth, height));
			race3.add(new Position(race1X, race1Y - 2 * height - logoHeight));
			race3.add(new SelectedFlag(false));
			race3Rect = new Rectangle(race1X, race1Y - 2 * height - logoHeight, raceWidth, height);

			// class stuff:
			classText = new Entity("ClassText");
			classText.add(new Sprite("misc/class", raceWidth, logoHeight));
			classText.add(new Position(classX, race1Y));

			// Class 1:
			class1 = new Entity("Class1");
			class1.add(new Sprite("misc/spacewarrior", classWidth, height));
			class1.add(new Position(classX, race1Y - logoHeight));
			class1.add(new SelectedFlag(true));
			class1Rect = new Rectangle(classX, race1Y - logoHeight, classWidth, height);
			selectedClass = class1;

			// Race 2
			class2 = new Entity("Class2");
			class2.add(new Sprite("misc/spacerogue", classWidth, height));
			class2.add(new Position(classX, race1Y - height - logoHeight));
			class2.add(new SelectedFlag(false));
			class2Rect = new Rectangle(classX, race1Y - height - logoHeight, classWidth, height);

			// Race 3
			class3 = new Entity("Class3");
			class3.add(new Sprite("misc/spacemage", classWidth, height));
			class3.add(new Position(classX, race1Y - 2 * height - logoHeight));
			class3.add(new SelectedFlag(false));
			class3Rect = new Rectangle(classX, race1Y - 2 * height - logoHeight, classWidth, height);
		}

		engine.addEntity(raceText);
		engine.addEntity(race1);
		engine.addEntity(race2);
		engine.addEntity(race3);
		engine.addEntity(class1);
		engine.addEntity(class2);
		engine.addEntity(class3);
		engine.addEntity(classText);
		engine.addEntity(seedInfo);
		engine.addEntity(seedBox);
		engine.addEntity(newGameButton);
		activeButtons.add(raceText);
		activeButtons.add(race1);
		activeButtons.add(race2);
		activeButtons.add(race3);
		activeButtons.add(class1);
		activeButtons.add(class2);
		activeButtons.add(class3);
		activeButtons.add(classText);
		activeButtons.add(newGameButton);
		activeButtons.add(seedInfo);
		activeButtons.add(seedBox);
	}

	/**
	 * Adds an additional digit if possible to the seed
	 * 
	 * @param value
	 *            digit that should be added
	 */
	private void seedAdd(int value) {
		String newSeed = tmpSeed + value;
		try {
			Long.parseLong(newSeed);
		} catch (NumberFormatException e) {
			System.out.println("Seed too long. Max seed: " + Long.MAX_VALUE);
			return;
		}

		tmpSeed = newSeed;
	}

	private void loadNewGame() {
		unregister();
		if (tmpSeed.length() == 0) {
			System.out.println("Seed required - random being used");
			Engine.seed = new Random().nextLong();
		} else {
			Engine.seed = Long.parseLong(tmpSeed);
		}
		SpaceRace race;
		if (selectedRace == race1) {
			race = SpaceRace.SPACE_ALIEN;
		} else if (selectedRace == race2) {
			race = SpaceRace.SPACE_HUMAN;
		} else {
			race = SpaceRace.SPACE_DWARF;
		}

		SpaceClass spaceClass;
		if (selectedClass == class1) {
			spaceClass = SpaceClass.SPACE_WARRIOR;
		} else if (selectedClass == class2) {
			spaceClass = SpaceClass.SPACE_ROGUE;
		} else {
			spaceClass = SpaceClass.SPACE_MAGE;
		}

		// Reset:
		selectedRace.getComponent(SelectedFlag.class).setFlag(false);
		race1.getComponent(SelectedFlag.class).setFlag(true);
		selectedRace = race1;
		selectedClass.getComponent(SelectedFlag.class).setFlag(false);
		class1.getComponent(SelectedFlag.class).setFlag(true);
		selectedClass = class1;
		engine.newGame(spaceClass, race);
	}

	private void mainMenu() {
		unregister();
		engine.addEntity(playButton);
		engine.addEntity(loadButton);
		engine.addEntity(optionsButton);
		engine.addEntity(exitButton);
		activeButtons.add(playButton);
		activeButtons.add(loadButton);
		activeButtons.add(optionsButton);
		activeButtons.add(exitButton);
		state = MenuState.MAINMENU;
		Engine.gameState = GameState.MAIN_MENU;
	}

	public void setState(MenuState state) {
		this.state = state;
		if (state == MenuState.GAMEOVER) {
			gameOver();
		}
	}

	public void gameOver() {
		unregister();
		if (gameOverLogo == null) {
			gameOverLogo = new Entity("Seed box");
			gameOverLogo.add(new Sprite("misc/gameover", 268, 52));
			gameOverLogo.add(new Position(Engine.screenWidth / 2 - 268 / 2, Engine.screenHeight - 300));
		}
		if (gameOverNewGame == null || gameOverExit == null) {
			int width = 242;
			int height = 62;
			int x = Engine.screenWidth / 2 - width;
			int y = Engine.screenHeight - 500;

			gameOverNewGame = engine.entityCreator.createButton(x, y, "play_button", width, height);
			gameOverNewGameRect = new Rectangle(x, y, width, height);
			gameOverExit = engine.entityCreator.createButton(x + width, y, "exit_button", width, height);
			gameOverExitRect = new Rectangle(x + width, y, width, height);
		}
		state = MenuState.GAMEOVER;
		activeButtons.add(gameOverLogo);
		engine.addEntity(gameOverLogo);
		activeButtons.add(gameOverNewGame);
		engine.addEntity(gameOverNewGame);
		activeButtons.add(gameOverExit);
		engine.addEntity(gameOverExit);
	}
}
