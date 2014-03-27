package se.chalmers.roguelike.Systems;

import se.chalmers.plotgen.PlotEngine;
import se.chalmers.roguelike.Entity;

//TODO: Kanske ska man ha en entitet som är "OverworldMainCharacter", som bland
// annat håller koll på om det aktuella uppdraget är klarat eller ej...?
// Fast det känns nog fortfarande oklart hur system interagerar:
// Visst får de väl inte ha entiteter utanför deras vanliga entitetssort?

public class PlotSystem implements ISystem {

	public PlotSystem(PlotEngine plotEngine) {
		// Flytta createStars hit i sin helhet?
		// Skapa en hashmap mellan Scenes : Entities (stars)
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

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

}
