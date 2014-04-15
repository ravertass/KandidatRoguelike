package se.chalmers.roguelike.Components;

public class BlocksWalking implements IComponent {

	/**
	 * This class acts as a flag to block walking basically.
	 */
	private boolean blocksWalking;

	public BlocksWalking() {
		blocksWalking = true;
	}

	public BlocksWalking(boolean blocksWalking) {
		this.blocksWalking = blocksWalking;
	}

	public boolean getBlocksWalking() {
		return blocksWalking;
	}

	public void setBlocksWalking(boolean blocksWalking) {
		this.blocksWalking = blocksWalking;
	}

	public IComponent clone() {
		return new BlocksWalking(blocksWalking);
	}
}