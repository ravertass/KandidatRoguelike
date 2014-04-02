package se.chalmers.roguelike.Components;
/**
 * A component for enteties which have two names.
 * @author twister
 *
 */
public class DoubleName implements IComponent{
	
	private String realName;
	
	public DoubleName(String s) {
		this.realName = s;
	}
	
	public String getRealName() {
		return this.realName;
	}
}
