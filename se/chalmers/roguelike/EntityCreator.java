package se.chalmers.roguelike;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.BlocksLineOfSight;
import se.chalmers.roguelike.Components.BlocksWalking;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.FieldOfView;
import se.chalmers.roguelike.Components.FirstStarFlag;
import se.chalmers.roguelike.Components.Gold;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.Highlight;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.MobType;
import se.chalmers.roguelike.Components.Player;
import se.chalmers.roguelike.Components.PlotAction;
import se.chalmers.roguelike.Components.PlotLoot;
import se.chalmers.roguelike.Components.Pocketable;
import se.chalmers.roguelike.Components.PopupText;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Seed;
import se.chalmers.roguelike.Components.SelectedFlag;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.Stair;
import se.chalmers.roguelike.Components.StatusEffects;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;
import se.chalmers.roguelike.Systems.ItemSystem;
import se.chalmers.roguelike.World.Dungeon;

/**
 * EntityCreator creates entities, either by using pre-defined entity combinations, or by providing lists
 * of components that should be added
 */
public class EntityCreator {
	
	private static final String[] bossSprites = { "ginger_boy", "ginger_girl", "smurf", "woody", "megaman",
	"devilmarine" };
	private static final String[] lootSprites = { "blue", "green", "purple", "red", "yellow" };

	private Engine engine;

	/**
	 * Spawns a new entity creator instance
	 * @param engine the game engine in use
	 */
	public EntityCreator(Engine engine) {
		this.engine = engine;
	}

	/**
	 * Creates a player
	 * @param spaceClass the class of the player
	 * @param spaceRace the race of the player
	 * @param actor 
	 * @return the entity that represents a player
	 */
	public static Entity createPlayer(SpaceClass spaceClass, SpaceRace spaceRace, Actor actor) {
		Entity player = new Entity("(Player) " + actor.toString());
		player.add(new MobType(MobType.Type.PLAYER));
		player.add(new Health(50));
		player.add(new TurnsLeft(1));
		player.add(new Input());
		Sprite sprite = null;
		if (spaceRace == SpaceRace.SPACE_ALIEN) {
			sprite = new Sprite("mobs/mob_blue");
		} else if (spaceRace == SpaceRace.SPACE_HUMAN) {
			sprite = new Sprite("mobs/mob_marine");
		} else if (spaceRace == SpaceRace.SPACE_DWARF) {
			sprite = new Sprite("mobs/mob_dwarf");
		}
		
		player.add(sprite);
		player.add(new Position(44, 44));
		player.add(new Direction());
		player.add(new Player());
		player.add(new Attribute(actor.toString(), spaceClass, spaceRace, 1, 50));
		player.add(new Weapon(2, 6, 0, TargetingSystem.LINE, 1, 10));
		ArrayList<Entity> inv = new ArrayList<Entity>();
		inv.add(ItemSystem.getRandomPotion());
		inv.add(ItemSystem.getRandomPotion());
		for (Prop prop : actor.getProps()) {
			inv.add(createPlotLoot(prop, 0, 0));
		}
		
		player.add(new StatusEffects());
		player.add(new Inventory(inv));
		player.add(new Gold(0));
		player.add(new BlocksWalking(true));
		return player;
	}
	
	public static Entity createPlotLoot(Prop prop, int x, int y) {
		String name = "(Loot) " + prop;
		Entity plotLoot = new Entity(name);
		String spritePrefix = "keycard_";
		String sprite = spritePrefix + lootSprites[prop.getType()];
		plotLoot.add(new Position(x, y));
		plotLoot.add(new Sprite(sprite));
		plotLoot.add(new Pocketable());
		plotLoot.add(new PlotLoot(prop));
		
		return plotLoot;
	}
	
	public static Entity createBoss(Actor actor, int x, int y) {
		String name = "(Boss) " + actor.toString();
		Entity boss = new Entity(name);

		String spritePrefix = "mobs/mob_";
		String sprite = spritePrefix + bossSprites[actor.getType()];
		boss.add(new MobType(MobType.Type.BOSS));
		boss.add(new Health(20));
		boss.add(new TurnsLeft(1));
		boss.add(new Input());
		boss.add(new Sprite(sprite));
		ArrayList<Entity> inv = new ArrayList<Entity>();
		//for (Prop prop : actor.getProps()) {
//			inv.add(createPlotLoot(prop, 0, 0));
		//}
		boss.add(new Inventory(inv));

		boss.add(new Direction());
		boss.add(new AI());
		Attribute attribute = new Attribute(name, SpaceClass.SPACE_ROGUE,
				SpaceRace.SPACE_DWARF, 1, 50);
		boss.add(new BlocksWalking(true));
		boss.add(new Weapon(2, 6, 0, TargetingSystem.SINGLE_TARGET, 1, 1)); // hardcoded equals bad
		boss.add(new FieldOfView(8)); // hardcoded equals bad
		boss.add(attribute);
		boss.add(new Position(x, y));
		
		return boss;
	}

	/**
	 * Creates an enemy and directly adds it to the engine
	 * @param name name of the enemy
	 */
	public void createEnemy(String name) {
		Entity enemy = new Entity("(Enemy) " + name);
		enemy.add(new Health(10));
		enemy.add(new TurnsLeft(1));
		enemy.add(new Input());
		enemy.add(new Sprite("mobs/mob_devilmarine"));
		enemy.add(new Position(45, 44));
		enemy.add(new Direction());
		enemy.add(new AI());
		enemy.add(new FieldOfView(10));
		enemy.add(new Attribute("Enemy", Attribute.SpaceClass.SPACE_ROGUE, Attribute.SpaceRace.SPACE_ALIEN, 1, 50));
		enemy.add(new Inventory()); // here items that the enemy drops should be added
		enemy.add(new BlocksWalking(true));
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
		enemy.add(new BlocksWalking(true));
		engine.addEntity(enemy);
	}

