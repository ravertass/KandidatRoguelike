package se.chalmers.roguelike.util;

public class Clock {

	private long lastUpdate;
	
	public Clock() {
		getDelta();
	}
	
	/**
	 * Returns the current system time.
	 * @return current system time
	 */
	private long getTime() {
		// better solution with lwjgl?
		return System.nanoTime()/1000000;
	}
	
	/**
	 * Gives a delta time since last time it was run.
	 * @return the since last time getDelta was run
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int)(time-lastUpdate);
		lastUpdate = time;
		return delta;
	}
}
