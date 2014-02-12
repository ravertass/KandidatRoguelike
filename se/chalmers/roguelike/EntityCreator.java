package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
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
		player.add(new Sprite("guy"));
		player.add(new Position(10,10));
		//engine.addToInputSys(player);
		//engine.addToRenderingSys(player); // depreached, left to show
		engine.addEntity(player); // should this be used? possibly just pass it to system directly
	}

}
