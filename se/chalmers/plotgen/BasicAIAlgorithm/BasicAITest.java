package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;
import java.util.Random;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotGraph;

public class BasicAITest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		twoActorsTest();
	}

	// In this test, we find:
	// Sonic at Basen
	// A sword in NC
	// Also, Deltaparken
	// Sonic both wants the sword and to be in Deltaparken
	private static void oneActorTest() {
		// Create the SAPs
		Actor sonic = new Actor("Sonic");
		Prop sword = new Prop("sword");
		Scene basen = new Scene("Basen");
		Scene parken = new Scene("Deltaparken");
		Scene nc = new Scene("NC");
		// Add them to lists
		ArrayList<Scene> scenes = new ArrayList<Scene>();
		scenes.add(basen);
		scenes.add(nc);
		scenes.add(parken);
		ArrayList<Actor> actors = new ArrayList<Actor>();
		actors.add(sonic);
		ArrayList<Prop> props = new ArrayList<Prop>();
		props.add(sword);

		// We find Sonic in Basen
		sonic.setLocation(basen);
		// We find the sword in NC
		sword.setLocation(nc);

		// Now, we generate a plot graph using the BasicAIAlgorithm
		PlotGraph plotGraph = BasicAIAlgorithm.algorithm(scenes, actors, props,
				new Random());

		// Print out the plot graph!
		System.out.println(plotGraph);
	}

	/*
	 * The setAgentGoals() for the above test: // TODO: Testkod nedan
	 * ArrayList<ICondition> trueGoals = new ArrayList<ICondition>();
	 * ArrayList<ICondition> falseGoals = new ArrayList<ICondition>();
	 * ICondition getSword = new BelongsToCondition(props.get(0),
	 * agents.get(0).getSelf()); ICondition gotoParken = new
	 * IsAtLocationCondition(agents.get(0).getSelf(), scenes.get(2));
	 * trueGoals.add(getSword); trueGoals.add(gotoParken);
	 * agents.get(0).setTrueGoals(trueGoals);
	 * agents.get(0).setFalseGoals(falseGoals);
	 */

	// In this test, we find:
	// Mario, a mushroom and a fork in the kitchen
	// Bowser and poop in the bathroom
	// Also, a bedroom and a garden
	// Mario wants the poo
	// Bowser wants the fork
	private static void twoActorsTest() {
		// Create the SAPs
		Actor mario = new Actor("Mario");
		Actor bowser = new Actor("Bowser");
		Prop mushroom = new Prop("mushroom");
		Prop fork = new Prop("fork");
		Prop poo = new Prop("poo");
		Scene kitchen = new Scene("the kitchen");
		Scene bathroom = new Scene("the bathroom");
		Scene bedroom = new Scene("the bedroom");
		Scene garden = new Scene("the garden");
		// Add them to lists
		ArrayList<Scene> scenes = new ArrayList<Scene>();
		scenes.add(kitchen);
		scenes.add(bathroom);
		scenes.add(bedroom);
		scenes.add(garden);
		ArrayList<Actor> actors = new ArrayList<Actor>();
		actors.add(bowser);
		actors.add(mario);
		ArrayList<Prop> props = new ArrayList<Prop>();
		props.add(mushroom);
		props.add(fork);
		props.add(poo);

		// We find Mario in the kitchen and Bowser in the bathroom
		mario.setLocation(kitchen);
		bowser.setLocation(bathroom);
		// We find the mushroom and the fork in the kitchen, and the poo in the
		// bathroom
		mushroom.setLocation(kitchen);
		fork.setLocation(kitchen);
		poo.setOwner(bowser);

		// Now, we generate a plot graph using the BasicAIAlgorithm
		PlotGraph plotGraph = BasicAIAlgorithm.algorithm(scenes, actors, props,
				new Random());

		// Print out the plot graph!
		System.out.println(plotGraph);
	}
	/*
	 * Nedanstående kod genererar målen till testet ovan // TODO: Testkod nedan
	 * // Add Mario's goal ArrayList<ICondition> marioTrueGoals = new
	 * ArrayList<ICondition>(); ArrayList<ICondition> marioFalseGoals = new
	 * ArrayList<ICondition>(); ICondition getPoo = new
	 * BelongsToCondition(props.get(2), agents.get(1) .getSelf());
	 * marioTrueGoals.add(getPoo); agents.get(1).setTrueGoals(marioTrueGoals);
	 * agents.get(1).setFalseGoals(marioFalseGoals);
	 * 
	 * // Add Bowser's goal ArrayList<ICondition> bowserTrueGoals = new
	 * ArrayList<ICondition>(); ArrayList<ICondition> bowserFalseGoals = new
	 * ArrayList<ICondition>(); ICondition getMushroom = new
	 * BelongsToCondition(props.get(1), agents .get(0).getSelf());
	 * bowserTrueGoals.add(getMushroom);
	 * agents.get(0).setTrueGoals(bowserTrueGoals);
	 * agents.get(0).setFalseGoals(bowserFalseGoals);
	 */

}
