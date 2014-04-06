package se.chalmers.roguelike;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;

import se.chalmers.plotgen.PlotEngine;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Systems.AISystem;
import se.chalmers.roguelike.Systems.CombatSystem;
import se.chalmers.roguelike.Systems.HighlightSystem;
import se.chalmers.roguelike.Systems.InventorySystem;
import se.chalmers.roguelike.Systems.InteractionSystem;
import se.chalmers.roguelike.Systems.LevelingSystem;
import se.chalmers.roguelike.Systems.MainMenuSystem;
import se.chalmers.roguelike.Systems.MobSpriteSystem;
import se.chalmers.roguelike.Systems.MoveSystem;
import se.chalmers.roguelike.Systems.OverworldSystem;
import se.chalmers.roguelike.Systems.PlayerInputSystem;
import se.chalmers.roguelike.Systems.PlotSystem;
import se.chalmers.roguelike.Systems.RenderingSystem;
import se.chalmers.roguelike.Systems.TurnSystem;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.util.Camera;

public class Engine {

	// Global stuff
	public static int spriteSize = 16;
	public static int screenHeight = 768;
	public static int screenWidth = 1024;
	// Debug flag:
	public static boolean debug = true;
	// Constants: Components

	// / private int fps; // updates per second, not necessarly fps
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
	public static final long CompInventory = 1 << componentID++;
	public static final long CompPocketable = 1 << componentID++;
	public static final long CompPlotAction = 1 << componentID++;
	public static final long CompEnemyType = 1 << componentID++;
	public static final long CompStair = 1 << componentID++;
	
	// Constants: System requirements:

	public static final long inventoryReq = CompInventory;
	public static final long inputSysReq = CompInput;
	public static final long renderingSysReq = CompSprite | CompPosition;
	public static final long moveSysReq = CompInput | CompPosition
			| CompDirection | CompTurnsLeft;
	public static final long mobSpriteSysReq = CompSprite | CompDirection;
	public static final long highlightSysReq = CompSprite | CompPosition;
	public static final long aiSysReq = CompAI | CompInput; // add CompFieldOfView?
	public static final long playerInputSysReq = CompPlayer;
	public static final long combatSystemReq = CompInput | CompHealth
			| CompPosition | CompTurnsLeft;
	public static final long levelingSystemReq = CompAttribute;
	public static final long dungeonReq = CompSprite | CompPosition;
	public static final long overworldReq = CompDungeon | CompSelectedFlag
			| CompPosition;

	// private ArrayList<ISystem> systems; // Depreached, re-add later?
	private ArrayList<Entity> entities; // useless?
	public EntityCreator entityCreator;

	private Dungeon dungeon; // remove static, just for testing unloading atm

	// Systems:
	private RenderingSystem renderingSys;
	private MoveSystem moveSys;
	private MobSpriteSystem mobSpriteSys;
	private HighlightSystem highlightSys;
	private Entity player; // TODO: remove somehow?
	private TurnSystem turnSystem;
	private AISystem aiSystem;
	private InputManager inputManager; // not quite a system but close enough
	private PlayerInputSystem playerInputSys;
	private CombatSystem combatsystem;
	private LevelingSystem levelingSys;
	private OverworldSystem overworldSys;
	private MainMenuSystem mainmenuSys;
	private InventorySystem inventorySys;
	private InteractionSystem interactionSys;
	private PlotSystem plotSys;
	private PlotEngine plotEngine;

	public enum GameState {
		DUNGEON, MAIN_MENU, OVERWORLD
	}

	public static GameState gameState; // or private?

	/**
	 * Sets up the engine and it's variables.
	 */
	public Engine() {
		System.out.println("Starting new engine.");
		entities = new ArrayList<Entity>();
		entityCreator = new EntityCreator(this);
		// gameState = GameState.DUNGEON;
		gameState = GameState.MAIN_MENU;
		plotEngine = new PlotEngine(new Random().nextLong()); // make dependent
																// on seed
		spawnSystems();
		registerInputSystems();
		setCamera();
	}

	/**
	 * Add entity to the engine and systems.
	 * 
	 * @param entity
	 *            entity to be added to the systems
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
		addOrRemoveEntity(entity, false);
	}

	/**
	 * Remove entities from the engine and the systems
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		// maybe return a bool if it could remove it?�
		// Check if removals really work properly or if we need to write some
		// equals function
		entities.remove(entity);
		addOrRemoveEntity(entity, true);
	}

	/**
	 * Handles adding and removal of entities from systems.
	 * 
	 * @param entity
	 *            entity that should be added or removed
	 * @param remove
	 *            if true the entity should be removed from systems, if false it
	 *            will be added
	 */

