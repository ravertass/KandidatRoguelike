package se.chalmers.roguelike.Systems;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Seed;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Generator;
import se.chalmers.roguelike.util.Observer;

/**
 * A system for the overworld. Will handle the logic and control of it.
 */
public class OverworldSystem implements ISystem, Observer{
	private Engine engine;
	private ArrayList<Rectangle> starRectangles;
	
	private Entity activeStar;
	private HashMap<String,Entity> stars;
	final Position noClick = new Position(-1, -1);
	private Rectangle playRect, menuRect;
	private Entity playButton, menuButton;
	public OverworldSystem(Engine engine) {
		this.engine = engine;
		activeStar = null;
		stars = new HashMap<String,Entity>();
		starRectangles = new ArrayList<Rectangle>();
		playButton = engine.entityCreator.createButton(Engine.screenWidth-80, 200, "button_play_gray"); 
		menuButton = engine.entityCreator.createButton(Engine.screenWidth-80, 200-32, "button_menu"); 
		menuRect = new Rectangle(Engine.screenWidth-80,200-32,80,32);
		setupStars();
	}
	@Override
	public void update() {
		
		
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO not necessary?
		
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO not necessary?
		
	}
	/**
	 * Handles the input and determines if anything needs to run.
	 */
	public void notify(Enum<?> i) {
		if (Engine.gameState == Engine.GameState.OVERWORLD && i.equals(InputManager.InputAction.MOUSECLICK)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			for(Rectangle star : starRectangles){
				// if(star.contains(mouseX,mouseY)){
				if(!star.contains(mouseX,mouseY)) continue;
				starClicked(star);
			}
			if(playRect != null && playRect.contains(mouseX,mouseY)){
				System.out.println("PLAY PRESSED!");
				loadDungeon();
			} else if (menuRect.contains(mouseX,mouseY)){
				System.out.println("Someone stole the flippin menu");
			}
		}
	}
	/**
	 * Triggers the loading of a dungeon.
	 */
	private void loadDungeon(){
		Dungeon starDungeon = activeStar.getComponent(DungeonComponent.class).getDungeon();
		if(starDungeon == null){
			System.out.println("No dungeon found! Generating one.");
			long seed=activeStar.getComponent(Seed.class).getSeed();
			starDungeon = new Dungeon(engine);
			starDungeon.setWorld(50,50,new Generator(seed).toTiles());
			activeStar.getComponent(DungeonComponent.class).setDungeon(starDungeon);
		} 
		
		unregister();
		engine.loadDungeon(starDungeon,Engine.GameState.DUNGEON);
	}
	/**
	 * Makes a star active if it was clicked and adds a play-button.
	 * @param star the star rectangle that was clicked
	 */
	private void starClicked(Rectangle star){
		if(activeStar == null){
			engine.removeEntity(playButton);
			playButton = engine.entityCreator.createButton(Engine.screenWidth-80, 200, "button_play"); 
			playRect = new Rectangle(Engine.screenWidth-80,200,80,32);
		} else {
			activeStar.getComponent(SelectedFlag.class).setFlag(false); // deactivates current star
		}
		String coords = star.x+","+star.y;
		activeStar = stars.get(coords);
		activeStar.getComponent(SelectedFlag.class).setFlag(true); 
	}
	
	/**
	 * Unregisters all the entities from the engine and its systems. Used when
	 * switching from the overworld.
	 */
	public void unregister(){
//		activeStar = null; // Possibly leave this with an active one, so that if you return to the ship it wont be inactive
//		playRect = null;
		
		// Unregisters all the stars
		for(Entity star : stars.values()){
			engine.removeEntity(star);
		}
		engine.removeEntity(playButton);
		engine.removeEntity(menuButton);
//		playButton = null;
	}
	
	/**
	 * Registers all the stars and other entities. Used when restoring the 
	 * overworld system.
	 */
	public void register(){
		// Register all the stars
		for(Entity star : stars.values()){
			engine.addEntity(star);
		}
		if(playButton != null){
			engine.addEntity(playButton);
		}
		if(menuButton != null){
			engine.addEntity(menuButton);
		}
	}

	/**
	 * Creates a number of stars
	 */
	private void setupStars(){
		int radius = 50;
		
		Random rand = new Random(1234L); // Make seed depenendant later
		
		for(int i=1;i<360*3;i+=45){
			double rad = i*Math.PI/180;
			int x = (int)(radius*Math.cos(rad)+400);
			int y = (int)(radius*Math.sin(rad)+400);
			createStar(x,y,rand.nextLong(),("Star"+i/15));
			radius += 10;
		}
	}
	
	/**
	 * Creates a star for the overworld and adds it to the star hashmap.
	 * 
	 * @param x x-coordinate for the star
	 * @param y y-coordinate for the star
	 * @param seed the seed that the stars dungeon will be using
	 * @param starname the name of the star
	 */
	private void createStar(int x, int y, long seed, String starname){ 
		Entity star = engine.entityCreator.createStar(x, y, seed, starname);
		starRectangles.add(new Rectangle(x,y,16,16));// test case
		stars.put((x+","+y),star);
	}
	
	/**
	 * Returns the currently active star
	 * @return the active star
	 */
	public Entity getActiveStar(){
		return activeStar;
	}
}
