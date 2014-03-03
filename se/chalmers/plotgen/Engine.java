package se.chalmers.plotgen;

import java.util.ArrayList;
import java.util.Set;

import se.chalmers.plotgen.NameGen.NameGenerator;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotEdge;
import se.chalmers.plotgen.PlotGraph.PlotGraph;
import se.chalmers.plotgen.PlotGraph.PlotVertex;

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

public class Engine {

	// TODO: Satte antalet plot-grejer (Scenes, Actors, Props)
	// till 3 som standard, för testning (hårdkodade algoritmen för plotgen
	// utgår
	// ifrån siffran 3)
	private static final int noOfPlotThings = 3;

	private NameGenerator nameGen;

	private ArrayList<Actor> actors;
	private ArrayList<Prop> props;
	private ArrayList<Scene> scenes;
	private PlotGraph plotGraph;

	/**
	 * Initializes everything and randomizes new actors, props and a new plot.
	 * 
	 * @param seed
	 */
	private void run(int seed) {
		nameGen = new NameGenerator(3); // TODO: Satte order på nameGen till 3
										// som standard, för testning

		generateScenes(seed);
		generateActors(seed);
		generateProps(seed);

		generatePlot(seed);
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
	public PlotGraph getPlotGraph() {
		return plotGraph;
	}

	/**
	 * TODO: Will randomize new actors. Will use the name generator for names.
	 * 
	 * @param seed
	 * @return
	 */
	private void generateActors(int seed) {
		actors = new ArrayList<Actor>();

		for (int i = 0; i < noOfPlotThings; i++) {
			Actor actor = new Actor(nameGen.generateName());
			actors.add(actor);
		}
	}

	/**
	 * TODO Will randomize new props. Will use the name generator, to an extent,
	 * for names.
	 * 
	 * @param seed
	 * @return
	 */
	private void generateProps(int seed) {
		props = new ArrayList<Prop>();

		for (int i = 0; i < noOfPlotThings; i++) {
			Prop prop = new Prop(nameGen.generateName());
			props.add(prop);
		}
	}

	/**
	 * TODO Will randomize new props. Will use the name generator, to an extent,
	 * for names.
	 * 
	 * @param seed
	 * @return
	 */
	private void generateScenes(int seed) {
		scenes = new ArrayList<Scene>();

		for (int i = 0; i < noOfPlotThings; i++) {
			Scene scene = new Scene(nameGen.generateName());
			scenes.add(scene);
		}
	}

	/**
	 * TODO: Randomizes a plot. This is where the action is!
	 * 
	 * @param seed
	 * @param actors
	 * @param props
	 * @return
	 */
	private void generatePlot(int seed) {
		plotGraph = PlotGenerator.hardCodedAlgorithm(scenes, actors, props);
	}

	/**
	 * @return all the plotedges outgoing from the active vertex
	 */
	public Set<PlotEdge> getPossibleActions() {
		return plotGraph.getAdjacentVertices().keySet();
	}

	public PlotVertex getActiveVertex() {
		return plotGraph.getActiveVertex();
	}

	/**
	 * Sets the new active vertex accorting to the given edge.
	 * 
	 * @param plotEdge
	 *            the edge that contains the action to take
	 * @return the plot-text for the new active vertex
	 * @throws ImpossibleActionException
	 *             if the given PlotEdge-action isn't possible
	 */
	public String takeAction(PlotEdge plotEdge)
			throws ImpossibleActionException {
		if (!plotGraph.setActiveVertex(plotGraph.getAdjacentVertices().get(
				plotEdge))) {
			throw new ImpossibleActionException("Not a possible action: "
					+ plotEdge);
		}
		return plotGraph.getActiveVertex().getPlotText();
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
		int seed;

		if (args.length > 0) {
			seed = Integer.parseInt(args[0]);
		} else {
			seed = 0; // TODO: Randomize seed
		}

		new Engine(seed);
	}

	public Engine(int seed) {
		run(seed);
	}
}
