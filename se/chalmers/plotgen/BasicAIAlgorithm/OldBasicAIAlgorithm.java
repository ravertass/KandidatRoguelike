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
// - Refaktorera, plocka ut metoder som kan vara användbara även för en AdvancedAIAlgorithm
// - Gör så att de noder som genereras är lite vettigare (enkel lösning: gör så de baseras på allas handlingar)
// - Skriv ordentligt om algoritmen
// - Gör seriösa tester på algoritmen, med olika antal SAPs osv.
// - Lös problemet där huvudpersonen önskar en person död, men inte dödar personen själv [check, va?]
// - Gör MediumAIAlgorithm

/**
 * This class takes SAPs as input and creates Agents from the Actors. It then,
 * when using createPlot(), simulates a story where the Agents try to accomplish
 * their goals by using different operators (performing actions). A plot graph is
 * returned.
 */
public class OldBasicAIAlgorithm {

	private ArrayList<Scene> scenes;
	private ArrayList<Actor> actors;
	private ArrayList<Prop> props;

	private ArrayList<Agent> agents;
	private Agent mainAgent;
	private HashMap<Agent, ArrayList<Operator>> operators;

	private Random random;

	public OldBasicAIAlgorithm(ArrayList<Scene> scenes, ArrayList<Actor> actors, ArrayList<Prop> props,
			Random random) {
		this.scenes = scenes;
		this.actors = actors;
		this.props = props;
		this.random = random;
	}

	public PlotGraph createPlot() {

		// Put actors on scenes and props on scenes and actors
		placePlotBodies();

		PlotGraph plotGraph = new PlotGraph();
		// Add the first vertex
		PlotVertex rootVertex = new PlotVertex("This is the story");
		plotGraph.addRootVertex(rootVertex);
		PlotVertex lastVertex = rootVertex;

		// Create all agents
		createAgents();
		// It's a bit arbitrary, but we decide that the last agent in the list
		// is the main character
		mainAgent = agents.get(agents.size() - 1);
		// Create all agents' goals
		setAgentGoals();
		// Create all operators
		createOperators();

		// While the main agent's goals aren't met and the main agent lives
		while (!mainAgent.goalsMet() && mainAgent.getSelf().isAlive()) {

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
			HashMap<Agent, Operator> chosenOps = chooseOpsAlg();

			for (Agent agent : agents) {
				Operator op = chosenOps.get(agent);

				if (op == null) {
					continue;
				}
				// If the conditions are still met (since another actor can
				// have done something this iteration that makes the chosen
				// operator undoable)

				boolean opPerformed = performOp(op, agent);

				// If the main character performed the op
				if ((agent == mainAgent) && opPerformed) {
					// Add an edge and a vertex to the plot graph
					// corresponding to the
					// main agent's operator
					PlotVertex newVertex = new PlotVertex("");
					PlotEdge newEdge = new PlotEdge(chosenOps.get(mainAgent).getAction());

					plotGraph.addVertex(lastVertex, newVertex, newEdge);
					lastVertex = newVertex;
				}

				// If the op was done to the main character
				if (op.getAction().getObjectActor() == mainAgent.getSelf() && opPerformed) {
					// Add an edge and a vertex to the plot graph
					// corresponding to the
					// performed operator
					PlotVertex newVertex = new PlotVertex("");
					PlotEdge newEdge = new PlotEdge(chosenOps.get(agent).getAction());

					plotGraph.addVertex(lastVertex, newVertex, newEdge);
					lastVertex = newVertex;
				}

				// If the plot is too long, we'll try again
				if (plotGraph.size() > 9) {
					return null;
				}
			}
		}

		// If the plot is too short, we'll also try again
		if (plotGraph.size() < 4) {
			return null;
		}

		for (Actor actor : actors) {
			actor.loadSnapShot();
		}
		for (Prop prop : props) {
			prop.loadSnapShot();
		}
		for (Scene scene : scenes) {
			scene.loadSnapShot();
		}
		
		return plotGraph;
	}

