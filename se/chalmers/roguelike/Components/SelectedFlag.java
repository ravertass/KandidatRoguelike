package se.chalmers.roguelike.Components;

/**
 * This is a class used to keep a flag. Can be useful for ECS.
 *
 */
public class SelectedFlag implements IComponent{
	boolean flag;
	public SelectedFlag(){
		this(false);
	}
	public SelectedFlag(boolean flag){
		this.flag = flag;
	}
	public boolean getFlag(){
		return flag;
	}
	public void setFlag(boolean flag){
		this.flag = flag;
	}
}
