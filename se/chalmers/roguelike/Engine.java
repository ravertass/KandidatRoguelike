package se.chalmers.roguelike;

import se.chalmers.roguelike.Systems.*;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Entities.Entity;
import se.chalmers.roguelike.Systems.ISystem;

public class Engine {

	private long lastUpdate;
	private int fps; // updates per second, not necessarly fps
	private ArrayList<ISystem> systems;
	private ArrayList<Entity> entities;
	private EntityCreator entityCreator;
	public InputSystem inputSys; // todo: Don't have it public
	
	public Engine() {
		System.out.println("Starting new engine.");
		systems = new ArrayList<ISystem>();
		entities = new ArrayList<Entity>();
		entityCreator = new EntityCreator(this);
		inputSys = new InputSystem(); //
		systems.add(inputSys);
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
		// Debug, testing EC 
		entityCreator.createPlayer();
		RenderingSystem renderingSystem = new RenderingSystem();
		systems.add(renderingSystem);
		for(int i=0;i<100;i++){
		// while(true){ // possible switch to a game status boolean later
			for(ISystem sys : systems){
				//System.out.println(Keyboard.getEventKey());
				sys.update();
			}
			
			
		}
		
		System.out.println("HP: "+entities.get(0).getComponent(Health.class).getHealth());
		System.out.println("Next key: "+entities.get(0).getComponent(Input.class).getNextKey());
		renderingSystem.exit();
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
	//	systems.add(new InputSystem());
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

/*
		System.out.println(new File("./resources/" + "guy" + ".png").getAbsolutePath());
		RenderingSystem renderingSystem = new RenderingSystem();
		for (int i = 0; i < 100; i++) {
			renderingSystem.update();
		}
		renderingSystem.exit();
*/
		new Engine().run();
/*
		Rendering renderingSystem = new Rendering();
		for (int i = 0; i < 10000; i++) {
			renderingSystem.update();
		}
		renderingSystem.exit();*/
	}

}
