package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.World.Dungeon;

public class DungeonComponent implements IComponent {

	private Dungeon dungeon;

	public DungeonComponent() {
		dungeon = null;
	}

	public DungeonComponent(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public IComponent clone() {
		return new DungeonComponent(dungeon);
	}
}
