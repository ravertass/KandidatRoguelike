package se.chalmers.roguelike;

import java.util.ArrayList;

import se.chalmers.roguelike.Systems.*;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Generator;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.Components.TurnsLeft;

public class Engine {
	
	// Constants: Components
	public static final int CompCharacter = 1 << 0;
	public static final int CompHealth = 1 << 1;
	public static final int CompInput = 1 << 2;
	public static final int CompPosition = 1 << 3;
	public static final int CompSprite = 1 << 4;
	public static final int CompTurnsLeft = 1 << 5;
	public static final int CompDirection = 1 << 6;
	public static final int CompAI = 1 << 7;
	public static final int CompHighlight = 1 << 8;
	public static final int CompPlayer = 1 << 9;
	public static final int CompWeapon = 1 << 10;
	
	
	// Constants: System requirements:
	public static final int inputSysReq = CompInput;
	public static final int renderingSysReq = CompSprite | CompPosition;
	public static final int moveSysReq = CompInput | CompPosition | CompDirection | CompTurnsLeft;
	public static final int mobSpriteSysReq = CompSprite | CompDirection;
	public static final int highlightSysReq = CompSprite | CompPosition | CompInput | CompHighlight;
	public static final int aiSysReq = CompAI | CompInput;
	public static final int playerInputSysReq = CompPlayer;
	public static final int combatSystemReq = CompInput | CompHealth | CompPosition | CompTurnsLeft;
	
	/// private int fps; // updates per second, not necessarly fps
	// private ArrayList<ISystem> systems; // Depreached, re-add later?
	private ArrayList<Entity> entities; // useless?
	private EntityCreator entityCreator;
	
	private Dungeon dungeon;
	
	// Systems:
	private RenderingSystem renderingSys;
	private MoveSystem moveSys;
	private MobSpriteSystem mobSpriteSys;
	private HighlightSystem highlightSys;
	private Entity player; // TODO: remove somehow?
	private TurnSystem turnSystem;
	private AISystem aiSystem;
	private InputManager inputManager; //not quite a system but close enough
	private PlayerInputSystem playerInputSys;
	private CombatSystem combatsystem;
	
	private enum GameState {
		DUNGEON, MAIN_MENU, OVERWORLD
	}
	private GameState gameState;
	
	/**
	 * Sets up the engine and it's variables.
	 */
	public Engine() {
		System.out.println("Starting new engine.");
		entities = new ArrayList<Entity>();
		entityCreator = new EntityCreator(this);
		gameState = GameState.DUNGEON;
		spawnSystems();
		registerInputSystems();
		setCamera();
	}
	
	/**
	 * Add entity to the engine and systems.
	 * @param entity entity to be added to the systems
	 */
	public void addEntity(Entity entity){
		entities.add(entity);
		addOrRemoveEntity(entity, false);
	}
	
	/**
	 * Remove entities from the engine and the systems
	 * @param entity
	 */
	public void removeEntity(Entity entity){
		// maybe return a bool if it could remove it?�
		// Check if removals really work properly or if we need to write some equals function 
		entities.remove(entity);
		addOrRemoveEntity(entity, true);
	}
	
	/**
	 * Handles adding and removal of entities from systems.
	 * 
	 * @param entity entity that should be added or removed
	 * @param remove if true the entity should be removed from systems, if false it will be added
	 */
	private void addOrRemoveEntity(Entity entity, boolean remove){		
		int compKey = entity.getComponentKey(); 
//		if((compKey & inputSysReq) == inputSysReq) {
//			if(remove){
//				inputSys.removeEntity(entity);
//			} else {
//				inputSys.addEntity(entity);
//			}
//		}
		if((compKey & renderingSysReq) == renderingSysReq) {
			if(remove){
				renderingSys.removeEntity(entity);
			} else {
				renderingSys.addEntity(entity);
			}
		}
		if((compKey & moveSysReq) == moveSysReq) {
			if(remove){
				moveSys.removeEntity(entity);
			} else {
				moveSys.addEntity(entity);
			}
		}
		if((compKey & mobSpriteSysReq) == mobSpriteSysReq) {
			if(remove){
				mobSpriteSys.removeEntity(entity);
			} else {
				mobSpriteSys.addEntity(entity);
			}
		}
		if((compKey & highlightSysReq) == highlightSysReq) {
			if(remove) {
				highlightSys.removeEntity(entity);
			} else {
				highlightSys.addEntity(entity);
			}
		}
		if ((compKey & combatSystemReq) == combatSystemReq) {
			if(remove) {
				combatsystem.removeEntity(entity);
			} else {
				combatsystem.addEntity(entity);
			}
		}
		if((compKey & CompPlayer) == CompPlayer) {
			player = entity;
			playerInputSys.addEntity(player);
		}
		if((compKey & CompTurnsLeft) == CompTurnsLeft){
			turnSystem.addEntity(entity);
		}
		if((compKey & aiSysReq) == aiSysReq){
			aiSystem.addEntity(entity);
		}
	}
	
	/**
	 * Worlds worst game loop.
	 */
	public void run(){
		entityCreator.createPlayer();
		entityCreator.createEnemy();
		entityCreator.createHighlight();
		
		while(true){
			if(gameState == GameState.DUNGEON) {
				renderingSys.update(dungeon);
				renderingSys.update();
//				inputSys.update();
				inputManager.update();
				combatsystem.update();
				moveSys.update();
				mobSpriteSys.update();
				highlightSys.update();
				turnSystem.update();
				if(player.getComponent(TurnsLeft.class).getTurnsLeft() == 0){
					aiSystem.update();
				}
				
			//} else if(gameState == GameState.MENU) {

			} else if(gameState == GameState.OVERWORLD) {
				//TODO
				//add system  that is used in the overworld
			} else if(gameState == GameState.MAIN_MENU) {
				//TODO
				MainMenu.getInstance().show(renderingSys);
			}
		}
	}
	
	/**
	 * Initiazes the necessary systems.
	 */
	private void spawnSystems(){
		renderingSys = new RenderingSystem();
		dungeon = new Dungeon();
		dungeon.setWorld(50,50,new Generator().toTiles());
		inputManager = new InputManager();
		//inputSys = new InputSystem();
		moveSys = new MoveSystem(dungeon); // remember to update pointer for new worlds
		mobSpriteSys = new MobSpriteSystem();
		highlightSys = new HighlightSystem();
		turnSystem = new TurnSystem();
		aiSystem = new AISystem(dungeon);
		playerInputSys = new PlayerInputSystem();
		combatsystem = new CombatSystem(dungeon);
		
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
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Engine().run();
	}
}
