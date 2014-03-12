package se.chalmers.roguelike.Systems;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.Components.Position;
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
					activeStar = stars.get(coords);
					System.out.println("New active star: "+activeStar+" ... x: "+star.x);
				}
			}
		}
	}

	private void setupStars(){
		createStar(10,10, "fuuuuu");
		createStar(234,43, "B0rk");
		createStar(123,94,"PX1234");
	}
	
	private void createStar(int x, int y, String starname){ 
		Entity star = engine.entityCreator.createStar(x,y,starname);
		starRectangles.add(new Rectangle(x,y,16,16));// test case
		stars.put((x+","+y),star);
	}
	
	public Entity getActiveStar(){
		return activeStar;
	}
}
