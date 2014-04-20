package se.chalmers.roguelike;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;

import se.chalmers.plotgen.PlotEngine;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Systems.AISystem;
import se.chalmers.roguelike.Systems.CombatSystem;
import se.chalmers.roguelike.Systems.HighlightSystem;
import se.chalmers.roguelike.Systems.InteractionSystem;
import se.chalmers.roguelike.Systems.InventorySystem;
import se.chalmers.roguelike.Systems.ItemSystem;
import se.chalmers.roguelike.Systems.LevelingSystem;
import se.chalmers.roguelike.Systems.MenuSystem;
import se.chalmers.roguelike.Systems.MenuSystem.MenuState;
import se.chalmers.roguelike.Systems.MobSpriteSystem;
import se.chalmers.roguelike.Systems.MoveSystem;
import se.chalmers.roguelike.Systems.OverworldSystem;
import se.chalmers.roguelike.Systems.PlayerInputSystem;
import se.chalmers.roguelike.Systems.PlotSystem;
import se.chalmers.roguelike.Systems.RenderingSystem;
import se.chalmers.roguelike.Systems.StatusEffectSystem;
import se.chalmers.roguelike.Systems.TurnSystem;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.util.Camera;

public class Engine {

	// Global stuff
	public static int spriteSize = 16;
	public static int screenHeight = 768;
	public static int screenWidth = 1024;
	public static int hudWidth = 200;
	// Debug flag:
	public static boolean debug = true;

	// Constants: Components
	private static long componentID = 0;
	public static final long CompAttribute = 1 << componentID++;
	public static final long CompHealth = 1 << componentID++;
	public static final long CompInput = 1 << componentID++;
	public static final long CompPosition = 1 << componentID++;
	public static final long CompSprite = 1 << componentID++;
	public static final long CompTurnsLeft = 1 << componentID++;
	public static final long CompDirection = 1 << componentID++;
	public static final long CompAI = 1 << componentID++;
	public static final long CompHighlight = 1 << componentID++;
	public static final long CompPlayer = 1 << componentID++;
	public static final long CompWeapon = 1 << componentID++;
	public static final long CompFieldOfView = 1 << componentID++;
	public static final long CompSeed = 1 << componentID++;
	public static final long CompDungeon = 1 << componentID++;
	public static final long CompSelectedFlag = 1 << componentID++;
	public static final long CompGold = 1 << componentID++;
	public static final long CompBlocksWalking = 1 << componentID++;
	public static final long CompBlocksLineOfSight = 1 << componentID++;
	public static final long CompPlotAction = 1 << componentID++;
	public static final long CompMobType = 1 << componentID++;
	public static final long CompStair = 1 << componentID++;
	public static final long CompInventory = 1 << componentID++;
	public static final long CompPocketable = 1 << componentID++;
	public static final long CompDoubleName = 1 << componentID++;
	public static final long CompUsable = 1 << componentID++;
	public static final long CompText = 1 << componentID++;
	public static final long CompPlotLoot = 1 << componentID++;
	public static final long CompStatusEffect = 1 << componentID++;
	public static final long CompFirstStarFlag = 1 << componentID++;

	// Constants: System requirements:

	public static final long inventoryReq = CompInventory;
	public static final long inputSysReq = CompInput;
	public static final long renderingSysReq = CompSprite | CompPosition;
	public static final long moveSysReq = CompInput | CompPosition | CompDirection | CompTurnsLeft;
	public static final long mobSpriteSysReq = CompSprite | CompDirection;
	public static final long highlightSysReq = CompSprite | CompPosition;
	public static final long aiSysReq = CompFieldOfView | CompAI | CompInput;
	public static final long playerInputSysReq = CompPlayer;
	public static final long combatSystemReq = CompInput | CompHealth | CompPosition | CompTurnsLeft
			| CompMobType;
	public static final long levelingSystemReq = CompAttribute;
	public static final long dungeonReq = CompSprite | CompPosition;
	public static final long overworldReq = CompDungeon | CompSelectedFlag | CompPosition;
	public static final long statusEffectReq = CompStatusEffect;

	public EntityCreator entityCreator;
	private Dungeon dungeon;
	public static long seed;
	// Systems:
	private RenderingSystem renderingSys;
	private MoveSystem moveSys;
	private HighlightSystem highlightSys;
	private Entity player; // TODO: remove somehow?
	private TurnSystem turnSystem;
	private AISystem aiSystem;
	private InputManager inputManager; // not quite a system but close enough
	private PlayerInputSystem playerInputSys;
	private CombatSystem combatsystem;
	private LevelingSystem levelingSys;
	private OverworldSystem overworldSys;
	private MenuSystem menuSys;
	private InventorySystem inventorySys;
	private InteractionSystem interactionSys;
	private PlotSystem plotSys;
	private PlotEngine plotEngine;
	private MobSpriteSystem mobSpriteSys;
	private ItemSystem itemSys;
	private StatusEffectSystem statusEffectSys;

	public enum GameState {
		DUNGEON, MAIN_MENU, OVERWORLD, GAMEOVER
	}