	/**
	 * @param op
	 * @param agent
	 * @return if it succeeds to perform the op
	 */
	private boolean performOp(Operator op, Agent agent) {
		if (checkConditions(op.getBeTrue(), op.getBeFalse())) {
			for (ICondition cond : op.getSetTrue()) {
				cond.set(true);
			}
			for (ICondition cond : op.getSetFalse()) {
				cond.set(false);
			}
			return true;
		}

		return false;
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
	private void placePlotBodies() {

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
		
		for (Actor actor : actors) {
			actor.saveSnapShot();
		}
		for (Prop prop : props) {
			prop.saveSnapShot();
		}
		for (Scene scene : scenes) {
			scene.saveSnapShot();
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
	private HashMap<Agent, Operator> chooseOpsAlg() {

		HashMap<Agent, Operator> chosenOps = new HashMap<Agent, Operator>();

		for (Agent agent : agents) {
			ArrayList<Operator> bestOps = new ArrayList<Operator>();
			int bestOpValue = 0;

			for (Operator op : operators.get(agent)) {
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
				chosenOp = randomizeOp(bestOps);
				// Old way to randomize:
				//chosenOp = bestOps.get(random.nextInt(bestOps.size()));
			} else {
				// TODO: Should the program ever be able to go here?
				chosenOp = null;
			}
			chosenOps.put(agent, chosenOp);
		}

		return chosenOps;
	}

	// Different ops should have different weights
	// E.g. it should be less likely that an actor kills than that it moves
	private Operator randomizeOp(ArrayList<Operator> ops) {
		// Compute the total weight of the ops
		// Add the ops to a list, according to its weight
		// (yes, this is not an optimal way to do it, but it works and is easy)
		int totalWeight = 0;
		ArrayList<Operator> weightedOps = new ArrayList<Operator>();
		for (Operator op : ops) {
			totalWeight += op.getWeight();
			for (int i = 0; i < op.getWeight(); i++) {
				weightedOps.add(op);
			}
		}
		//TODO This is so stupid
		// By adding a number of nulls, it is very possible 
		// that an agent determines to do no operation (by using null as operator)
		// This also means that there will be no error when the only possible
		// actions have 0 weight
		int amountOfNulls = 10;
		totalWeight += amountOfNulls;
		for (int i = 0; i < amountOfNulls; i++) {
			weightedOps.add(null);
		}

		Operator randomOp = weightedOps.get(random.nextInt(totalWeight));
		return randomOp;
		/*
		 * // Randomize a number, choose a random op based on the // randomized
		 * number and the ops' weights Operator randomOp = null; int randomInt =
		 * random.nextInt(totalWeight); System.out.println(randomInt); // TODO
		 * This doesn't seem to work for (Operator op : ops) { randomDouble -=
		 * op.getWeight(); if (randomDouble <= 0.0d) { randomOp = op;
		 * System.out.println(randomOp.getAction());
		 * System.out.println(randomDouble);
		 * System.out.println(randomOp.getWeight()); System.out.println("---");
		 * break; } }
		 * 
		 * return randomOp;
		 */
	}

	// Defines all the operators for each agent
	private void createOperators() {
		operators = new HashMap<Agent, ArrayList<Operator>>();

		for (Agent agent : agents) {
			ArrayList<Operator> agentsOperators = new ArrayList<Operator>();

			Actor self = agent.getSelf();

			for (Actor actor : actors) {
				// We don't want the agent to be doing things to herself
				if (actor == self) {
					continue;
				}

				Operator killOp = Operators.killOperator(self, actor);
				// Special case:
				// Set the weight of the killOp to 0 if the object actor
				// is the main character (that is, tha main character can't die)
				if (actor == mainAgent.getSelf()) {
					killOp.setWeight(0);
				}
				// Another special case:
				// Only the mainAgent should be able to kill the
				// character that the mainAgent wants to kill
				//TODO: This doesn't seem to work!
				// 		It seems the program doesn't get into the loop below at all sometimes...
				if (actor != mainAgent.getSelf()) {
					for (ICondition mainGoal : mainAgent.getTrueGoals()) {
						if (killOp.getSetTrue().contains(mainGoal)) {
							killOp.setWeight(0);
						}
					}
				}

				agentsOperators.add(killOp);
				agentsOperators.add(Operators.meetOperator(self, actor));

				for (Prop prop : props) {
					agentsOperators.add(Operators.giveOperator(self, actor, prop));
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
	}

	private void createAgents() {
		agents = new ArrayList<Agent>();

		for (Actor actor : actors) {
			Agent agent = new Agent(actor);
			agents.add(agent);
		}
	}

	// Stochastically sets goals for all the agents
	private void setAgentGoals() {
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

	private boolean checkConditions(ArrayList<ICondition> beTrue, ArrayList<ICondition> beFalse) {
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
