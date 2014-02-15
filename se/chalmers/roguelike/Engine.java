package se.chalmers.roguelike;

import se.chalmers.roguelike.Systems.*;
import se.chalmers.roguelike.World.World;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Entities.Entity;

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
	public static final int CompCamera = 1 << 10;
	
	// Constants: System requirements:
	public static final int inputSysReq = CompInput;
	public static final int renderingSysReq = CompSprite | CompPosition;
	public static final int moveSysReq = CompInput | CompPosition | CompDirection | CompTurnsLeft;
	public static final int mobSpriteSysReq = CompSprite | CompDirection;
	public static final int highlightSysReq = CompSprite | CompPosition | CompInput | CompHighlight;
	
	private long lastUpdate;
	/// private int fps; // updates per second, not necessarly fps
	// private ArrayList<ISystem> systems; // Depreached, re-add later?
	private ArrayList<Entity> entities; // useless?
	private EntityCreator entityCreator;
	
	// Systems:
	private World world;
	private InputSystem inputSys; // todo: Don't have it public
	private RenderingSystem renderingSys;
	private MoveSystem moveSys;
	private MobSpriteSystem mobSpriteSys;
	private HighlightSystem highlightSys;
	
	private enum GameState {
		DUNGEON, MAIN_MENU, OVERWORLD
	}
	private GameState gameState;
	
	public Engine() {
		System.out.println("Starting new engine.");
		entities = new ArrayList<Entity>();
		entityCreator = new EntityCreator(this);
		gameState = GameState.DUNGEON;
		spawnSystems();
	}
	
	public void addEntity(Entity entity){
		entities.add(entity);
		addOrRemoveEntity(entity, false);
	}
	
	public void removeEntity(Entity entity){
		// maybe return a bool if it could remove it?�
		// Check if removals really work properly or if we need to write some equals function 
		entities.remove(entity);
		addOrRemoveEntity(entity, true);
	}
	
	private void addOrRemoveEntity(Entity entity, boolean remove){		
		int compKey = entity.getComponentKey(); 
		if((compKey & inputSysReq) == inputSysReq) {
			if(remove){
				inputSys.removeEntity(entity);
			} else {
				inputSys.addEntity(entity);
			}
		}
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
			
	}
	
	/**
	 * Worlds worst game loop.
	 */
	public void run(){
//		entityCreator.createPlayer(); 	// Debug, testing EC
		entityCreator.createPlayer();
		entityCreator.createHighlight();
//		System.out.println("FOOO: "+entities.get(0).equals(entities.get(1)));
		//for(int i=0;i<100;i++){
		
		
		while(true){
			if(gameState == GameState.DUNGEON) {
				renderingSys.update(world);
				renderingSys.update();
				inputSys.update();
				moveSys.update();
				mobSpriteSys.update();
				highlightSys.update();
			} else if(gameState == GameState.OVERWORLD) {
				//TODO
				//add system  that is used in the overworld
			} else if(gameState == GameState.MAIN_MENU) {
				//TODO
				renderingSys.drawMenu();
			}
		}
		
		//System.out.println("HP: "+entities.get(0).getComponent(Health.class).getHealth());
		//System.out.println("Next key: "+entities.get(0).getComponent(Input.class).getNextKey());
		//renderingSys.exit();
	}
	
	/**
	 * Returns the current system time.
	 * @return current system time
	 */
	private long getTime(){
		// better solution with lwjgl?
		return System.nanoTime()/1000000;
	}
	
	/**
	 * Gives a delta time since last time it was run.
	 * @return the since last time getDelta was run
	 */
	private int getDelta() {
		long time = getTime();
		int delta = (int)(time-lastUpdate);
		lastUpdate = time;
		return delta;
	}
	
	/**
	 * Initiazes the necessary systems.
	 */
	private void spawnSystems(){
		renderingSys = new RenderingSystem();
		world = new World();
		inputSys = new InputSystem();
		moveSys = new MoveSystem(world); // remember to update pointer for new worlds
		mobSpriteSys = new MobSpriteSystem();
		highlightSys = new HighlightSystem();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(new File("./resources/" + "guy" + ".png").getAbsolutePath());

		new Engine().run();

	}
}
