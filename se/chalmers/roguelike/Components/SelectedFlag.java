package se.chalmers.roguelike.Components;

/**
 * A component to see if an entity is selected
 */
public class SelectedFlag implements IComponent {

	boolean flag;

	public SelectedFlag() {
		this(false);
	}

	public SelectedFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public IComponent clone() {
		return new SelectedFlag(flag);
	}
}