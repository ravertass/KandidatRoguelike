package se.chalmers.roguelike.Components;

public class BlocksLineOfSight implements IComponent {
	private boolean blockStatus;
	
	public BlocksLineOfSight(boolean blockStatus){
		this.blockStatus = blockStatus;
	}
	
	public boolean getBlockStatus(){
		return blockStatus;
	}
	
	public void setBlockStatus(boolean blockStatus){
		this.blockStatus = blockStatus;
	}
}
