package se.chalmers.roguelike;

import java.util.ArrayList;

import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Highlight;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.Player;
import se.chalmers.roguelike.Components.PopupText;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Seed;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;

public class EntityCreator {

	private Engine engine;

	public EntityCreator(Engine engine) {
		this.engine = engine;
	}

	public Entity createPlayer(SpaceClass spaceClass, SpaceRace spaceRace) {
		Entity player = new Entity("Player");
		player.add(new Health(5));
		player.add(new TurnsLeft(1));
		player.add(new Input());
//		player.add(new Sprite("mobs/mob_knight"));
		player.add(new Sprite("gravestone"));
		player.add(new Position(44, 44));
		player.add(new Direction());
		player.add(new Player());
		player.add(new Attribute("Player", spaceClass, spaceRace, 1, 50));
		player.add(new Weapon(2, 6, 0, TargetingSystem.BOX, 1, 10));
		ArrayList<Entity> a = new ArrayList<Entity>();
		a.add(player);
		a.add(player);
		player.add(new Inventory(a));
		//engine.addEntity(player);
		return player;
	}

	public void createEnemy(String name) {
		Entity enemy = new Entity("(Enemy) " + name);
		enemy.add(new Health(10));
		enemy.add(new TurnsLeft(1));
		enemy.add(new Input());
		enemy.add(new Sprite("mobs/mob_devilmarine"));
		enemy.add(new Position(45, 44));
		enemy.add(new Direction());
		enemy.add(new AI());
		enemy.add(new Attribute("Enemy", Attribute.SpaceClass.SPACE_ROGUE, Attribute.SpaceRace.SPACE_ALIEN, 1, 50));
		enemy.add(new Inventory()); // here items that the enemy drops should be added
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
	public Entity createStar(int x, int y, long seed){
		return createStar(x, y, seed, "Star");
	}
	public Entity createStar(int x, int y, long seed, String starname){
		Entity star = new Entity(starname);
		star.add(new Sprite("star"));
		star.add(new Position(x,y));
		star.add(new Seed(seed));
		star.add(new SelectedFlag(false));
		star.add(new DungeonComponent());
		engine.addEntity(star);
		return star;
	}
	public Entity createButton(int x, int y, String spriteName, int width, int height){
		Entity button = new Entity("button");
		button.add(new Sprite(spriteName,width, height)); // The 80 thing might screw it up
		button.add(new Position(x,y));
		engine.addEntity(button);
		System.out.println("New button added");
		return button;
	}
	
	public static Entity createEntity(String name, ArrayList<IComponent> components){
		Entity entity = new Entity(name);
		for (IComponent component : components) {
			entity.add(component);
		}
		return entity;
	}
	
	public Entity createPopup(ArrayList<String> text, int x, int y, int width, int height) {
		Entity popup = new Entity("popup");
		popup.add(new Sprite("popupbackground", width, height));
		popup.add(new Position(x,y));
		popup.add(new PopupText(text));
		return popup;
	}
}
