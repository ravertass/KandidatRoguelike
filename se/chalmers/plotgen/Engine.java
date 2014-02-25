package se.chalmers.plotgen;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotGraph.PlotEdge;
import se.chalmers.plotgen.PlotGraph.PlotVertex;
import se.chalmers.plotgen.util.DirectedGraph;

/**
 * This is the main class for the plot generator.
 * 
 * TODO: Jag är osäker på om allt nedan kommer att stämma.
 * 
 * The idea is that it will be possible to initialize it using its main function,
 * independently of its game. Then it will return some randomize plot data via STDOUT.
 * 
 * When using it in the game, it should not only be able to generate plots, but also
 * be able to keep track of the plot state.
 * 
 * @author fabian
 */

public class Engine {

	private ArrayList<Actor> actors;
	private ArrayList<Prop> props;
	// TODO: Maybe also scenes? private ArrayList<Scene> scenes;
	private DirectedGraph<PlotVertex, PlotEdge> plotGraph;

	/**
	 * Initializes everything and randomizes new actors, props and a new plot.
	 * 
	 * @param seed
	 */
	private void run(int seed) {
		actors = randomizeActors(seed);
		props = randomizeProps(seed);

		plotGraph = randomizePlot(seed, actors, props);
	}

	/**
	 * Will randomize new actors. Will use the name generator for names.
	 * 
	 * @param seed
	 * @return
	 */
	private ArrayList<Actor> randomizeActors(int seed) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Will randomize new props. Will use the name generator, to an extent, for
	 * names.
	 * 
	 * @param seed
	 * @return
	 */
	private ArrayList<Prop> randomizeProps(int seed) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Randomizes a plot. This is where the action is!
	 * 
	 * @param seed
	 * @param actors
	 * @param props
	 * @return
	 */
	private DirectedGraph<PlotVertex, PlotEdge> randomizePlot(int seed,
			ArrayList<Actor> actors, ArrayList<Prop> props) {
		// TODO Auto-generated method stub
		return null;
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

		new Engine().run(seed);
	}
}
