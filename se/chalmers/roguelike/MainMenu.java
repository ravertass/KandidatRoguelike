package se.chalmers.roguelike;

import se.chalmers.roguelike.Systems.RenderingSystem;

public class MainMenu {
	
	
	private static MainMenu instance = null;
	private String[] menuItems;

	protected MainMenu() {
		menuItems = new String[] {"CRIMSON POODLE", "Play", "Load", "Quit"};
		}

	public static MainMenu getInstance() {
		if(instance == null) {
			instance = new MainMenu();
		}
	    return instance;
	}

	public void show(RenderingSystem renderSys) {
		
		renderSys.drawMenu(menuItems);
	}
	
	
}