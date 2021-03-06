package se.chalmers.roguelike.Components;

public class BlocksLineOfSight implements IComponent {

	private boolean blockStatus;

	/**
	 * This is a flag for tiles to see if they block the sight or not
	 * 
	 * @param blockStatus
	 */
	public BlocksLineOfSight(boolean blockStatus) {
		this.blockStatus = blockStatus;
	}

	public boolean getBlockStatus() {
		return blockStatus;
	}

	public void setBlockStatus(boolean blockStatus) {
		this.blockStatus = blockStatus;
	}

	public IComponent clone() {
		return new BlocksLineOfSight(blockStatus);
	}
}