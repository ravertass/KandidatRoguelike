package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Health;
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
	}
	
	public void createPlayer(){
		Entity player = new Entity();
		player.add(new Health(100));
		player.add(new TurnsLeft(1));
		player.add(new Input());
		player.add(new Sprite("player"));
		player.add(new Position(10,10));
		player.add(new Direction());
		//engine.addToInputSys(player);
		//engine.addToRenderingSys(player); // depreached, left to show
		engine.addEntity(player); // should this be used? possibly just pass it to system directly
	}

}
