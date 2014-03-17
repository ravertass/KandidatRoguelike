package se.chalmers.plotgen.BasicAIAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotEdge;
import se.chalmers.plotgen.PlotGraph.PlotGraph;
import se.chalmers.plotgen.PlotGraph.PlotVertex;

// TODO:
// - Se till att algoritmen fungerar med generell indata [check-ish] (verkar fungera, ej övertestat)
// - Gör så att PlotTest fungerar (eller gör en bedömning om det bör fungera eller ej)
// - Refaktorera, plocka ut metoder som kan vara användbara även för en AdvancedAIAlgorithm
// - Se till att NameGen tar en seed, kanske?
// - Gör så att huvudpersonen inte kan dö
// - Gör så att de noder som genereras är lite vettigare (enkel lösning: gör så de baseras på allas handlingar)
// - Bevisa att olösbara problem kan uppstå
// - Lös olösbara problem? (typ kasta ut dem och starta om)
// - Skriv ordentligt om algoritmen
// - Gör seriösa tester på algoritmen, med olika antal SAPs osv.
// - Lös problemet där huvudpersonen önskar en person död, men inte dödar personen själv
// - Se till så att även operatorer där huvudpersonen är objekt översätts till kanter i grafen

public class BasicAIAlgorithm {

	public static PlotGraph createPlot(ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props, Random random) {

		// Put actors on scenes and props on scenes and actors
		placePlotBodies(scenes, actors, props, random);

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
		setAgentGoals(agents, scenes, actors, props, random);
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
			HashMap<Agent, Operator> chosenOps = chooseOpsAlg(agents,
					operators, random);

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

	/**
	 * This method places the actors on scenes and the props on scenes and with
	 * actors. This is done in a stochastic way, using the given random
	 * instance.
	 * 
	 * @param scenes
	 * @param actors
	 * @param props
	 * @param random
	 */
	private static void placePlotBodies(ArrayList<Scene> scenes,
			ArrayList<Actor> actors, ArrayList<Prop> props, Random random) {

		// To be able to remove them when they're placed, the actors and
		// props are placed in queues, implemented as LinkedLists
		LinkedList<Actor> actorsQueue = new LinkedList<Actor>();
		actorsQueue.addAll(actors);
		LinkedList<Prop> propsQueue = new LinkedList<Prop>();
		propsQueue.addAll(props);

		int i = 0;

		// Until we're done with both the actors and the props
		while (!actorsQueue.isEmpty() | !propsQueue.isEmpty()) {
			Scene scene = scenes.get(i);
			// See if we should add an actor to this scene
			// TODO: Kinda magic number, the chance of being put at
			// a scene is 1 / scenes.size()
			if (random.nextInt(scenes.size()) == 0) {
				Actor actor = actorsQueue.poll();
				if (actor != null) {
					actor.setLocation(scene);
					// See if we should add a prop to this actor
					// TODO: Kinda magic number, the chance of being
					// put with an actor is 1/2
					if (random.nextInt(2) == 0) {
						Prop prop = propsQueue.poll();
						if (prop != null) {
							prop.setOwner(actor);
						}
					}
				}
			}

			// See if we should add a prop to this scene
			if (random.nextInt(scenes.size()) == 0) {
				// TODO: Kinda magic number, the chance of being put at
				// a scene is 1 / scenes.size()
				Prop prop = propsQueue.poll();
				if (prop != null) {
					prop.setLocation(scene);
				}
			}

			// Determine the index of the next scene
			// We loop through the same scenes again and again until we're done
			i = (i + 1) % scenes.size();
		}

		/*
		 * TODO Test output System.out.println("Scenes:"); for (Scene scene :
		 * scenes) { System.out.println(scene); } System.out.println("Actors:");
		 * for (Actor actor : actors) { System.out.println(actor + ": " +
		 * actor.getLocation()); } System.out.println("Props:"); for (Prop prop
		 * : props) { System.out.println(prop + ": " + prop.getLocation());
		 * System.out.println(prop + ": " + prop.getOwner()); }
		 * System.out.println("----");
		 */
	}

	// This is the algorithm that determines which operators the agents
	// will try this iteration
	private static HashMap<Agent, Operator> chooseOpsAlg(
			ArrayList<Agent> agents, HashMap<Agent, ArrayList<Operator>> ops,
			Random random) {

		HashMap<Agent, Operator> chosenOps = new HashMap<Agent, Operator>();

		for (Agent agent : agents) {
			ArrayList<Operator> bestOps = new ArrayList<Operator>();
			int bestOpValue = 0;

			for (Operator op : ops.get(agent)) {
				int valueOfOp = -1;
				if (checkConditions(op.getBeTrue(), op.getBeFalse())) {
					valueOfOp = 0;
					for (ICondition cond : op.getSetTrue()) {
						if (agent.getTrueGoals().contains(cond)) {
							valueOfOp++;
						}
					}
					for (ICondition cond : op.getSetFalse()) {
						if (agent.getFalseGoals().contains(cond)) {
							valueOfOp++;
						}
					}

					// If this operator is better than all earlier operators,
					// all earlier operators will be thrown out
					if (valueOfOp > bestOpValue) {
						bestOpValue = valueOfOp;
						// Remove all previously chosen ops
						bestOps = new ArrayList<Operator>();
					}
					// If the operator is among the best operators, it will be
					// added
					if (valueOfOp == bestOpValue) {
						bestOps.add(op);
					}
				}
			}

			Operator chosenOp;
			if (bestOps.size() > 0) {
				chosenOp = bestOps.get(random.nextInt(bestOps.size()));
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
				// We don't want the agent to be doing things to herself
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

	// Stochastically sets goals for all the agents
	private static void setAgentGoals(ArrayList<Agent> agents,
			ArrayList<Scene> scenes, ArrayList<Actor> actors,
			ArrayList<Prop> props, Random random) {
		for (Agent agent : agents) {

			ICondition goal;
			ArrayList<ICondition> trueGoals = new ArrayList<ICondition>();
			ArrayList<ICondition> falseGoals = new ArrayList<ICondition>();

			// Flip a coin to see which condition will be the goal
			// TODO: Kind of a magic number, here we say that the chance to get
			// a killing goal is 1/2 and the chance to get an item retrieval
			// goal is 1/2
			int conditionCoinFlip = random.nextInt(2);
			if (conditionCoinFlip == 0) {
				Prop propToGet = null;
				// See so the randomized prop to get doesn't belong to the agent
				// itself
				boolean notBelongToSelf = true;
				while (notBelongToSelf) {
					propToGet = props.get(random.nextInt(props.size()));
					if (propToGet.getOwner() == agent.getSelf()) {
						notBelongToSelf = true;
					} else {
						notBelongToSelf = false;
					}
				}
				goal = new BelongsToCondition(propToGet, agent.getSelf());
				// TODO Test output
				System.out.println(agent.getSelf() + ": " + goal);
				trueGoals.add(goal);
			} else {
				Actor actorToKill = null;
				// See so the randomized actor to kill isn't the agent itself
				boolean notSelf = true;
				while (notSelf) {
					actorToKill = actors.get(random.nextInt(actors.size()));
					if (actorToKill == agent.getSelf()) {
						notSelf = true;
					} else {
						notSelf = false;
					}
				}
				goal = new LivesCondition(actorToKill);
				// TODO Test output
				System.out.println(agent.getSelf() + ": " + goal);
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
