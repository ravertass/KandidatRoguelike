package se.chalmers.roguelike;

import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.FieldOfView;
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

	public EntityCreator(Engine engine) {
		this.engine = engine;
		this.input = new Input();
	}

	public void createPlayer() {
		Entity player = new Entity("Player");
		player.add(new Health(5));
		player.add(new TurnsLeft(1));
		player.add(new Input());
		player.add(new Sprite("mobs/mob_knight"));
		player.add(new Position(1, 1));
		player.add(new Direction());
		player.add(new Player());
		player.add(new Attribute("Player", 1));
		player.add(new Weapon(2, 6, -3));
		engine.addEntity(player);
	}

	public void createEnemy(String name) {
		Entity enemy = new Entity("(Enemy) " + name);
		enemy.add(new Health(10));
		enemy.add(new TurnsLeft(1));
		enemy.add(new Input());
		enemy.add(new Sprite("mobs/mob_devilmarine"));
		enemy.add(new Position(2, 1));
		enemy.add(new Direction());
		enemy.add(new AI());
		enemy.add(new Attribute("Enemy", 1));
		engine.addEntity(enemy);
	}

	/**
	 * Creates an enemyEntity.
	 * 
	 * @param health
	 *            : The max health of the enemy.
	 * @param sprite
	 *            : The name of the sprite as a string, e.g.
	 *            "mobs/mob_devilmarine".
	 * @param startPos
	 *            : The starting position of the enemy.
	 * @param attribute
	 *            : An attribute object.
	 */
	public void createEnemy(int health, String sprite, Position startPos,
			Attribute attribute) {
		Entity enemy = new Entity("Enemy");
		enemy.add(new Health(health));
		enemy.add(new TurnsLeft(1));
		enemy.add(new Input());
		enemy.add(new Sprite(sprite));
		enemy.add(startPos);
		enemy.add(new Direction());
		enemy.add(new AI());
		enemy.add(attribute);
		engine.addEntity(enemy);
	}

	public void createHighlight() {
		Entity highlight = new Entity("Highlight");
		highlight.add(new Sprite("highlight2"));
		highlight.add(new Position(1,1));
		highlight.add(new Highlight());
		engine.addEntity(highlight);
	}
	
	public Entity createHighlight(Position pos) {
		Entity highlight = new Entity("Highlight");
		highlight.add(new Sprite("transparenthighlight"));
		highlight.add(pos);
		highlight.add(new Highlight());
		engine.addEntity(highlight);
		return highlight;
	}

}
