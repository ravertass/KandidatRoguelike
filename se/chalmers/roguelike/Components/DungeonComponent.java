package se.chalmers.roguelike.Components;

import se.chalmers.roguelike.World.Dungeon;

public class DungeonComponent implements IComponent{
	// Stupid name, but just calling it dungeon would create a name collision, this is used 
	// Maybe combine with Seed, this is here to follow the ECS system in an easy way
	private Dungeon dungeon;
	public DungeonComponent(){
		dungeon = null;
	}
	public DungeonComponent(Dungeon dungeon){
		this.dungeon = dungeon;
	}
	public Dungeon getDungeon(){
		return dungeon;
	}
	public void setDungeon(Dungeon dungeon){
		this.dungeon = dungeon;
	}
	public IComponent clone() {
		return new DungeonComponent(dungeon);
	}
}
