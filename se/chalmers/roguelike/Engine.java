package se.chalmers.roguelike;

import se.chalmers.roguelike.Systems.*;

import java.util.ArrayList;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Entities.Entity;

public class Engine {

	private long lastUpdate;
	/// private int fps; // updates per second, not necessarly fps
	// private ArrayList<ISystem> systems; // Depreached, re-add later?
	private ArrayList<Entity> entities; // useless?
	private EntityCreator entityCreator;
	
	// Systems:
	private InputSystem inputSys; // todo: Don't have it public
	private RenderingSystem renderingSys;
	
	public Engine() {
		System.out.println("Starting new engine.");
		entities = new ArrayList<Entity>();
		entityCreator = new EntityCreator(this);
		spawnSystems();
	}
	
	public void addEntity(Entity entity){
		entities.add(entity);
	}
	
	public void addToInputSys(Entity entity){
		inputSys.addEntity(entity);
	}
	
	/**
	 * Worlds worst game loop.
	 */
	public void run(){
		entityCreator.createPlayer(); 	// Debug, testing EC
		for(int i=0;i<100;i++){
			//renderingSys.update();
			inputSys.update();
			
		}
		
		System.out.println("HP: "+entities.get(0).getComponent(Health.class).getHealth());
		System.out.println("Next key: "+entities.get(0).getComponent(Input.class).getNextKey());
		renderingSys.exit();
	}
	
	/**
	 * Returns the current system time.
	 * @return current system time
	 */
	private long getTime(){
		// better solution with lwjgl?
		return System.nanoTime()/1000000;
	}
	
	private int getDelta() {
		long time = getTime();
		int delta = (int)(time-lastUpdate);
		lastUpdate = time;
		return delta;
	}
	
	
	private void spawnSystems(){
		renderingSys = new RenderingSystem();
		inputSys = new InputSystem();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(new File("./resources/" + "guy" + ".png").getAbsolutePath());

		new Engine().run();

	}
}
