package se.chalmers.plotgen;

import java.util.ArrayList;
import java.util.Random;

import se.chalmers.plotgen.NameGen.NameGenerator;
import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotLine.PlotLine;
import se.chalmers.plotgen.PlotLine.PlotNode;
import se.chalmers.plotgen.PlotLine.PlotTextGenerator;

/**
 * This is the main class for the plot generator.
 * 
 * TODO: Jag är osäker på om allt nedan kommer att stämma.
 * 
 * The idea is that it will be possible to initialize it using its main
 * function, independently of its game. Then it will return some randomize plot
 * data via STDOUT.
 * 
 * When using it in the game, it should not only be able to generate plots, but
 * also be able to keep track of the plot state.
 * 
 * @author fabian
 */

public class PlotEngine {

	private NameGenerator actorNameGen;
	private NameGenerator propNameGen;
	private NameGenerator sceneNameGen;

	private ArrayList<Actor> actors;
	private ArrayList<Prop> props;
	private ArrayList<Scene> scenes;
	private PlotLine plotLine;

	/**
	 * Initializes everything and randomizes new actors, props and a new plot.
	 * 
	 * @param seed
	 */
	private void run(long seed) {
		actorNameGen = new NameGenerator(4, seed);
		propNameGen = new NameGenerator(2, seed);
		propNameGen.loadFile("thingnames");
		sceneNameGen = new NameGenerator(3, seed);
		sceneNameGen.loadFile("starnames");

		Random random = new Random(seed);
		
		generateScenes(random);
		generateActors(random);
		generateProps(random);

		generatePlot(random);
		plotLine = new PlotTextGenerator(plotLine, actors.get(actors.size()-1)).generatePlotText();
		printPlot();
	}
	
	public void printPlot() {
		System.out.println(plotLine);
	}

	public ArrayList<Scene> getScenes() {
		return scenes;
	}

	public ArrayList<Actor> getActors() {
		return actors;
	}

	public ArrayList<Prop> getProps() {
		return props;
	}

	/**
	 * TODO: I'm not sure if you should be able to get this like this.
	 * 
	 * @return
	 */
	public PlotLine getPlotLine() {
		return plotLine;
	}

	/**
	 * TODO: Will randomize new actors. Will use the name generator for names.
	 * 
	 * @param random
	 * @return
	 */
	private void generateActors(Random random) {
		actors = new ArrayList<Actor>();

		// This way, we will get 3-5 actors
		int noOfActors = random.nextInt(3) + 3;
		
		for (int i = 0; i < noOfActors; i++) {
			Actor actor = new Actor(actorNameGen.generateName(), random.nextInt(6));
			actors.add(actor);
		}
	}

	/**
	 * TODO Will randomize new props. Will use the name generator, to an extent,
	 * for names.
	 * 
	 * @param random
	 * @return
	 */
	private void generateProps(Random random) {
		props = new ArrayList<Prop>();

		// This way, we will get 5-10 props
		int noOfProps = random.nextInt(6) + 5;
		
		for (int i = 0; i < noOfProps; i++) {
			Prop prop = new Prop(propNameGen.generateName(), random.nextInt(5));
			props.add(prop);
		}
	}

	/**
	 * TODO Will randomize new props. Will use the name generator, to an extent,
	 * for names.
	 * 
	 * @param random
	 * @return
	 */
	private void generateScenes(Random random) {
		scenes = new ArrayList<Scene>();

		// This way, we will get 5-10 scenes
		int noOfScenes = random.nextInt(6) + 5;
		
		for (int i = 0; i < noOfScenes; i++) {
			Scene scene = new Scene(sceneNameGen.generateName(), random.nextInt(10));
			scenes.add(scene);
		}
	}

	/**
	 * TODO: Randomizes a plot. This is where the action is!
	 * 
	 * @param random
	 * @param actors
	 * @param props
	 * @return
	 */
	private void generatePlot(Random random) {
		plotLine = PlotGenerator.basicAIAlgorithm(scenes, actors, props, random);
	}

	/**
	 * @return the possible action
	 */
	public Action getPossibleAction() {
		if (plotLine.getNextNode() == null) {
			return null;
		}
		return plotLine.getNextNode().getAction();
	}

	public PlotNode getCurrentNode() {
		return plotLine.getCurrentNode();
	}
	
	public PlotNode getNextNode() {
		return plotLine.getNextNode();
	}

	/**
	 * Advances the plot to the next plot node.
	 * 
	 * @return the plot-text for the new active node
	 */
	public String takeAction() {
		plotLine.incrementCurrentNode();
		return plotLine.getCurrentNode().getText();
	}

	/**
	 * The main method is to be used if you want to run the plot generator
	 * separately from the game.
	 * 
	 * TODO: It will probably give some nice output to STDOUT.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long seed;

		if (args.length > 0) {
			seed = Integer.parseInt(args[0]);
		} else {
			seed = new Random().nextLong();
		}

		new PlotEngine(seed).printPlot();
	}

	public PlotEngine(long seed) {
		run(seed);
	}

	public Actor getMainActor() {
		return actors.get(actors.size()-1);
	}
}
