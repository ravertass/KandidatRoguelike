package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import se.chalmers.plotgen.PlotEngine;
import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotEdge;
import se.chalmers.plotgen.PlotGraph.PlotGraph;
import se.chalmers.plotgen.PlotGraph.PlotVertex;
import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Attribute;
import se.chalmers.roguelike.Components.BlocksWalking;
import se.chalmers.roguelike.Components.Direction;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.FieldOfView;
import se.chalmers.roguelike.Components.Health;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.PlotAction;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.Components.Weapon;
import se.chalmers.roguelike.Components.Attribute.SpaceClass;
import se.chalmers.roguelike.Components.Attribute.SpaceRace;
import se.chalmers.roguelike.Components.Weapon.TargetingSystem;
import se.chalmers.roguelike.World.Dungeon;

//TODO: Kanske ska man ha en entitet som är "OverworldMainCharacter", som bland
// annat håller koll på om det aktuella uppdraget är klarat eller ej...?
// Fast det känns nog fortfarande oklart hur system interagerar:
// Visst får de väl inte ha entiteter utanför deras vanliga entitetssort?

public class PlotSystem implements ISystem {

	private Engine engine;
	private PlotEngine plotEngine;
	private ArrayList<Scene> scenes;
	private Actor mainCharacter;
	private Actor secondCharacter;

	// You could use a bi-directional map for this, but since there is none
	// in the standard Java API, I won't.
	private HashMap<Scene, Entity> scenesStars;
	private HashMap<Entity, Scene> starsScenes;

	private boolean starsCreated;

	private PlotGraph plotGraph;

	public PlotSystem(Engine engine, PlotEngine plotEngine) {
		starsCreated = false;
		scenes = plotEngine.getScenes();
		scenesStars = new HashMap<Scene, Entity>();
		starsScenes = new HashMap<Entity, Scene>();
		this.engine = engine;
		this.plotEngine = plotEngine;
		testPlot();
		// I vanliga fall är all plot kopplad till stjärnor
		// Men, det finns ett gränsfall: Allra första plot-texten
		// Lämpligtvis skriver man en metod i PlotSystem som returnerar den,
		// sen skickar man med den med OverworldSystems konstruktor och
		// skriver ut den det första man gör.
	}

	private void actionsToStars() {
		ArrayList<PlotEdge> edges = new ArrayList<PlotEdge>();
		edges.addAll(plotGraph.getAdjacentVertices().keySet());
		for (PlotEdge edge : edges) {
			Entity star = null;
			if (edge.getAction().getActionType() == Action.ActionType.VISIT) {
				star = scenesStars.get(edge.getAction().getObjectScene());
			}

			if (edge.getAction().getActionType() == Action.ActionType.MEET) {
				star = scenesStars.get(edge.getAction().getObjectActor()
						.getLocation());
			}

			if (edge.getAction().getActionType() == Action.ActionType.KILL) {
				star = scenesStars.get(edge.getAction().getObjectActor()
						.getLocation());
				Dungeon starDungeon = star.getComponent(DungeonComponent.class)
						.getDungeon();
				if (starDungeon == null) {
					// Lägg till flaggan som säger att plotThingen ska placeras
					// ut när
					// dungeon har genererats
				} else {
					Entity boss = generateBoss(edge.getAction()
							.getObjectActor(), starDungeon.getPlotThingX(),
							starDungeon.getPlotThingY());
					starDungeon.addPlotThing(boss);
				}
			}
			star.getComponent(PlotAction.class).setActionPerformed(false);
			star.getComponent(PlotAction.class).setAction(edge.getAction());
			star.getComponent(PlotAction.class).setPlotText(
					plotGraph.getAdjacentVertices().get(edge).getPlotText());
		}
	}

