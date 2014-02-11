package se.chalmers.roguelike;

import java.util.ArrayList;

import se.chalmers.roguelike.Systems.ISystem;

public class Engine {

	private long lastUpdate;
	private int fps; // updates per second, not necessarly fps
	private ArrayList<ISystem> systems
	;
	public Engine(){
		System.out.println("Starting new engine.");
		systems = new ArrayList<ISystem>();
	}
	/**
	 * Worlds worst game loop.
	 */
	public void run(){
		while(true){ // possible switch to a game status boolean later
			for(ISystem sys : systems){
				sys.update();
			}
		}
	}
	
	/**
	 * Returns the current system time.
	 * @return current system time
	 */
	private long getTime(){
		// better solution with lwjgl?
		return System.nanoTime()/1000000;
	}
	
	private int getDelta(){
		long time = getTime();
		int delta = (int)(time-lastUpdate);
		lastUpdate = time;
		return delta;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Engine().run();

	}

}
