package se.chalmers.roguelike;

import java.util.ArrayList;

import se.chalmers.plotgen.NameGen.NameGenerator;
import se.chalmers.roguelike.Systems.*;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.util.Camera;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.TurnsLeft;

public class Engine {
	
	//Global stuff
	public static int spriteSize = 16;
	public static int screenHeight = 768;
	public static int screenWidth = 1024;
	// Debug flag:
	public static boolean debug = true; 
	// Constants: Components
	public static final int CompAttribute = 1 << 0;
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
	public static final int CompFieldOfView = 1 << 11;
	public static final int CompSeed = 1 << 12;
	public static final int CompDungeon= 1 << 13;
	public static final int CompSelectedFlag = 1 << 14;
	
	
	// Constants: System requirements:
	public static final int inputSysReq = CompInput;
	public static final int renderingSysReq = CompSprite | CompPosition;
	public static final int moveSysReq = CompInput | CompPosition | CompDirection | CompTurnsLeft;
	public static final int mobSpriteSysReq = CompSprite | CompDirection;
	public static final int highlightSysReq = CompSprite | CompPosition;
	public static final int aiSysReq = CompAI | CompInput;
	public static final int playerInputSysReq = CompPlayer;
	public static final int combatSystemReq = CompInput | CompHealth | CompPosition | CompTurnsLeft;
	public static final int levelingSystemReq = CompAttribute;
	public static final int dungeonReq = CompSprite | CompPosition;
	
	/// private int fps; // updates per second, not necessarly fps
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
	private InputManager inputManager; //not quite a system but close enough
	private PlayerInputSystem playerInputSys;
	private CombatSystem combatsystem;
	private LevelingSystem levelingSys;
	private OverworldSystem overworldSys;
	
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
		//gameState = GameState.DUNGEON;
		gameState = GameState.OVERWORLD;
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
			if(remove) {
				turnSystem.removeEntity(entity);
			} else {
				turnSystem.addEntity(entity);
			}
		}
		if((compKey & aiSysReq) == aiSysReq){
			if(remove) {
				aiSystem.removeEntity(entity);
			} else {
				aiSystem.addEntity(entity);
			}
		}
		if((compKey & levelingSystemReq) == levelingSystemReq) {
			if(remove) {
				levelingSys.removeEntity(entity);
			} else {
				levelingSys.addEntity(entity);
			}
		}
		if((compKey & dungeonReq) == dungeonReq) {
			// Bit of a special case, since this requires coordinates:
			Position pos = entity.getComponent(Position.class);
			if(remove) {
				dungeon.removeEntity(pos.getX(), pos.getY(), entity);
			} else {
				dungeon.addEntity(pos.getX(), pos.getY(), entity);
			}
		}
		
		
	}
	
	/**
	 * Worlds worst game loop.
	 */
	public void run(){
		entityCreator.createPlayer(SpaceClass.SPACE_WARRIOR, SpaceRace.SPACE_HUMAN);
		NameGenerator ng = new NameGenerator(2);
		for (int i = 0; i <4; i++)
			entityCreator.createEnemy(ng.generateName());
		entityCreator.createHighlight();
		
		while(true){
			if(gameState == GameState.DUNGEON) {
				renderingSys.update(dungeon);
				renderingSys.update();
				inputManager.update();
				combatsystem.update(dungeon);
				moveSys.update(dungeon);
				mobSpriteSys.update();
				highlightSys.update(dungeon);
				levelingSys.update();
				turnSystem.update();
				if(player.getComponent(TurnsLeft.class).getTurnsLeft() == 0){
					aiSystem.update(dungeon);
				}
				
			//} else if(gameState == GameState.MENU) {

			} else if(gameState == GameState.OVERWORLD) {
				//TODO
				//add system  that is used in the overworld
				renderingSys.update();
				inputManager.update();
				overworldSys.update();
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
		dungeon = new Dungeon(this); // remove engine?
		// dungeon.setWorld(50,50,new Generator().toTiles());
		inputManager = new InputManager(this); // This feels stupid that it should have engine component, maybe change once debug stuff is over for the load manager
		//inputSys = new InputSystem();
		moveSys = new MoveSystem(); // remember to update pointer for new worlds
		mobSpriteSys = new MobSpriteSystem();
		highlightSys = new HighlightSystem(entityCreator, this);
		turnSystem = new TurnSystem();
		aiSystem = new AISystem();
		playerInputSys = new PlayerInputSystem();
		combatsystem = new CombatSystem(this);
		levelingSys = new LevelingSystem();
		
		overworldSys = new OverworldSystem(this);
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
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Engine().run();
	}
	
	public void loadDungeon(Dungeon dungeon, GameState newState){
		// TODO: Loading screen stuff
		if(gameState == GameState.OVERWORLD && newState == GameState.DUNGEON){
			this.dungeon = dungeon;

			player.getComponent(Position.class).set(1, 1); // This respawns the player 1,1 of each map

			addEntity(player);
			this.dungeon.register();
			gameState = newState;
		}
	}
	public void loadOverworld(){

		if(gameState == GameState.DUNGEON && dungeon != null){
			dungeon.unregister();
			removeEntity(player); // TODO: Remove, this is due to some bug
			System.out.println("Unregister done");
		}
		overworldSys.register();
		gameState = GameState.OVERWORLD;
	}
}