	public static GameState gameState; // or private?

	/**
	 * Sets up the engine and it's variables.
	 */
	public Engine() {
		
		gameState = GameState.MAIN_MENU;
		seed = 1235L; // TODO: Switch to new Random().nextLong();
		
		// Init systems, etc:
		entityCreator = new EntityCreator(this);
		renderingSys = new RenderingSystem();
		menuSys = new MenuSystem(this);
		inputManager = new InputManager(this); // required to start the game
		inputManager.addObserver(menuSys);
	}

	/**
	 * Add entity to the engine and systems.
	 * 
	 * @param entity
	 *            entity to be added to the systems
	 */
	public void addEntity(Entity entity) {
		addOrRemoveEntity(entity, false);
	}

	/**
	 * Remove entities from the engine and the systems
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		addOrRemoveEntity(entity, true);
	}

	/**
	 * Handles adding and removal of entities from systems.
	 * 
	 * @param entity
	 *            entity that should be added or removed
	 * @param remove
	 *            if true the entity should be removed from systems, if false it will be added
	 */

	private void addOrRemoveEntity(Entity entity, boolean remove) {
		long compKey = entity.getComponentKey();

		if ((compKey & renderingSysReq) == renderingSysReq) {
			if (remove) {
				renderingSys.removeEntity(entity);
			} else {
				renderingSys.addEntity(entity);
			}
		}

		// This is a very special case and really bad code, the reason it is like this is due to the
		// other systems not having been initiated yet and will cause crashes

		if (Engine.gameState == GameState.MAIN_MENU) {
			return;
		}

		if ((compKey & moveSysReq) == moveSysReq) {
			if (remove) {
				moveSys.removeEntity(entity);
			} else {
				moveSys.addEntity(entity);
			}
		}
		if ((compKey & mobSpriteSysReq) == mobSpriteSysReq) {
			if (remove) {
				mobSpriteSys.removeEntity(entity);
			} else {
				mobSpriteSys.addEntity(entity);
			}
		}
		if ((compKey & highlightSysReq) == highlightSysReq) {
			if (remove) {
				highlightSys.removeEntity(entity);
			} else {
				highlightSys.addEntity(entity);
			}
		}
		if ((compKey & combatSystemReq) == combatSystemReq) {
			if (remove) {
				combatsystem.removeEntity(entity);
			} else {
				combatsystem.addEntity(entity);
			}
		}
		if ((compKey & CompPlayer) == CompPlayer) {
			// player = entity;
			playerInputSys.addEntity(player);
		}
		if ((compKey & CompTurnsLeft) == CompTurnsLeft) {
			if (remove) {
				turnSystem.removeEntity(entity);
			} else {
				turnSystem.addEntity(entity);
			}
		}
		if ((compKey & aiSysReq) == aiSysReq) {
			if (remove) {
				aiSystem.removeEntity(entity);
			} else {
				aiSystem.addEntity(entity);
			}
		}
		if ((compKey & levelingSystemReq) == levelingSystemReq) {
			if (remove) {
				levelingSys.removeEntity(entity);
			} else {
				levelingSys.addEntity(entity);
			}
		}
		if ((compKey & dungeonReq) == dungeonReq && (compKey & CompHighlight) != CompHighlight) {
			// Bit of a special case, since this requires coordinates:
			Position pos = entity.getComponent(Position.class);
			if (remove) {
				dungeon.removeEntity(pos.getX(), pos.getY(), entity);
			} else {
				dungeon.addEntity(pos.getX(), pos.getY(), entity);
			}
		}
		if ((compKey & overworldReq) == overworldReq) {
			if (remove) {
				overworldSys.removeEntity(entity);
			} else {
				overworldSys.addEntity(entity);
			}
		}
		if ((compKey & inventoryReq) == inventoryReq) {
			if (remove) {
				inventorySys.removeEntity(entity);
			} else {
				inventorySys.addEntity(entity);
			}
		}

		if (entity.containsComponent(CompPlayer)) {
			if (remove) {
				interactionSys.removeEntity(entity);
			} else {
				interactionSys.addEntity(entity);
			}
		}
		if ((compKey & statusEffectReq) == statusEffectReq) {
			if (remove) {
				statusEffectSys.removeEntity(entity);
			} else {
				statusEffectSys.addEntity(entity);
			}
		}

	}

	/**
	 * The game loop. Handles what should run for each state.
	 */
	public void run() {

		while (!Display.isCloseRequested()) {
			if (gameState == GameState.DUNGEON) {
				renderingSys.update();
				inputManager.update();
				combatsystem.update(dungeon);
				moveSys.update(dungeon);
				statusEffectSys.update();
				inventorySys.update(dungeon);
				interactionSys.update();
				mobSpriteSys.update();
				highlightSys.update(dungeon);
				levelingSys.update();
				turnSystem.update();

				if (player.getComponent(TurnsLeft.class).getTurnsLeft() <= 0) {
					aiSystem.update(dungeon, player);

					if (Engine.debug) {
						System.out.println("------------NEW TURN------------");
					}
				}

			} else if (gameState == GameState.OVERWORLD) {
				plotSys.update();
				renderingSys.update();
				inputManager.update();
				overworldSys.update();
			} else if (gameState == GameState.MAIN_MENU) {
				renderingSys.update();
				inputManager.update();
				menuSys.update();
			} else if (gameState == GameState.GAMEOVER) {
				// highlightSys.update(dungeon);
				renderingSys.update();
				inputManager.update();
				menuSys.update();
			}
		}
		Display.destroy();
	}

