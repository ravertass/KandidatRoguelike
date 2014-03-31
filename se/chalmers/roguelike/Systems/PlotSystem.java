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
import se.chalmers.roguelike.Components.PlotAction;

//TODO: Kanske ska man ha en entitet som är "OverworldMainCharacter", som bland
// annat håller koll på om det aktuella uppdraget är klarat eller ej...?
// Fast det känns nog fortfarande oklart hur system interagerar:
// Visst får de väl inte ha entiteter utanför deras vanliga entitetssort?

public class PlotSystem implements ISystem {

	private Engine engine;
	private PlotEngine plotEngine;
	private ArrayList<Scene> scenes;
	private Actor mainCharacter;

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
			if (edge.getAction().getActionType() == Action.ActionType.VISIT) {
				Entity star = scenesStars
						.get(edge.getAction().getObjectScene());
				star.getComponent(PlotAction.class).setAction(edge.getAction());
				star.getComponent(PlotAction.class)
						.setPlotText(
								plotGraph.getAdjacentVertices().get(edge)
										.getPlotText());
			}
		}
	}

	private void nextAction(Action action) {
		ArrayList<PlotEdge> edges = new ArrayList<PlotEdge>();
		edges.addAll(plotGraph.getAdjacentVertices().keySet());
		PlotVertex nextVertex = plotGraph.getAdjacentVertices().get(
				new PlotEdge(action));
		plotGraph.setActiveVertex(nextVertex);
	}

	private void setupStars() {
		int radius = 50;

		Random rand = new Random(1235L); // Make seed depenendant later

		int i = 0;
		for (Scene scene : scenes) {
			double rad = i * Math.PI / 180;
			int x = (int) (radius * Math.cos(rad) + 400);
			int y = (int) (radius * Math.sin(rad) + 400);
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
				actionsToStars();
				plotAction.setActionPerformed(false);
				plotAction.setAction(null);
				plotAction.setPlotText(null);
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
	}
}
