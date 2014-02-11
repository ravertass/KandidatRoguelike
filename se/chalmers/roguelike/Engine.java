package se.chalmers.roguelike;

import se.chalmers.roguelike.Systems.*;

public class Engine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello world");

		Rendering renderingSystem = new Rendering();
		for (int i = 0; i < 10000; i++) {
			renderingSystem.update();
		}
		renderingSystem.exit();
	}

}
