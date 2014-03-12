package se.chalmers.plotgen.PlotData;


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
		MEET, // [Actor] [MEETS] [actor]
		VISIT, // [Actor] [VISITS] [scene]
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
	// The objectScene determines what location the action is done to
	private Scene objectScene = null;

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
			Prop objectProp, Scene objectScene) {

		this.type = type;
		this.subject = subject;

		if (type == ActionType.KILL) {
			this.objectActor = objectActor;
		}

		if (type == ActionType.TAKE) {
			this.objectProp = objectProp;
		}

		if (type == ActionType.MEET) {
			this.objectActor = objectActor;
		}
		
		if (type == ActionType.VISIT) {
			this.objectScene = objectScene;
		}

		if (type == ActionType.GIVE) {
			this.objectActor = objectActor;
			this.objectProp = objectProp;
		}
	}

	/**
	 * To be used for KILL and MEET
	 * 
	 * @param type
	 * @param subject
	 * @param objectActor
	 */
	public Action(ActionType type, Actor subject, Actor objectActor) {
		this(type, subject, objectActor, null, null);
	}

	/**
	 * To be used for TAKE
	 * 
	 * @param type
	 * @param subject
	 * @param objectProp
	 */
	public Action(ActionType type, Actor subject, Prop objectProp) {
		this(type, subject, null, objectProp, null);
	}
	
	/**
	 * To be used for VISIT
	 * 
	 * @param type
	 * @param subject
	 * @param objectScene
	 */
	public Action(ActionType type, Actor subject, Scene objectScene) {
		this(type, subject, null, null, objectScene);
	}
	
	/**
	 * To be used for GIVE
	 * 
	 * @param type
	 * @param subject
	 * @param objectActor
	 * @param objectProp
	 */
	public Action(ActionType type, Actor subject, Actor objectActor, Prop objectProp) {
		this(type, subject, objectActor, objectProp, null);
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

		if (type == ActionType.MEET) {
			returnString = subject + " meets " + objectActor;
		}
		
		if (type == ActionType.VISIT) {
			returnString = subject + " visits " + objectScene;
		}

		if (type == ActionType.GIVE) {
			returnString = subject + " gives " + objectProp + " to "
					+ objectActor;
		}

		return returnString;
	}
}
