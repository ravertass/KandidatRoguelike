package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;

/**
 * A class with static methods to generated hard-coded operators.
 */
public class Operators {

	/**
	 * Operator counterpart for Action [Actor] [KILLS] [actor]
	 * @param self
	 * @param victim
	 * @return the newly generated kill operator
	 */
	public static Operator killOperator(Actor self, Actor victim) {

		// Set conditions that must be true
		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new LivesCondition(victim));
		beTrue.add(new SamePlaceCondition(self, victim));

		// Set conditions that must be false
		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		// Set conditions that will be set true
		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		for (Prop prop : victim.getProps()) {
			setTrue.add(new IsAtLocationCondition(prop, victim.getLocation()));
		}

		// Set conditions that will be set false
		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		setFalse.add(new LivesCondition(victim));
		
		// Add the corresponding action to the operator
		Action action = new Action(Action.ActionType.KILL, self, victim);
		
		// The weight for killing actions
		int weight = 1;

		return new Operator(beTrue, beFalse, setTrue, setFalse, action, weight);
	}

	/**
	 * Operator counterpart for Action [Actor] [TAKES] [prop]
	 * @param self
	 * @param victim
	 * @return the newly generated take operator
	 */
	public static Operator takeOperator(Actor self, Prop prop) {

		// Set conditions that must be true
		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new SamePlaceCondition(self, prop));

		// Set conditions that must be false
		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		// Set conditions that will be set true
		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new BelongsToCondition(prop, self));

		// Set conditions that will be set false
		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		
		// Add the corresponding action to the operator
		Action action = new Action(Action.ActionType.TAKE, self, prop);
		
		// The weight for taking actions
		int weight = 5;

		return new Operator(beTrue, beFalse, setTrue, setFalse, action, weight);
	}

	/**
	 * Operator counterpart for Action [Actor] [MEETS] [actor]
	 * @param self
	 * @param victim
	 * @return the newly generated meet operator
	 */
	public static Operator meetOperator(Actor self, Actor actor) {

		// Set conditions that must be true
		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new LivesCondition(actor));

		// Set conditions that must be false
		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();
		beFalse.add(new SamePlaceCondition(self, actor));

		// Set conditions that will be set true
		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new SamePlaceCondition(self, actor));

		// Set conditions that will be set false
		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		
		// Add the corresponding action to the operator
		Action action = new Action(Action.ActionType.MEET, self, actor);
		
		// The weight for meeting actions
		int weight = 10;

		return new Operator(beTrue, beFalse, setTrue, setFalse, action, weight);
	}

	/**
	 * Operator counterpart for Action [Actor] [VISITS] [scene]
	 * @param self
	 * @param victim
	 * @return the newly generated visit operator
	 */
	public static Operator visitOperator(Actor self, Scene targetLocation) {

		// Set conditions that must be true
		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));

		// Set conditions that must be false
		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();
		beFalse.add(new IsAtLocationCondition(self, targetLocation));

		// Set conditions that will be set true
		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new IsAtLocationCondition(self, targetLocation));

		// Set conditions that will be set false
		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		
		// Add the corresponding action to the operator
		Action action = new Action(Action.ActionType.VISIT, self, targetLocation);
		
		// The weight for visiting actions
		int weight = 5;

		return new Operator(beTrue, beFalse, setTrue, setFalse, action, weight);
	}

	/**
	 * Operator counterpart for Action [Actor] [GIVES] [prop] to [actor]
	 * @param self
	 * @param victim
	 * @return the newly generated give operator
	 */
	public static Operator giveOperator(Actor self, Actor recipient, Prop prop) {

		// Set conditions that must be true
		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new LivesCondition(recipient));
		beTrue.add(new SamePlaceCondition(self, recipient));
		beTrue.add(new BelongsToCondition(prop, self));

		// Set conditions that must be false
		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		// Set conditions that will be set true
		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new BelongsToCondition(prop, recipient));

		// Set conditions that will be set false
		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();

		// Add the corresponding action to the operator
		Action action = new Action(Action.ActionType.GIVE, self, recipient, prop);
		
		// The weight for giving actions
		int weight = 5;
		
		return new Operator(beTrue, beFalse, setTrue, setFalse, action, weight);
	}
}
