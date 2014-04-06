package se.chalmers.roguelike.Components;

public class MobType implements IComponent {
	public enum Type {
		PLAYER, GRUNT, BOSS
	}
	
	private Type type;
	
	public MobType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
