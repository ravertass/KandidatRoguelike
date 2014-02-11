package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Entities.Entity;

public class EntityCreator {
	
	private Engine engine;
	private Input input; // should be the same for all entities
	public EntityCreator(Engine engine){
		this.engine = engine;
		input = new Input();
	}
	
	public void createPlayer(){
		Entity player = new Entity();
		player.add(new Health(100));
		player.add(new TurnsLeft(1));
		player.add(new Input());
		engine.addToInputSys(player);
		engine.addEntity(player); // should this be used? possibly just pass it to system directly
	}

}
