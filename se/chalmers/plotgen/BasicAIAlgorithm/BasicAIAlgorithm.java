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
// - Implementera meetOperator, samt döp om VISIT-actionen till meet [check]
// - Lägg in en action i Operator [check]
// - Skriv färdigt algoritmen nedan: Gör så att grafer kan genereras [check]
// - Kolla så att klassen som kan pränta ut grafer är fristående från andra test [check]
// - Implementera en equals-metod för conditions [check]
// - Gör tvåaktörstestet [check]
// - Se till att conditions kan printas, kanske?
// - Skapa karaktärers mål på ett vettigt vis [fungerar ej!]
// - Se till att algoritmen fungerar med generell indata
// - Gör så att de noder som genereras är lite vettigare
// - Fundera på hur död ska gå till
// - Lös olösbara problem? (typ kasta ut dem och starta om)

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
		setAgentGoals(agents, scenes, actors, props);
		// Create all operators
		HashMap<Agent, ArrayList<Operator>> operators = createOperators(agents,
				scenes, actors, props);

		// While no agent's goal is met
		while (!mainAgent.goalsMet() & mainAgent.getSelf().isAlive()) {

			// Check if agents are alive; if one is not, remove the agent
			ArrayList<Agent> agentsToRemove = new ArrayList<Agent>();
			for (Agent agent : agents) {
				if (!agent.getSelf().isAlive()) {
					agentsToRemove.add(agent);
				}
			}
			for (Agent agent : agentsToRemove) {
				agents.remove(agent);
			}

			// Choose operators
			HashMap<Agent, Operator> chosenOps = chooseOpsAlg(agents, operators);

			for (Agent agent : agents) {
				Operator op = chosenOps.get(agent);
				// If the conditions are still met (since another actor can
				// have done something this iteration that makes the chosen
				// operator undoable)
				if (checkConditions(op.getBeTrue(), op.getBeFalse())) {
					// Perform all the operations
					for (ICondition cond : op.getSetTrue()) {
						cond.set(true);
					}
					for (ICondition cond : op.getSetFalse()) {
						cond.set(false);
					}

					if (agent == mainAgent) {
						// Add an edge and a vertex to the plot graph
						// corresponding to the
						// main agent's operator
						PlotVertex newVertex = new PlotVertex("");
						PlotEdge newEdge = new PlotEdge(chosenOps
								.get(mainAgent).getAction());

						plotGraph.addVertex(lastVertex, newVertex, newEdge);
						lastVertex = newVertex;
					}
				}
			}
		}

		return plotGraph;
	}

	// This is the algorithm that determines which operators the agents
	// will try this iteration
	private static HashMap<Agent, Operator> chooseOpsAlg(
			ArrayList<Agent> agents, HashMap<Agent, ArrayList<Operator>> ops) {

		HashMap<Agent, Operator> chosenOps = new HashMap<Agent, Operator>();

		for (Agent agent : agents) {
			ArrayList<Operator> bestOps = new ArrayList<Operator>();
			int bestNoOfConds = 0;

			for (Operator op : ops.get(agent)) {
				int i = -1;
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

					// If this operator is better than all eariler operators,
					// all earlier operators will be thrown out
					if (i > bestNoOfConds) {
						bestNoOfConds = i;
						bestOps.removeAll(bestOps);
					}
					// If the operator is among the best operators, it will be
					// added
					if (i == bestNoOfConds) {
						bestOps.add(op);
					}
				}
			}

			Operator chosenOp;
			if (bestOps.size() > 0) {
				chosenOp = bestOps.get(new Random().nextInt(bestOps.size()));
			} else {
				// TODO: Should the program ever be able to go here?
				chosenOp = null;
			}
			chosenOps.put(agent, chosenOp);
		}

		return chosenOps;
	}

	// Defines all the operators for each agent
	private static HashMap<Agent, ArrayList<Operator>> createOperators(
			ArrayList<Agent> agents, ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props) {
		HashMap<Agent, ArrayList<Operator>> operators = new HashMap<Agent, ArrayList<Operator>>();

		for (Agent agent : agents) {
			ArrayList<Operator> agentsOperators = new ArrayList<Operator>();

			Actor self = agent.getSelf();

			for (Actor actor : actors) {
				if (actor == self) {
					continue;
				}
				agentsOperators.add(Operators.killOperator(self, actor));
				agentsOperators.add(Operators.meetOperator(self, actor));

				for (Prop prop : props) {
					agentsOperators.add(Operators.giveOperator(self, actor,
							prop));
				}
			}

			for (Prop prop : props) {
				agentsOperators.add(Operators.takeOperator(self, prop));
			}

			for (Scene targetScene : scenes) {
				agentsOperators.add(Operators.visitOperator(self, targetScene));
			}

			operators.put(agent, agentsOperators);
		}

		return operators;
	}

	private static ArrayList<Agent> createAgents(ArrayList<Actor> actors) {
		ArrayList<Agent> agents = new ArrayList<Agent>();

		for (Actor actor : actors) {
			Agent agent = new Agent(actor);
			agents.add(agent);
		}

		return agents;
	}

	// TODO: I don't really know yet how to in a smart way generate the agents'
	// goals.
	private static void setAgentGoals(ArrayList<Agent> agents,
			ArrayList<Scene> scenes, ArrayList<Actor> actors,
			ArrayList<Prop> props) {
		for (Agent agent : agents) {
			// TODO: Slumpa fram ett mål
			// Two basic types of goal:
			// BelongsToCondition(random-prop*, self) *that doesn't belong to
			// self
			// !Lives(random-actor*) *not self

			ICondition goal;
			ArrayList<ICondition> trueGoals = new ArrayList<ICondition>();
			ArrayList<ICondition> falseGoals = new ArrayList<ICondition>();

			// Flip a coin to see which condition will be the goal
			int conditionCoinFlip = new Random().nextInt(2);
			System.out.println(conditionCoinFlip);
			if (conditionCoinFlip == 0) {
				Prop propToGet = null;
				// See so the randomized prop to get doesn't belong to the agent
				// itself
				boolean notBelongToSelf = true;
				while (notBelongToSelf) {
					propToGet = props.get(new Random().nextInt(props.size()));
					if (propToGet.getOwner() == agent.getSelf()) {
						notBelongToSelf = true;
					} else {
						notBelongToSelf = false;
					}
				}
				goal = new BelongsToCondition(propToGet, agent.getSelf());
				trueGoals.add(goal);
			} else {
				Actor actorToKill = null;
				// See so the randomized actor to kill isn't the agent itself
				boolean notSelf = true;
				while (notSelf) {
					actorToKill = actors
							.get(new Random().nextInt(actors.size()));
					if (actorToKill == agent.getSelf()) {
						notSelf = true;
					} else {
						notSelf = false;
					}
				}
				goal = new LivesCondition(actorToKill);
				falseGoals.add(goal);
			}

			agent.setTrueGoals(trueGoals);
			agent.setFalseGoals(falseGoals);
		}
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
