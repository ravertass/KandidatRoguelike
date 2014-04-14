package se.chalmers.roguelike.Systems;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Text;
import se.chalmers.roguelike.util.Observer;

/**
 * The main menu system sets up and manages the main menu of the game.
 */
public class MainMenuSystem implements ISystem, Observer {

	private Rectangle playRect, optionsRect, exitRect, loadRect, tutorialPlayRect, newGameRect;
	private Entity playButton, optionsButton, exitButton, loadButton, tutorialPlayButton, seedInfo, seedBox,
		newGameButton, tutorial;

	private ArrayList<Entity> buttons;

	public enum MenuState {
		DEFAULT, NEWGAME, TUTORIAL
	}

	private MenuState state = MenuState.DEFAULT;
			
	private Engine engine;

	private String tmpSeed;
	
	/**
	 * Sets up the buttons and internal logic required for the main menu
	 * @param e engine being used by the game
	 */
	public MainMenuSystem(Engine e) {
		this.engine = e;
		tmpSeed = String.valueOf(Engine.seed);
		buttons = new ArrayList<Entity>();
		
		playButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 + 120,
				"play_button", 242, 62);
		buttons.add(playButton);
		playRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 + 120, 242, 60);
		
		
		loadButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 + 60,
				"load_button", 242, 62);
		buttons.add(loadButton);
		loadRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 + 60, 242, 60);
		
		
		optionsButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30,
				"options_button", 242, 62);
		buttons.add(optionsButton);
		optionsRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30, 242, 60);
		
		
		exitButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 - 60,
				"exit_button", 242, 62);
		buttons.add(exitButton);
		exitRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 - 60, 242, 60);


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
		if(state == MenuState.DEFAULT) {
			if(playRect != null && playRect.contains(mouseX,mouseY)){
				playButton.getComponent(Sprite.class).setSpritesheet("play_button_selected");
			} else if (loadRect != null && loadRect.contains(mouseX,mouseY)){
				loadButton.getComponent(Sprite.class).setSpritesheet("load_button_selected");
			} else if (optionsRect != null && optionsRect.contains(mouseX,mouseY)){
				optionsButton.getComponent(Sprite.class).setSpritesheet("options_button_selected");
			} else if (exitRect != null && exitRect.contains(mouseX,mouseY)){
				exitButton.getComponent(Sprite.class).setSpritesheet("exit_button_selected");
			}
		} else if(state == MenuState.TUTORIAL) {
			if (tutorialPlayRect != null && tutorialPlayRect.contains(mouseX,mouseY)) {
				tutorialPlayButton.getComponent(Sprite.class).setSpritesheet("play_button_selected");
			} else if (tutorialPlayRect != null && !tutorialPlayRect.contains(mouseX,mouseY)) {
				tutorialPlayButton.getComponent(Sprite.class).setSpritesheet("play_button");
			}
		} else if(state == MenuState.NEWGAME) {
			// Todo: Do something
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
		if (Engine.gameState == Engine.GameState.MAIN_MENU && i.equals(InputManager.InputAction.MOUSECLICK)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			if(state == MenuState.DEFAULT) {
				if(playRect != null && playRect.contains(mouseX,mouseY)){
				// engine.loadOverworld();
					newGame();
				} else if (loadRect != null && loadRect.contains(mouseX,mouseY)){
					// Temporarily using the load button for tutorial
					tutorial();
				} else if (optionsRect != null && optionsRect.contains(mouseX,mouseY)){
					System.out.println("Someone implement the options plz");
				} else if (exitRect != null && exitRect.contains(mouseX,mouseY)){
					System.exit(1); //TODO not the right thing to do but will do for now
				}
			} else if(state == MenuState.TUTORIAL) {
				if(tutorialPlayRect != null && tutorialPlayRect.contains(mouseX,mouseY)){
					engine.removeEntity(tutorialPlayButton);
					engine.removeEntity(tutorial);
					showMenuButtons(true);
					state = MenuState.DEFAULT;
				}
			} else if(state == MenuState.NEWGAME) {
				if(newGameRect != null && newGameRect.contains(mouseX,mouseY)) {
					engine.removeEntity(newGameButton);
					engine.removeEntity(seedBox);
					engine.removeEntity(seedInfo);
					if(tmpSeed.length() == 0){
						System.out.println("Seed required - random being used");
						Engine.seed = new Random().nextLong();
					} else {
						Engine.seed = Long.parseLong(tmpSeed);
					}
					engine.newGame();
				}
			}
		}
		
		if(Engine.gameState == Engine.GameState.MAIN_MENU) {
			if(i == InputAction.NUM_0){
				seedAdd(0);
			} else if(i == InputAction.NUM_1){
				seedAdd(1);
			} else if(i == InputAction.NUM_2){
				seedAdd(2);
			} else if(i == InputAction.NUM_3){
				seedAdd(3);
			} else if(i == InputAction.NUM_4){
				seedAdd(4);
			} else if(i == InputAction.NUM_5){
				seedAdd(5);
			} else if(i == InputAction.NUM_6){
				seedAdd(6);
			} else if(i == InputAction.NUM_7){
				seedAdd(7);
			} else if(i == InputAction.NUM_8){
				seedAdd(8);
			} else if(i == InputAction.NUM_9){
				seedAdd(9);
			} else if(i == InputAction.BACKSPACE && tmpSeed.length() > 0) {
				tmpSeed = tmpSeed.substring(0, tmpSeed.length()-1);
			}
			if(seedBox != null){
				seedBox.getComponent(Text.class).setText(tmpSeed);
			}
		}
	}

	/**
	 * This method will be called when switching to the MainMenu gamestate and
	 * will register all the entities with engine.
	 */
	public void register() {
		for (Entity e : buttons) {
			if(e != null)
				engine.addEntity(e);
		}

	}

	/**
	 * Unregisters all the menu buttons from the engine
	 */
	public void unregister() {
		for (Entity e : buttons) {
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
	private void tutorial(){
		state = MenuState.TUTORIAL;
		
		showMenuButtons(false);
		
		int width = 242;
		int height = 64;
		int x = Engine.screenWidth / 2-width/2;
		int y = 20;
		if(tutorialPlayRect == null){
			tutorialPlayRect = new Rectangle(x, y, width, height);
		}
		if(tutorialPlayButton == null){
			tutorialPlayButton = engine.entityCreator.createButton(x, y,"play_button", width, height);
		}
		tutorial = new Entity("Tutorial");
		Sprite sprite = new Sprite("misc/tutorial",800,600);
		sprite.setLayer(5);
		Position pos = new Position(50,150);
		tutorial.add(sprite);
		tutorial.add(pos);
		engine.addEntity(tutorial);
	}
	
	/**
	 * Registers or unregisters the menu buttons from the engine based on the new status
	 * @param newStatus false if they should be unregistered and not shown, true to register and show the 
	 * buttons
	 */
	private void showMenuButtons(boolean newStatus){
		if(!newStatus){
			engine.removeEntity(playButton);
			engine.removeEntity(loadButton);
			engine.removeEntity(optionsButton);
			engine.removeEntity(exitButton);
		} else {
			engine.addEntity(playButton);
			engine.addEntity(loadButton);
			engine.addEntity(optionsButton);
			engine.addEntity(exitButton);
		}
	}
	
	private void newGame() {
		showMenuButtons(false);
		state = MenuState.NEWGAME;
		if(seedInfo == null){
			seedInfo = new Entity("Seed information");
			seedInfo.add(new Sprite("misc/seedtext", 260, 26));
			seedInfo.add(new Position(Engine.screenWidth/2-260/2,Engine.screenHeight-100));
		}
		if(seedBox == null){
			seedBox = new Entity("Seed box");
			seedBox.add(new Sprite("misc/seedbox", 260, 26));
			seedBox.add(new Position(Engine.screenWidth/2-260/2,Engine.screenHeight-146));
			seedBox.add(new Text(tmpSeed));
		}
		if(newGameButton == null){
			int width = 242;
			int height = 64;
			int x = Engine.screenWidth / 2-width/2;
			int y = Engine.screenHeight-146-height-20;
			newGameRect = new Rectangle(x, y, width, height);
			newGameButton = engine.entityCreator.createButton(x, y,"play_button", width, height);
		}
		engine.addEntity(seedInfo);
		engine.addEntity(seedBox);
		System.out.println("Time for a new game!");
	}
	
	private void seedAdd(int value){
		String newSeed = tmpSeed + value;
		try{
			Long.parseLong(newSeed);
		} catch (NumberFormatException e){
			System.out.println("Seed too long. Max seed: "+Long.MAX_VALUE);
			return;
		}
		
		tmpSeed = newSeed;
	}
}
