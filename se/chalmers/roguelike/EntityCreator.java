package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Entities.Entity;

public class EntityCreator {
	
	private Engine engine;
	
	public EntityCreator(Engine engine){
		this.engine = engine;
	}
	
	public void createPlayer(){
		Entity player = new Entity();
		player.add(new Health(100));
		player.add(new TurnsLeft(1));
		player.add(new Input());
		engine.addEntity(player); // should this be used? possibly just pass it to system directly
	}

}
