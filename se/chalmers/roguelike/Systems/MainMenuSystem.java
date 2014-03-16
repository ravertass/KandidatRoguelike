package se.chalmers.roguelike.Systems;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.util.Observer;

public class MainMenuSystem implements ISystem, Observer {

	private Rectangle playRect, optionsRect, exitRect, loadRect;
	private Entity playButton, optionsButton, exitButton, loadButton, highLighted;
	
	private ArrayList<Entity> buttons;

	private Engine engine;

	public MainMenuSystem(Engine e) {
		this.engine = e;
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

	@Override
	public void update() {
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		resetButtonTextures();
		if(playRect != null && playRect.contains(mouseX,mouseY)){
			playButton.getComponent(Sprite.class).setSpritesheet("play_button_selected");
		} else if (loadRect != null && loadRect.contains(mouseX,mouseY)){
			loadButton.getComponent(Sprite.class).setSpritesheet("load_button_selected");
		} else if (optionsRect != null && optionsRect.contains(mouseX,mouseY)){
			optionsButton.getComponent(Sprite.class).setSpritesheet("options_button_selected");
		} else if (exitRect != null && exitRect.contains(mouseX,mouseY)){
			exitButton.getComponent(Sprite.class).setSpritesheet("exit_button_selected");
		}
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notify(Enum<?> i) {
		if (Engine.gameState == Engine.GameState.MAIN_MENU && i.equals(InputManager.InputAction.MOUSECLICK)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			if(playRect != null && playRect.contains(mouseX,mouseY)){
				engine.loadOverworld();
			} else if (loadRect != null && loadRect.contains(mouseX,mouseY)){
				System.out.println("Someone implement load stuffs plsplspls");
			} else if (optionsRect != null && optionsRect.contains(mouseX,mouseY)){
				System.out.println("Someone implement the options plz");
			} else if (exitRect != null && exitRect.contains(mouseX,mouseY)){
				System.exit(1); //TODO not the right thing to do but will do for now
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

	public void unregister() {
		for (Entity e : buttons) {
			engine.removeEntity(e);
		}
	}
	
	private void resetButtonTextures() {
		playButton.getComponent(Sprite.class).setSpritesheet("play_button");
		loadButton.getComponent(Sprite.class).setSpritesheet("load_button");
		optionsButton.getComponent(Sprite.class).setSpritesheet("options_button");
		exitButton.getComponent(Sprite.class).setSpritesheet("exit_button");
	}

}
