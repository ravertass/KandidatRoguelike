package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotGraph;

// TODO:
// - Implementera operatorgeneratorn (usch)
// - Skapa karaktärers mål
// - Implementera checkConditions(beTrue, beFalse)
// - Implementera iterationsalgoritmen
// - Skriv färidigt algoritmen nedan: Gör så att grafer kan genereras

public class BasicAIAlgorithm {

	public static PlotGraph algorithm(ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props) {
		PlotGraph plotGraph = new PlotGraph();

		// Create all agents
		ArrayList<Agent> agents = createAgents(actors);
		// Create all agents' goals
		setAgentGoals(agents);
		// Create all operators
		HashMap<Agent, ArrayList<Operator>> operators = createOperators(agents,
				scenes, actors, props);

		// While no agent's goal is met
		// - Choose operators

		// - For chosen operators:
		// - - In randomized order, translate chosen ops to plotnodes

		return plotGraph;
	}

	// This method will be kind of long and very hardcoded. Hurrah!
	private static HashMap<Agent, ArrayList<Operator>> createOperators(
			ArrayList<Agent> agents, ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props) {
		HashMap<Agent, ArrayList<Operator>> operators = new HashMap<Agent, ArrayList<Operator>>();

		for (Agent agent : agents) {
			ArrayList<Operator> agentsOperators = new ArrayList<Operator>();

			// TODO: Generate all permutations of operators
			// This will be messy as shit
			// Operators.giveOperator(agent.getSelf(), recipient, prop, location)

			operators.put(agent, agentsOperators);
		}

		return null;
	}

	private static ArrayList<Agent> createAgents(ArrayList<Actor> actors) {
		ArrayList<Agent> agents = new ArrayList<Agent>();

		for (Actor actor : actors) {
			Agent agent = new Agent(actor);
		}

		return agents;
	}

	// TODO: I don't really know yet how to in a smart way generate the agents'
	// goals.
	private static void setAgentGoals(ArrayList<Agent> agents) {
		// TODO Auto-generated method stub
	}
}
