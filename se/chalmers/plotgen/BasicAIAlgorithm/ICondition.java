package se.chalmers.plotgen.BasicAIAlgorithm;

public interface ICondition {

	/**
	 * Get the boolean value for the condition
	 */
	public boolean get();

	/**
	 * Set the boolean value for the condition; will change the states related
	 * to the condition
	 */
	public void set(boolean bool);
}
