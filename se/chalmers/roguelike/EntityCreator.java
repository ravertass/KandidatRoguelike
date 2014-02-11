package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Entities.Entity;

public class EntityCreator {
	
	private Engine engine;
	
	public EntityCreator(Engine engine){
		this.engine = engine;
	}
	
	public void createPlayer(){
		Entity player = new Entity();
		player.add(new Health(100));
		engine.addEntity(player);
	}

}
