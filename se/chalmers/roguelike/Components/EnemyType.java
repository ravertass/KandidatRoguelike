package se.chalmers.roguelike.Components;

public class EnemyType implements IComponent {
	public enum Type {
		GRUNT, BOSS
	}
	
	private Type type;
	
	public EnemyType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