	/**
	 * Initializes the necessary systems.
	 */
	private void spawnSystems() {
		plotEngine = new PlotEngine(seed);
		dungeon = new Dungeon();
		moveSys = new MoveSystem();
		mobSpriteSys = new MobSpriteSystem();
		highlightSys = new HighlightSystem(entityCreator, this);
		turnSystem = new TurnSystem();
		aiSystem = new AISystem();
		playerInputSys = new PlayerInputSystem();
		combatsystem = new CombatSystem(this);
		levelingSys = new LevelingSystem();

		plotSys = new PlotSystem(this, plotEngine);
		overworldSys = new OverworldSystem(this, plotSys.getFirstPlotText());
		inventorySys = new InventorySystem(this);
		interactionSys = new InteractionSystem(this);
		itemSys = new ItemSystem();
		statusEffectSys = new StatusEffectSystem();
	}

	/**
	 * Sets up the camera
	 */
	private void setCamera() {
		Camera c = new Camera();
		highlightSys.setCamera(c);
		renderingSys.setCamera(c);
		playerInputSys.setCamera(c);
	}

	/**
	 * Adds the remaining observers that aren't registered from the start to the input manager
	 */
	private void handleObservers(boolean add) {
		if (add) {
			inputManager.addObserver(playerInputSys);
			inputManager.addObserver(highlightSys);
			inputManager.addObserver(overworldSys);
			inputManager.addObserver(interactionSys);
			inputManager.addObserver(inventorySys);
		} else {
			inputManager.removeObserver(playerInputSys);
			inputManager.removeObserver(highlightSys);
			inputManager.removeObserver(overworldSys);
			inputManager.removeObserver(interactionSys);
			inputManager.removeObserver(inventorySys);
		}
	}

	private void registerNewTurnSystems() {
		turnSystem.addObserver(statusEffectSys);
	}



	/**
	 * Loads a dungeon and changes the gamestate to DUNGEON
	 * 
	 * @param dungeon
	 *            the dungeon that should be loaded
	 * @param startX
	 *            the X-coord of the start position for the player
	 * @param startY
	 *            the Y-coord of the start position for the player
	 */
	public void loadDungeon(Dungeon dungeon, int startX, int startY) {
		if (gameState == GameState.OVERWORLD) {
			overworldSys.unregister();
		}
		if (this.dungeon != null) {
			this.dungeon.unregister(this);
		}
		this.dungeon = dungeon;
		this.dungeon.register(this);
		player.getComponent(Position.class).set(startX, startY);
		renderingSys.setDungeon(dungeon);
		interactionSys.setDungeon(dungeon);
		addEntity(player);
		gameState = GameState.DUNGEON;
	}

	/**
	 * Loads the overworld
	 */
	public void loadOverworld() {
		if (gameState == GameState.OVERWORLD) {
			return;
		}
		if (gameState == GameState.DUNGEON && dungeon != null) {
			dungeon.unregister(this);
			System.out.println("Unregister of dungeon done");
		} else if (gameState == GameState.MAIN_MENU) {
			menuSys.unregister();
			System.out.println("Unregister of mainmenu done");
		}
		if (overworldSys != null) {
			overworldSys.register();
		}
		gameState = GameState.OVERWORLD;
	}

	/**
	 * Loads the main menu
	 */
	public void loadMainMenu() {
		if (gameState == GameState.DUNGEON && dungeon != null) {
			dungeon.unregister(this);
			removeEntity(player); // TODO: Remove, this is due to some bug
		} else if (gameState == GameState.OVERWORLD) {
			overworldSys.unregister();
		}
		menuSys.register();
		gameState = GameState.MAIN_MENU;
	}

	/**
	 * Sets up a new game
	 */
	public void newGame() {	
		handleObservers(false); // removes any old systems still listening (does nothing at first run)
		spawnSystems();
		registerNewTurnSystems(); // Might be buggy with some changes were observers need to be changed
		handleObservers(true);
		setCamera();
		player = EntityCreator.createPlayer(SpaceClass.SPACE_WARRIOR, SpaceRace.SPACE_ALIEN);
		loadOverworld();
		overworldSys.addEntity(player);
		//addEntity(player);
	}

	public void gameOver() {
		if (gameState == GameState.DUNGEON) {
			dungeon.unregister(this);
		}
		highlightSys.unregister();
		gameState = GameState.GAMEOVER;
		menuSys.setState(MenuState.GAMEOVER);
	}
	
	/**
	 * @param args don't matter
	 */
	public static void main(String[] args) {
		new Engine().run();
	}
}
