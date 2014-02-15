package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Highlight;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Entities.Entity;

public class EntityCreator {
	
	private Engine engine;
	private Input input; // should be the same for all entities
	
	public EntityCreator(Engine engine){
		this.engine = engine;
		this.input = new Input();
	}
	
	public void createPlayer(){
		Entity player = new Entity();
		player.add(new Health(100));
		player.add(new TurnsLeft(1));
		player.add(input);
		player.add(new Sprite("player"));
		player.add(new Position(10,10));
		player.add(new Direction());
		//engine.addToInputSys(player);
		//engine.addToRenderingSys(player); // depreached, left to show
		engine.addEntity(player); // should this be used? possibly just pass it to system directly
	}
	
	public void createHighlight() {
		Entity highlight = new Entity();
		highlight.add(new Sprite("highlight"));
		highlight.add(new Position(10,10));
		highlight.add(input);
		highlight.add(new Highlight());
		engine.addEntity(highlight);
	}

}
