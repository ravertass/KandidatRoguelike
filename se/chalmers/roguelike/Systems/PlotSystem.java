package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import se.chalmers.plotgen.ImpossibleActionException;
import se.chalmers.plotgen.PlotEngine;
import se.chalmers.plotgen.PlotData.Action;
import se.chalmers.plotgen.PlotData.Actor;
import se.chalmers.plotgen.PlotData.Prop;
import se.chalmers.plotgen.PlotData.Scene;
import se.chalmers.plotgen.PlotGraph.PlotEdge;
import se.chalmers.plotgen.PlotGraph.PlotGraph;
import se.chalmers.plotgen.PlotGraph.PlotVertex;
import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.EntityCreator;
import se.chalmers.roguelike.Components.DungeonComponent;
import se.chalmers.roguelike.Components.IComponent;
import se.chalmers.roguelike.Components.Inventory;
import se.chalmers.roguelike.Components.PlotAction;
import se.chalmers.roguelike.Components.PlotLoot;
import se.chalmers.roguelike.Components.Pocketable;
import se.chalmers.roguelike.Components.Sprite;
import se.chalmers.roguelike.World.Dungeon;

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
	private String firstPlotText;
	private Entity lastStar;
	private boolean plotDone;

	public PlotSystem(Engine engine, PlotEngine plotEngine) {
		starsCreated = false;
		scenes = plotEngine.getScenes();
		scenesStars = new HashMap<Scene, Entity>();
		starsScenes = new HashMap<Entity, Scene>();
		this.engine = engine;
		this.plotEngine = plotEngine;
		mainCharacter = plotEngine.getActors().get(plotEngine.getActors().size() - 1);
		firstPlotText = plotEngine.getCurrentNode().getText();
		plotDone = false;
	}

	private void actionsToStars() {
		if (lastStar == null) {
			lastStar = scenesStars.get(mainCharacter.getLocation());
		}
		Action action = plotEngine.getPossibleAction();
		Entity star = null;

		if (action.getActionType() == Action.ActionType.VISIT) {
			star = scenesStars.get(action.getObjectScene());
		}

		if (action.getActionType() == Action.ActionType.MEET) {
			star = lastStar;
			//			if (action.getSubjectActor() == mainCharacter) {
			//				star = scenesStars.get(action.getObjectActor().getLocation());
			//			} else {
			//				star = scenesStars.get(action.getSubjectActor().getLocation());
			//			}
		}

		if (action.getActionType() == Action.ActionType.GIVE) {
			star = lastStar;
			//			star = scenesStars.get(action.getObjectActor().getLocation());
		}

		if (action.getActionType() == Action.ActionType.KILL) {
			star = lastStar;
			//			star = scenesStars.get(action.getObjectActor().getLocation());
			Dungeon starDungeon = star.getComponent(DungeonComponent.class).getDungeon();

			if (starDungeon != null) {
				starDungeon.addBoss(action.getObjectActor());
			}
		}

		if (action.getActionType() == Action.ActionType.TAKE) {
			star = lastStar;
			//			star = scenesStars.get(action.getObjectProp().getLocation());
			Dungeon starDungeon = star.getComponent(DungeonComponent.class).getDungeon();

			if (starDungeon != null) {
				starDungeon.addPlotLoot(action.getObjectProp());
			}
		}

		if (action.getActionType() == Action.ActionType.LAST) {
			star = lastStar;
			plotDone = true;
		}

		lastStar = star;

		star.getComponent(PlotAction.class).setActionPerformed(false);
		star.getComponent(PlotAction.class).setAction(action);
		star.getComponent(PlotAction.class).setMainCharacterIsSubject(
				plotEngine.getNextNode().getAction().getSubjectActor().equals(mainCharacter));
		star.getComponent(PlotAction.class).setPlotText(plotEngine.getNextNode().getText());
	}

	private void nextAction(Action action) {
		plotEngine.takeAction();
	}

	/**
	 * Creates all stars, and also the space ship
	 */
	private void setupStars() {
		int radius = 50;

		Random rand = new Random(Engine.seed); // Make seed dependent later

		// TODO doesn't do anything
		// Create the spaceship entity
		engine.entityCreator.createSpaceShip(0, 0);

		int i = 0;
		for (Scene scene : scenes) {
			double rad = i * Math.PI / 180;
			int x = (int) (radius * Math.cos(rad) + 400);
			int y = (int) (radius * Math.sin(rad) + 400);

			boolean isFirstStar = (mainCharacter.getLocation() == scene);

			Entity star = engine.entityCreator.createStar(x, y, rand.nextLong(), (scene + " Star"),
					isFirstStar);
			scenesStars.put(scene, star);
			starsScenes.put(star, scene);
			radius += 10;
			i += 45;
		}
	}

	@Override
	public void update() {
		if (plotDone) {
			lastStar.getComponent(PlotAction.class).setActionPerformed(false);
			lastStar.getComponent(PlotAction.class).setAction(null);
			lastStar.getComponent(PlotAction.class).setPlotText(null);
			return;
		}

		if (!starsCreated) {
			setupStars();
			actionsToStars();
			starsCreated = true;
		}

		// TODO: Sjukt ineffektivt sätt att göra detta på... Lös!
		ArrayList<Scene> scenes = new ArrayList<Scene>(scenesStars.keySet());

		for (Scene scene : scenes) {
			PlotAction plotAction = scenesStars.get(scene).getComponent(PlotAction.class);
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

	public String getFirstPlotText() {
		return firstPlotText;
	}

	/*
	 * private void testPlot() { mainCharacter = new Actor("MainChar");
	 * mainCharacter.setLocation(scenes.get(0));
	 * 
	 * secondCharacter = new Actor("SecondChar");
	 * secondCharacter.setLocation(scenes.get(2));
	 * 
	 * plotItem = new Prop("PlotItem"); //plotItem.setLocation(scenes.get(0));
	 * plotItem.setOwner(mainCharacter);
	 * 
	 * plotGraph = new PlotGraph();
	 * 
	 * PlotVertex rootVertex = new PlotVertex("Första rutan!");
	 * plotGraph.addRootVertex(rootVertex);
	 * 
	 * PlotEdge firstEdge = new PlotEdge(new Action(Action.ActionType.VISIT,
	 * mainCharacter, scenes.get(1)));
	 * 
	 * PlotVertex secondVertex = new PlotVertex(
	 * "Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann! Du vann!"
	 * ); plotGraph.addVertex(rootVertex, secondVertex, firstEdge);
	 * 
	 * PlotEdge secondEdge = new PlotEdge(new Action(Action.ActionType.VISIT,
	 * mainCharacter, scenes.get(2)));
	 * 
	 * PlotVertex thirdVertex = new PlotVertex(
	 * "Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen! Igen!"
	 * ); plotGraph.addVertex(secondVertex, thirdVertex, secondEdge);
	 * 
	 * PlotEdge thirdEdge = new PlotEdge(new Action(Action.ActionType.MEET,
	 * mainCharacter, secondCharacter));
	 * 
	 * PlotVertex fourthVertex = new PlotVertex(
	 * "You met this guy, You met this guy, You met this guy, You met this guy, You met this guy, You met this guy, You met this guy!"
	 * ); plotGraph.addVertex(thirdVertex, fourthVertex, thirdEdge);
	 * 
	 * PlotEdge fourthEdgeB = new PlotEdge(new Action(Action.ActionType.GIVE,
	 * secondCharacter, mainCharacter, plotItem));
	 * 
	 * PlotVertex fifthVertexB = new PlotVertex("You gave something away");
	 * plotGraph.addVertex(fourthVertex, fifthVertexB, fourthEdgeB);
	 * 
	 * //TODO fedt testigt objectProp = plotItem;
	 * 
	 * // PlotEdge fourthEdge = new PlotEdge(new Action(Action.ActionType.KILL,
	 * mainCharacter, secondCharacter));
	 * 
	 * // PlotVertex fifthVertex = new PlotVertex("A boss!"); //
	 * plotGraph.addVertex(fourthVertex, fifthVertex, fourthEdge);
	 * 
	 * // PlotEdge fifthEdge = new PlotEdge(new Action(Action.ActionType.TAKE,
	 * mainCharacter, plotItem));
	 * 
	 * // PlotVertex sixthVertex = new PlotVertex("A plot item!"); //
	 * plotGraph.addVertex(fifthVertex, sixthVertex, fifthEdge); }
	 */
}
