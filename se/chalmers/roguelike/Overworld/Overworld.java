package se.chalmers.roguelike.Overworld;

import java.util.ArrayList;

import org.newdawn.slick.geom.Circle;

import se.chalmers.roguelike.World.Dungeon;
import se.chalmers.roguelike.World.Generator;

public class Overworld {
	
	private ArrayList<Dungeon> dungeons;
	
	private ArrayList<Circle> planets;
	
	public Overworld(int numberOfPlanets) {
		
		Dungeon d = new Dungeon();
		for(int i = 0; i < numberOfPlanets; i++) {
			d.setWorld(50, 50, new Generator().toTiles());
			dungeons.add(d);
			planets.add(new Circle((float)Math.random(), (float)Math.random(), 0.1f));
		}
		//TODO ADD MOAR STUFF
	}
	
	public ArrayList<Circle> getPlanets() {
		return this.planets;
	}
	
	public ArrayList<Dungeon> getDungeons() {
		return this.dungeons;
	}
	
}
