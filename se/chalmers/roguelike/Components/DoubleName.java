package se.chalmers.roguelike.Components;

/**
 * A component for entities which have two names.
 * 
 * @author twister
 */
public class DoubleName implements IComponent {

	private String realName;

	public DoubleName(String s) {
		this.realName = s;
	}

	public String getRealName() {
		return this.realName;
	}

	public IComponent clone() {
		return new DoubleName(new String(realName));
	}
}