	private Entity generateBoss(Actor actor, int x, int y) {
		ArrayList<IComponent> components = new ArrayList<IComponent>();

		String name = actor.toString();
		String sprite = "mobs/mob_smurf";
		components.add(new Health(20));
		components.add(new TurnsLeft(1));
		components.add(new Input());
		components.add(new Sprite(sprite));
		components.add(new Inventory()); // TODO add items that the
											// enemy is carrying here,
											// arraylist<entity> inside
											// constructor
		components.add(new Position(x, y));
		components.add(new Direction());
		components.add(new AI());
		Attribute attribute = new Attribute(name, SpaceClass.SPACE_ROGUE,
				SpaceRace.SPACE_DWARF, 1, 50);
		components.add(new BlocksWalking(true));
		components
				.add(new Weapon(2, 6, 0, TargetingSystem.SINGLE_TARGET, 1, 1)); // hardcoded
																				// equals
																				// bad
		components.add(new FieldOfView(8)); // hardcoded equals bad
		components.add(attribute);
		return EntityCreator.createEntity("(Boss)" + name, components);

	}

	private void nextAction(Action action) {
		ArrayList<PlotEdge> edges = new ArrayList<PlotEdge>();
		edges.addAll(plotGraph.getAdjacentVertices().keySet());
		PlotVertex nextVertex = plotGraph.getAdjacentVertices().get(
				new PlotEdge(action));
		plotGraph.setActiveVertex(nextVertex);
	}

	/**
	 * Creates all stars, and also the space ship
	 */
	private void setupStars() {
		int radius = 50;

		Random rand = new Random(1235L); // Make seed dependent later

		int i = 0;
		for (Scene scene : scenes) {
			double rad = i * Math.PI / 180;
			int x = (int) (radius * Math.cos(rad) + 400);
			int y = (int) (radius * Math.sin(rad) + 400);

			// Create the spaceship entity
			engine.entityCreator.createSpaceShip(x, y);

			Entity star = engine.entityCreator.createStar(x, y,
					rand.nextLong(), (scene + " Star"));
			scenesStars.put(scene, star);
			starsScenes.put(star, scene);
			radius += 10;
			i += 45;
		}
	}

	@Override
	public void update() {
		if (!starsCreated) {
			setupStars();
			actionsToStars();
			starsCreated = true;
		}

		// TODO: Sjukt ineffektivt sätt att göra detta på... Lös!
		ArrayList<Scene> scenes = new ArrayList<Scene>();
		scenes.addAll(scenesStars.keySet());

		for (Scene scene : scenes) {
			PlotAction plotAction = scenesStars.get(scene).getComponent(
					PlotAction.class);
			if (plotAction.getActionPerformed()) {
				nextAction(plotAction.getAction());
				plotAction.setActionPerformed(false);
				plotAction.setAction(null);
				plotAction.setPlotText(null);
				actionsToStars();
			}
		}
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub
		// Osäkert om denna behövs?
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub
		// Osäkert om denna behövs?
	}

	private void testPlot() {
		mainCharacter = new Actor("MainChar");
		mainCharacter.setLocation(scenes.get(0));

		secondCharacter = new Actor("SecondChar");
		secondCharacter.setLocation(scenes.get(2));

		plotGraph = new PlotGraph();

		PlotVertex rootVertex = new PlotVertex("Första rutan!");
		plotGraph.addRootVertex(rootVertex);

		PlotEdge firstEdge = new PlotEdge(new Action(Action.ActionType.VISIT,
				mainCharacter, scenes.get(1)));

		PlotVertex secondVertex = new PlotVertex(
				"Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann!");
		plotGraph.addVertex(rootVertex, secondVertex, firstEdge);

		PlotEdge secondEdge = new PlotEdge(new Action(Action.ActionType.VISIT,
				mainCharacter, scenes.get(2)));

		PlotVertex thirdVertex = new PlotVertex(
				"Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen!");
		plotGraph.addVertex(secondVertex, thirdVertex, secondEdge);

		PlotEdge thirdEdge = new PlotEdge(new Action(Action.ActionType.MEET,
				mainCharacter, secondCharacter));

		PlotVertex fourthVertex = new PlotVertex(
				"You met this guy, You met this guy, You met this guy, You met this guy, You met this guy, You met this guy, You met this guy!");
		plotGraph.addVertex(thirdVertex, fourthVertex, thirdEdge);
		
		PlotEdge fourthEdge = new PlotEdge(new Action(Action.ActionType.KILL,
				mainCharacter, secondCharacter));

		PlotVertex fifthVertex = new PlotVertex(
				"A boss!");
		plotGraph.addVertex(fourthVertex, fifthVertex, fourthEdge);
	}
}
