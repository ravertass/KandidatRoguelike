package se.chalmers.roguelike;

import se.chalmers.roguelike.Systems.*;

import java.io.File;
import java.util.ArrayList;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Entities.Entity;
import se.chalmers.roguelike.Systems.ISystem;

public class Engine {

	private long lastUpdate;
	private int fps; // updates per second, not necessarly fps
	private ArrayList<ISystem> systems;
	private ArrayList<Entity> entities;
	private EntityCreator entityCreator;
	
	public Engine() {
		System.out.println("Starting new engine.");
		systems = new ArrayList<ISystem>();
		entities = new ArrayList<Entity>();
		entityCreator = new EntityCreator(this);
	}
	
	public void addEntity(Entity entity){
		entities.add(entity);
	}
	
	/**
	 * Worlds worst game loop.
	 */
	public void run(){
		/*while(true){ // possible switch to a game status boolean later
			for(ISystem sys : systems){
				sys.update();
			}
		}*/
		
		// Debug, testing EC 
		entityCreator.createPlayer();
		System.out.println("HP: "+entities.get(0).getComponent(Health.class).getHealth());
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(new File("./resources/" + "guy" + ".png").getAbsolutePath());
		RenderingSystem renderingSystem = new RenderingSystem();
		for (int i = 0; i < 100; i++) {
			renderingSystem.update();
		}
		renderingSystem.exit();
	}

}
