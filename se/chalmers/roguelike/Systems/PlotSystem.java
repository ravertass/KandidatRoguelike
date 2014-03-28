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
	}

	private void setupStars() {
		int radius = 50;

		Random rand = new Random(1234L); // Make seed depenendant later

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
			starsCreated = true;
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

		PlotVertex secondVertex = new PlotVertex("Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann!");
		plotGraph.addVertex(rootVertex, secondVertex, firstEdge);
	}

	public boolean visitAction(Entity star) {
		Action action = new Action(Action.ActionType.VISIT, mainCharacter,
				starsScenes.get(star));
		ArrayList<PlotEdge> edges = new ArrayList<PlotEdge>();
		HashMap<PlotEdge, PlotVertex> adjacencies = plotGraph.getAdjacentVertices();
		edges.addAll(adjacencies.keySet());
		for (PlotEdge edge : edges) {
			if (edge.getAction().equals(action)) {
				plotGraph.setActiveVertex(adjacencies.get(edge));
				// TODO: Det här är stället som gäller!
				System.out.println(plotGraph.getActiveVertex().getPlotText());
				return true;
			}
		}
		return false;
	}
	
	public String getActiveString() {
		return plotGraph.getActiveVertex().getPlotText();
	}
}