	private void addOrRemoveEntity(Entity entity, boolean remove) {
		long compKey = entity.getComponentKey();
		// if((compKey & inputSysReq) == inputSysReq) {
		// if(remove){
		// inputSys.removeEntity(entity);
		// } else {
		// inputSys.addEntity(entity);
		// }
		// }
		if ((compKey & renderingSysReq) == renderingSysReq) {
			if (remove) {
				renderingSys.removeEntity(entity);
			} else {
				renderingSys.addEntity(entity);
			}
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
		if ((compKey & dungeonReq) == dungeonReq
				&& (compKey & CompHighlight) != CompHighlight) {
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
	}

	/**
	 * World's worst game loop.
	 */
	public void run() {
		player = entityCreator.createPlayer(SpaceClass.SPACE_WARRIOR,
				SpaceRace.SPACE_ALIEN);
		// TODO använd en till spelet given seed
		// NameGenerator ng = new NameGenerator(2, new Random().nextLong());
		// for (int i = 0; i <4; i++)
		// entityCreator.createEnemy(ng.generateName());
		// entityCreator.createHighlight();

		while (!Display.isCloseRequested()) {
			if (gameState == GameState.DUNGEON) {
				renderingSys.update();
				inputManager.update();
				combatsystem.update(dungeon);
				moveSys.update(dungeon);
				inventorySys.update(dungeon);
				interactionSys.update();
				mobSpriteSys.update();
				highlightSys.update(dungeon);
				levelingSys.update();
				turnSystem.update();

				if (player.getComponent(TurnsLeft.class).getTurnsLeft() <= 0) {
					aiSystem.update(dungeon, player);

					System.out.println("------------NEW TURN------------");
				}

				// } else if(gameState == GameState.MENU) {

			} else if (gameState == GameState.OVERWORLD) {
				// TODO
				// add system that is used in the overworld
				// renderingSys.drawOWbackground();
				plotSys.update();
				renderingSys.update();
				inputManager.update();
				overworldSys.update();
			} else if (gameState == GameState.MAIN_MENU) {
				renderingSys.update();
				inputManager.update();
				mainmenuSys.update();

			}
		}
		Display.destroy();
	}

	/**
	 * Initializes the necessary systems.
	 */
	private void spawnSystems() {
		renderingSys = new RenderingSystem();
		dungeon = new Dungeon(); // remove engine?
		// dungeon.setWorld(50,50,new Generator().toTiles());
		inputManager = new InputManager(this); // This feels stupid that it
												// should have engine component,
												// maybe change once debug stuff
												// is over for the load manager
		// inputSys = new InputSystem();
		moveSys = new MoveSystem(); // remember to update pointer for new worlds
		mobSpriteSys = new MobSpriteSystem();
		highlightSys = new HighlightSystem(entityCreator, this);
		turnSystem = new TurnSystem();
		aiSystem = new AISystem();
		playerInputSys = new PlayerInputSystem();
		combatsystem = new CombatSystem(this);
		levelingSys = new LevelingSystem();

		plotSys = new PlotSystem(this, plotEngine);
		overworldSys = new OverworldSystem(this);
		mainmenuSys = new MainMenuSystem(this);
		inventorySys = new InventorySystem();
		interactionSys = new InteractionSystem(this);
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

	private void registerInputSystems() {
		inputManager.addObserver(playerInputSys);
		inputManager.addObserver(highlightSys);
		inputManager.addObserver(overworldSys);
		inputManager.addObserver(mainmenuSys);
		inputManager.addObserver(interactionSys);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Engine().run();
	}

	public void loadDungeon(Dungeon dungeon, int startX, int startY){
		// TODO: Loading screen stuff
		if(gameState == GameState.OVERWORLD){
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

	public void loadOverworld() {
		if (gameState == GameState.OVERWORLD) {
			return;
		}
		if (gameState == GameState.DUNGEON && dungeon != null) {
			dungeon.unregister(this);
			System.out.println("Unregister of dungeon done");
		} else if (gameState == GameState.MAIN_MENU) {
			mainmenuSys.unregister();
			System.out.println("Unregister of mainmenu done");
		}
		overworldSys.register();
		gameState = GameState.OVERWORLD;
	}

	public void loadMainMenu() {
		if (gameState == GameState.DUNGEON && dungeon != null) {
			dungeon.unregister(this);
			removeEntity(player); // TODO: Remove, this is due to some bug
			System.out.println("Unregister of dungeon done");
		} else if (gameState == GameState.OVERWORLD) {
			overworldSys.unregister();
			System.out.println("Unregister of overworld done");
		}
		mainmenuSys.register();
		gameState = GameState.MAIN_MENU;
	}
}
