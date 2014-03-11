package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;

/**
 * Lots of hard-coded operators in this class.
 * 
 * @author fabian
 */
public class Operators {

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

		return new Operator(beTrue, beFalse, setTrue, setFalse, action);
	}

	public static Operator takeOperator(Actor self, Prop prop) {

		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new SamePlaceCondition(self, prop));

		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new BelongsToCondition(prop, self));

		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		
		// Add the corresponding action to the operator
		Action action = new Action(Action.ActionType.TAKE, self, prop);

		return new Operator(beTrue, beFalse, setTrue, setFalse, action);
	}

	public static Operator visitOperator(Actor self, Scene targetLocation) {

		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));

		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new IsAtLocationCondition(self, targetLocation));

		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		
		// Add the corresponding action to the operator
		//TODO: Action action = new Action(Action.ActionType.KILL, self, victim);
		Action action = null;

		return new Operator(beTrue, beFalse, setTrue, setFalse, action);
	}

	public static Operator giveOperator(Actor self, Actor recipient, Prop prop) {

		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new LivesCondition(recipient));
		beTrue.add(new SamePlaceCondition(self, recipient));
		beTrue.add(new BelongsToCondition(prop, self));

		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new BelongsToCondition(prop, recipient));

		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();

		// Add the corresponding action to the operator
		Action action = new Action(Action.ActionType.GIVE, self, recipient, prop);
		
		return new Operator(beTrue, beFalse, setTrue, setFalse, action);
	}
}
