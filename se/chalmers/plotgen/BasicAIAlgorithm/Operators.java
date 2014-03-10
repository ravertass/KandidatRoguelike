package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;

/**
 * Lots of hard-coded operators in this class.
 * 
 * @author fabian
 */
public class Operators {

	public static Operator killOperator(Actor self, Actor victim, Scene location) {

		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new LivesCondition(victim));
		beTrue.add(new IsVisitingCondition(self, location));
		beTrue.add(new IsVisitingCondition(victim, location));

		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		for (Prop prop : victim.getProps()) {
			setTrue.add(new IsPlacedAtCondition(prop, location));
		}

		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		setFalse.add(new LivesCondition(victim));
		setFalse.add(new IsVisitingCondition(victim, location));
		for (Prop prop : victim.getProps()) {
			setFalse.add(new BelongsToCondition(prop, victim));
		}

		return new Operator(beTrue, beFalse, setTrue, setFalse);
	}

	public static Operator takeOperator(Actor self, Prop prop, Scene location) {

		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new IsVisitingCondition(self, location));
		beTrue.add(new IsPlacedAtCondition(prop, location));

		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new BelongsToCondition(prop, self));

		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		setFalse.add(new IsPlacedAtCondition(prop, location));

		return new Operator(beTrue, beFalse, setTrue, setFalse);
	}

	public static Operator visitOperator(Actor self, Scene startScene,
			Scene targetScene) {

		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new IsVisitingCondition(self, startScene));

		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new IsVisitingCondition(self, targetScene));

		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		setFalse.add(new IsVisitingCondition(self, startScene));

		return new Operator(beTrue, beFalse, setTrue, setFalse);
	}

	public static Operator giveOperator(Actor self, Actor recipient, Prop prop,
			Scene location) {

		ArrayList<ICondition> beTrue = new ArrayList<ICondition>();
		beTrue.add(new LivesCondition(self));
		beTrue.add(new LivesCondition(recipient));
		beTrue.add(new IsVisitingCondition(self, location));
		beTrue.add(new IsVisitingCondition(recipient, location));
		beTrue.add(new BelongsToCondition(prop, self));

		ArrayList<ICondition> beFalse = new ArrayList<ICondition>();

		ArrayList<ICondition> setTrue = new ArrayList<ICondition>();
		setTrue.add(new BelongsToCondition(prop, recipient));

		ArrayList<ICondition> setFalse = new ArrayList<ICondition>();
		setFalse.add(new BelongsToCondition(prop, self));

		return new Operator(beTrue, beFalse, setTrue, setFalse);
	}
}
