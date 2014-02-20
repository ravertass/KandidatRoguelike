package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Highlight;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Player;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;

public class EntityCreator {
	
	private Engine engine;
	private Input input; // should be the same for all entities
	
	public EntityCreator(Engine engine){
		this.engine = engine;
		this.input = new Input();
	}
	
	public void createPlayer(){
		Entity player = new Entity();
		player.add(new Health(5));
		player.add(new TurnsLeft(1));
		player.add(new Input());
		player.add(new Sprite("mobs/mob_knight"));
		player.add(new Position(1,1));
		player.add(new Direction());
		player.add(new Player());
		player.add(new se.chalmers.roguelike.Components.Character("Player", 1));
		player.add(new Weapon(2,6,-3));
		engine.addEntity(player);
	}
	
	public void createEnemy(){
		Entity enemy = new Entity();
		enemy.add(new Health(5));
		enemy.add(new TurnsLeft(1));
		enemy.add(new Input());
		enemy.add(new Sprite("mobs/mob_devilmarine"));
		enemy.add(new Position(2,1));
		enemy.add(new Direction());
		enemy.add(new AI());
		enemy.add(new se.chalmers.roguelike.Components.Character("Enemy", 1));
		engine.addEntity(enemy);
	}
	
	public void createHighlight() {
		Entity highlight = new Entity();
		highlight.add(new Sprite("highlight2"));
		highlight.add(new Position(10,10));
		highlight.add(input);
		highlight.add(new Highlight());
		engine.addEntity(highlight);
	}

}
