package se.chalmers.roguelike.Tests;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Systems.MobSpriteSystem;
import se.chalmers.roguelike.Systems.RenderingSystem;

public class GraphicsTest {

	public static void main(String[] args) {
		RenderingSystem renderingSys = new RenderingSystem();
		MobSpriteSystem mobSpriteSys = new MobSpriteSystem();
		
		Entity player = new Entity();
		player.add(new Sprite("player"));
		player.add(new Position(10,10));
		player.add(new Direction(Direction.Dir.EAST));
		renderingSys.addEntity(player);
		mobSpriteSys.addEntity(player);
		
		for (int i = 0; i < 100; i++) {
			mobSpriteSys.update();
			renderingSys.update();
		}
	}
}
