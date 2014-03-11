package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotEdge;
import se.chalmers.plotgen.PlotGraph.PlotGraph;
import se.chalmers.plotgen.PlotGraph.PlotVertex;

// TODO:
// - Skapa överklassen PlotBody till Prop och Actor [check]
// - Skapa SamePlaceCondition [check]
// - Ändra i operatorernas conditions [check]
// - Gör så att en död actor blir av med plats osv. [check]
// - Implementera createOperators() [check]
// - Implementera chooseOpsAlg() [check]
// - Implementera meetOperator, samt döp om VISIT-actionen till meet
//- Lägg in en action i Operator [check]
// - Implementera en action för att gå till en scen (alltså en VISIT-action, på riktigt)
// - Skriv färdigt algoritmen nedan: Gör så att grafer kan genereras [check]
// - Gör så att de noder som genereras är lite vettigare
// - Skapa karaktärers mål (hårdkoda från början, kanske?)
// - Testa ALLT

public class BasicAIAlgorithm {

	public static PlotGraph algorithm(ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props) {
		PlotGraph plotGraph = new PlotGraph();
		// Add the first vertex
		PlotVertex rootVertex = new PlotVertex("This is a story");
		plotGraph.addRootVertex(rootVertex);
		PlotVertex lastVertex = rootVertex;

		// Create all agents
		ArrayList<Agent> agents = createAgents(actors);
		// It's a bit arbitrary, but we decide that the last agent in the list
		// is the main character
		Agent mainAgent = agents.get(agents.size() - 1);
		// Create all agents' goals
		setAgentGoals(agents);
		// Create all operators
		HashMap<Agent, ArrayList<Operator>> operators = createOperators(agents,
				scenes, actors, props);

		// While no agent's goal is met
		boolean goalsMet = true;
		while (goalsMet) {
			// Choose operators
			HashMap<Agent, Operator> chosenOps = chooseOpsAlg(agents, operators);

			for (Agent agent : agents) {
				// Perform all the operations
				for (ICondition cond : chosenOps.get(agent).setTrue) {
					cond.set(true);
				}
				for (ICondition cond : chosenOps.get(agent).setFalse) {
					cond.set(false);
				}
			}
			// Add an edge and a vertex to the plot graph corresponding to the main agent's operator
			PlotVertex newVertex = new PlotVertex("banana");
			PlotEdge newEdge = new PlotEdge(chosenOps.get(mainAgent).getAction());
			plotGraph.addVertex(lastVertex, newVertex, newEdge);
			lastVertex = newVertex;

			goalsMet = false;
		}

		return plotGraph;
	}

	private static HashMap<Agent, Operator> chooseOpsAlg(
			ArrayList<Agent> agents, HashMap<Agent, ArrayList<Operator>> ops) {

		HashMap<Agent, Operator> chosenOps = new HashMap<Agent, Operator>();

		for (Agent agent : agents) {
			ArrayList<Operator> bestOps = new ArrayList<Operator>();
			int bestNoOfConds = 0;

			for (Operator op : ops.get(agent)) {
				int i = 0;
				if (checkConditions(op.getBeTrue(), op.getBeFalse())) {
					i = 0;
					for (ICondition cond : op.getSetTrue()) {
						if (agent.getTrueGoals().contains(cond)) {
							i++;
						}
					}
					for (ICondition cond : op.getSetFalse()) {
						if (agent.getFalseGoals().contains(cond)) {
							i++;
						}
					}
				}

				if (i >= bestNoOfConds) {
					bestOps.add(op);
					bestNoOfConds = i;
				}
			}

			Operator chosenOp;
			if (bestOps.size() > 0) {
				chosenOp = bestOps.get(new Random().nextInt(bestOps.size()));
			} else {
				chosenOp = null;
			}
			chosenOps.put(agent, chosenOp);
		}

		return chosenOps;
	}

	// This method will be kind of long and very hardcoded. Hurrah!
	private static HashMap<Agent, ArrayList<Operator>> createOperators(
			ArrayList<Agent> agents, ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props) {
		HashMap<Agent, ArrayList<Operator>> operators = new HashMap<Agent, ArrayList<Operator>>();

		for (Agent agent : agents) {
			ArrayList<Operator> agentsOperators = new ArrayList<Operator>();

			Actor self = agent.getSelf();

			int propsDone = 0;

			for (Actor actor : actors) {
				agentsOperators.add(Operators.killOperator(self, actor));

				for (Prop prop : props) {
					agentsOperators.add(Operators.giveOperator(self, actor,
							prop));

					if (propsDone == 0) {
						agentsOperators.add(Operators.takeOperator(self, prop));
					}
				}

				propsDone = 1;
			}

			for (Scene targetScene : scenes) {
				agentsOperators.add(Operators.visitOperator(self, targetScene));
			}

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

	private static boolean checkConditions(ArrayList<ICondition> beTrue,
			ArrayList<ICondition> beFalse) {
		for (ICondition condition : beTrue) {
			if (!condition.get()) {
				return false;
			}
		}
		for (ICondition condition : beFalse) {
			if (condition.get()) {
				return false;
			}
		}
		return true;
	}
}
