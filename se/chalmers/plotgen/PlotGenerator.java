package se.chalmers.plotgen;

import java.util.ArrayList;

import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotEdge;
import se.chalmers.plotgen.PlotGraph.PlotGraph;
import se.chalmers.plotgen.PlotGraph.PlotVertex;

public class PlotGenerator {

	// Notice that this should also alter the state of the Props, Actors and
	// Scenes;
	// Props will be given locations/owners and Actors will be given locations.
	public static PlotGraph hardCodedAlgorithm(ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props) {
		PlotGraph plotGraph = new PlotGraph();

		// A hardcoded plot

		// Add actors to scenes
		for (int i = 0; i < 3; i++) {
			actors.get(i).setLocation(scenes.get(i));
		}

		// Add props to actors and scenes
		props.get(0).setLocation(scenes.get(2));
		props.get(1).setOwner(actors.get(1));
		props.get(2).setOwner(actors.get(2));

		PlotVertex startVertex = new PlotVertex(
				"Once upon a time, there was a young lad named "
						+ actors.get(0) + ".");
		plotGraph.addRootVertex(startVertex);

		// The hero departs from home to visit the sage
		Action depart = new Action(Action.ActionType.VISIT, actors.get(0),
				actors.get(1));
		PlotEdge departEdge = new PlotEdge(depart);
		PlotVertex departVertex = new PlotVertex("Our hero " + actors.get(0)
				+ " departs from " + actors.get(0).getLocation() + " to visit " + 
				actors.get(1) + ", the sage of the " + actors.get(1).getLocation() 
				+ " planet.");
		plotGraph.addVertex(startVertex, departVertex, departEdge);

		// The hero gets a medallion from the sage
		Action gift = new Action(Action.ActionType.GIVE, actors.get(1),
				actors.get(0), props.get(1));
		PlotEdge giftEdge = new PlotEdge(gift);
		PlotVertex giftVertex = new PlotVertex("The young " + actors.get(0)
				+ " got the " + props.get(1) + " medallion from " + actors.get(1) + ".");
		plotGraph.addVertex(departVertex, giftVertex, giftEdge);
		
		// The hero visits the evil boss
		Action boss = new Action(Action.ActionType.VISIT, actors.get(0),
				actors.get(2));
		PlotEdge bossEdge = new PlotEdge(boss);
		PlotVertex bossVertex = new PlotVertex(actors.get(0) + 
				" meets up with the evil " + actors.get(2) + " of planet " 
				+ actors.get(2).getLocation() + ". " + actors.get(2) 
				+ " carries the magic sword of " + props.get(2) + ".");
		plotGraph.addVertex(giftVertex, bossVertex, bossEdge);

		// The hero gives the medallion to the evil boss...
		Action trade1 = new Action(Action.ActionType.GIVE, actors.get(0),
				actors.get(2), props.get(1));
		PlotEdge trade1Edge = new PlotEdge(trade1);
		PlotVertex trade1Vertex = new PlotVertex(actors.get(0) + " makes a deal with evil " 
				+ actors.get(2) + ", and trades him the " + props.get(1) + " medallion for...");
		plotGraph.addVertex(bossVertex, trade1Vertex, trade1Edge);
		
		// ... and gets the magic sword in return...
		Action trade2 = new Action(Action.ActionType.GIVE, actors.get(2),
				actors.get(0), props.get(1));
		PlotEdge trade2Edge = new PlotEdge(trade2);
		PlotVertex trade2Vertex = new PlotVertex("... the magic sword of " + props.get(2) + ".");
		plotGraph.addVertex(trade1Vertex, trade2Vertex, trade2Edge);
		
		// ... or, the hero kills the evil boss...
		Action kill = new Action(Action.ActionType.KILL, actors.get(0),
				actors.get(2));
		PlotEdge killEdge = new PlotEdge(kill);
		PlotVertex killVertex = new PlotVertex(actors.get(0) + " uses a magic spell to kill the evil " 
				+ actors.get(2) + ", and banish him to hell for all eternity.");
		plotGraph.addVertex(bossVertex, killVertex, killEdge);
		
		// ... and takes the magic sword.
		Action take = new Action(Action.ActionType.TAKE, actors.get(0),
				props.get(2));
		PlotEdge takeEdge = new PlotEdge(take);
		PlotVertex takeVertex = new PlotVertex(actors.get(0) 
				+ " takes the magic sword of " + props.get(2) + ".");
		plotGraph.addVertex(killVertex, takeVertex, takeEdge);
		
		// TODO: PlotGraph måste nog hålla koll på tillståndet. PlotGraph får nog helt enkelt
		// bli en Plot-klass (om inte Plot-klassen innehåller PlotGraphen, antar jag). Att hålla
		// koll på tillståndet lär ju dock inte bli svårt, baserat på kanterna.
		
		return plotGraph;
	}
}