	/**
	 * Creates a hilight marker and adds it to the engine
	 */
	public void createHighlight() {
		Entity highlight = new Entity("Highlight");
		highlight.add(new Sprite("highlight2"));
		highlight.add(new Position(1,1));
		highlight.add(new Highlight());
		engine.addEntity(highlight);
	}
	
	/**
	 * Creates a highlight at a specific position and registers it to the engine
	 * @param pos the position of the highlight
	 * @return returns the highlight entity
	 */
	public Entity createHighlight(Position pos) {
		Entity highlight = new Entity("Highlight");
		highlight.add(new Sprite("transparenthighlight"));
		highlight.add(pos);
		highlight.add(new Highlight());
		engine.addEntity(highlight);
		return highlight;
	}
	
	/**
	 * Creates a star and adds it to the engine
	 * @param x X-coordinate of the star
	 * @param y Y-coordinate of the star
	 * @param seed the seed for the dungeon the star will contain
	 * @return the star entity
	 */
	public Entity createStar(int x, int y, long seed) {
		return createStar(x, y, seed, "Star", false);
	}
	
	public Entity createStar(int x, int y, long seed, String starname, boolean isFirstStar) {
		Entity star = new Entity(starname);
		if (isFirstStar) {
			star.add(new FirstStarFlag());
		}
		star.add(new Sprite("star"));
		star.add(new Position(x,y));
		star.add(new Seed(seed));
		star.add(new SelectedFlag(false));
		star.add(new DungeonComponent());
		star.add(new PlotAction());
		engine.addEntity(star);
		return star;
	}
	
	/**
	 * Create a new button and registers it in the engine
	 * 
	 * @param x X-coordinate of the button 
	 * @param y Y-coordinate of the button
	 * @param spriteName the name of the sprite the button will use
	 * @param width width of the button
	 * @param height height of the button
	 * @return the entity representing a button
	 */
	public Entity createButton(int x, int y, String spriteName, int width, int height){
		Entity button = new Entity("button");
		Sprite sprite = new Sprite(spriteName,width, height);
		sprite.setLayer(3);
		button.add(sprite);
		button.add(new Position(x,y));
//		engine.addEntity(button);
		return button;
	}
	
	/**
	 * Create a stack of gold
	 * @param x X-coordinate of the gold
	 * @param y Y-coordinate of the gold
	 * @param amount the amount of gold
	 * @return an entity for the gold
	 */
	public static Entity createGold(int x, int y, int amount){
		Entity gold = new Entity("gold");
		gold.add(new Sprite("cash_small_amt"));
		gold.add(new Position(x,y));
		gold.add(new Gold(amount));
		return gold;
	}
	
	/**
	 * Creates a stair entity
	 * @param x X-coordinate for the stairs
	 * @param y Y-coordinate for the stairs
	 * @param spawnX the X-coordinate where the player should spawn when using the stairs
	 * @param spawnY the Y-coordinate where the player should spawn when using the stairs
	 * @param sprite the sprite of the stairs
	 * @param dungeon dungeon that should be loaded when the stairs are used
	 * @return the entity for the stairs
	 */
	public static Entity createStairs(int x, int y, int spawnX, int spawnY, String sprite, Dungeon dungeon){
		Entity stairs = new Entity("stairs");
		stairs.add(new Position(x,y));
		stairs.add(new Stair(spawnX,spawnY));
		stairs.add(new Sprite(sprite));
		stairs.add(new DungeonComponent(dungeon));
		return stairs;
	}
	
	/**
	 * Creates a door entity
	 * @param x X-coordinate for the door
	 * @param y Y-coordinate for the door
	 * @param sprite the sprite of the door
	 * @param open status flag if the door should be open or not
	 * @return the door entity
	 */
	public static Entity createDoor(int x, int y, String sprite, boolean open){
		Entity door = new Entity("door");
		door.add(new Position(x,y));
		door.add(new Sprite(sprite));
		door.add(new BlocksLineOfSight(!open));
		door.add(new BlocksWalking(!open));
		return door;
	}
	
	/**
	 * Creates a generic entity based on what components are provided
	 * 
	 * @param name name of the entity
	 * @param components a list of components to be included
	 * @return a new entity
	 */
	public static Entity createEntity(String name, ArrayList<IComponent> components){
		Entity entity = new Entity(name);
		for (IComponent component : components) {
			entity.add(component);
		}
		return entity;
	}
	
	/**
	 * Creates a popup
	 * @param text text that should be written on the popup
	 * @param x x-coordinate of the popup
	 * @param y y-coordinate of the popup
	 * @param width width of the popup
	 * @param height height of the popup
	 * @return the popup entity
	 */
	public Entity createPopup(ArrayList<String> text, int x, int y, int width, int height) {
		Entity popup = new Entity("popup");
		Sprite sprite = new Sprite("popupbackground", width, height);
		sprite.setLayer(2);
		popup.add(sprite);
		popup.add(new Position(x,y));
		popup.add(new PopupText(text));
		return popup;
	}
	
	/**
	 * Creates a space ship
	 * @param x x-coordinate of the ship
	 * @param y y-coordinate of the ship
	 * @return a space ship entity
	 */
	public Entity createSpaceShip(int x, int y) {
		Entity spaceShip = new Entity("Spaceship");
		spaceShip.add(new Sprite("mobs/mob_bear", 32, 32));
		spaceShip.add(new Position(x, y));
		return spaceShip;
	}

}
