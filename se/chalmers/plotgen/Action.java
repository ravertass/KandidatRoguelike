package se.chalmers.plotgen;

/**
 * This class models an action in a game, e.g. [Leif] [KILLS] [Uffe]
 * 
 * These actions will be at the edges of the plot graph and should easily
 * translate to gameplay actions from the player in the game.
 * 
 * @author fabian
 */

public class Action {

	public enum ActionType {
		KILL, // [Actor] [KILLS] [actor]
		TAKE, // [Actor] [TAKES] [prop]
		VISIT, // [Actor] [VISITS] [actor]
		GIVE // [Actor] [GIVES] [prop] (to) [actor]
	}

	// These are always essential parts of an action
	// The type determines what kind of action it is
	private ActionType type;
	// The subject determines who does the action
	private Actor subject;

	// An action will always have at least one of these,
	// but not necessarily both
	// The objectActor determines who the action is done to
	private Actor objectActor = null;
	// The objectProp determines what thing the action is done to
	private Prop objectProp = null;

	/**
	 * @param type
	 *            The type of action, as defiend by the ActionType enum
	 * @param subject
	 *            The actor who does the acton
	 * @param objectActor
	 *            The actor the action is done to
	 * @param objectProp
	 *            The prop the action is done to
	 */
	public Action(ActionType type, Actor subject, Actor objectActor,
			Prop objectProp) {

		this.type = type;
		this.subject = subject;

		if (type == ActionType.KILL) {
			this.objectActor = objectActor;
		}

		if (type == ActionType.TAKE) {
			this.objectProp = objectProp;
		}

		if (type == ActionType.VISIT) {
			this.objectActor = objectActor;
		}

		if (type == ActionType.GIVE) {
			this.objectActor = objectActor;
			this.objectProp = objectProp;
		}
	}

	/**
	 * To be used for KILL and VISIT
	 * 
	 * @param type
	 * @param subject
	 * @param objectActor
	 */
	public Action(ActionType type, Actor subject, Actor objectActor) {
		this(type, subject, objectActor, null);
	}

	/**
	 * To be used for TAKE
	 * 
	 * @param type
	 * @param subject
	 * @param objectProp
	 */
	public Action(ActionType type, Actor subject, Prop objectProp) {
		this(type, subject, null, objectProp);
	}

	/**
	 * For testing purposes
	 */
	@Override
	public String toString() {

		String returnString = null;

		if (type == ActionType.KILL) {
			returnString = subject + " kills " + objectActor;
		}

		if (type == ActionType.TAKE) {
			returnString = subject + " takes " + objectProp;
		}

		if (type == ActionType.VISIT) {
			returnString = subject + " visits " + objectActor;
		}

		if (type == ActionType.GIVE) {
			returnString = subject + " gives " + objectProp + " to "
					+ objectActor;
		}

		return returnString;
	}
}
