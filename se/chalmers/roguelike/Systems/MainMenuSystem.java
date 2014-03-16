package se.chalmers.roguelike.Systems;

import java.awt.Rectangle;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.util.Observer;

public class MainMenuSystem implements ISystem, Observer {

	private Rectangle playRect, optionsRect, exitRect, loadRect;
	private Entity playButton, optionsButton, exitButton, loadButton;

	private Engine engine;

	public MainMenuSystem(Engine e) {
		this.engine = e;
		playButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 + 120,
				"play_button", 242, 62);
		playRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30, 242, 60);
		loadButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 + 60,
				"load_button", 242, 62);
		loadRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 + 60, 242, 60);
		optionsButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30,
				"options_button", 242, 62);
		optionsRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30, 242, 60);
		exitButton = engine.entityCreator.createButton(
				Engine.screenWidth / 2 - 141, Engine.screenHeight / 2 - 30 - 60,
				"exit_button", 242, 62);
		exitRect = new Rectangle(Engine.screenWidth / 2 - 141,
				Engine.screenHeight / 2 - 30 - 60, 242, 60);


	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	/**
	 * This method will be called when switching to the MainMenu gamestate and
	 * will register all the entities with engine.
	 */
	public void register() {
		if (playButton != null) {
			engine.addEntity(playButton);
		} else if (loadButton != null) {
			engine.addEntity(loadButton);
		} else if (optionsButton != null) {
			engine.addEntity(optionsButton);
		} else if (exitButton != null) {
			engine.addEntity(exitButton);
		}
	}

	public void unregister() {
		engine.removeEntity(playButton);
		engine.removeEntity(loadButton);
		engine.removeEntity(optionsButton);
		engine.removeEntity(exitButton);
	}

}
