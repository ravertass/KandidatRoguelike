package se.chalmers.roguelike.Systems;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Seed;
import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Generator;
import se.chalmers.roguelike.util.Observer;


public class OverworldSystem implements ISystem, Observer{
	
	private ArrayList<Dungeon> dungeons;
	
	private Dungeon currentDungeon;
	private Engine engine;
	private ArrayList<Rectangle> starRectangles;
	
	private Entity activeStar;
	private HashMap<String,Entity> stars;
	final Position noClick = new Position(-1, -1);
	
	public OverworldSystem(Engine engine) {
		this.engine = engine;
		activeStar = null;
		stars = new HashMap<String,Entity>();
		starRectangles = new ArrayList<Rectangle>();
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

	public void notify(Enum<?> i) {
		if (Engine.gameState == Engine.GameState.OVERWORLD && i.equals(InputManager.InputAction.MOUSECLICK)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			for(Rectangle star : starRectangles){
				if(star.contains(mouseX,mouseY)){
					System.out.println("Star clicked!");
					String coords = star.x+","+star.y;
					boolean startWorld = false;
					if(stars.get(coords) == activeStar){
						// This means the star was clicked twice, and the world will load, change when a menu is implemented
						startWorld = true;
					}
					activeStar = stars.get(coords);
					long seed=activeStar.getComponent(Seed.class).getSeed();
					System.out.println("New active star: "+activeStar+" ... x: "+star.x);
					System.out.println("Stars seed: "+seed);
					System.out.println("Click again to start it");
					if(startWorld){
						if(activeStar.getComponent(DungeonComponent.class) == null){
							System.out.println("No dungeon found! Generating one.");
							Dungeon newWorld = new Dungeon(engine);
							newWorld.setWorld(50,50,new Generator(seed).toTiles());
							Engine.dungeon = newWorld;
						}
						unregister();
						Engine.gameState = Engine.GameState.DUNGEON;
					}
				}
			}
		}
	}
	
	public void unregister(){
		// Unregisters all the stars
		for(Entity star : stars.values()){
			engine.removeEntity(star);
		}
	}

	private void setupStars(){
		createStar(10, 10, 123, "fuuuuu");
		createStar(234, 43, 123455662345L, "B0rk");
		createStar(123, 94, 1234,"PX1234");
	}
	
	private void createStar(int x, int y, long seed, String starname){ 
		Entity star = engine.entityCreator.createStar(x, y, seed, starname);
		starRectangles.add(new Rectangle(x,y,16,16));// test case
		stars.put((x+","+y),star);
	}
	
	public Entity getActiveStar(){
		return activeStar;
	}
}
