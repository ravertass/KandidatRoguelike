package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.Random;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.Dungeon;

public class AISystem implements ISystem {

	/**
	 * A list of entities that has some kind of AI
	 */
	private ArrayList<Entity> entities;
	private AI ai;
	private Random rand;

	public AISystem(){
		entities = new ArrayList<Entity>();
		rand = new Random();
	}
	
	@Override
	public void update() {
		
	}
	
	public void update(Dungeon world){
		for (Entity e : entities){
			//move, attack or sleep?
			ai = e.getComponent(AI.class);
			Entity target = ai.getTarget();
			if (target != null){
				track(target);
			}
			
			Input input = e.getComponent(Input.class);
			
			int x = e.getComponent(Position.class).getX();
			int y = e.getComponent(Position.class).getY();
			boolean done = false;
			if (!(world.getTile(x - 1, y + 1).isWalkable() 
					|| world.getTile(x, y + 1).isWalkable() 
					|| world.getTile(x + 1, y + 1).isWalkable() 
					|| world.getTile(x - 1, y).isWalkable()
					|| world.getTile(x, y - 1).isWalkable()
					|| world.getTile(x + 1, y).isWalkable()
					|| world.getTile(x - 1, y - 1).isWalkable()
					|| world.getTile(x + 1, y - 1).isWalkable())){
				input.setNextEvent(InputAction.DO_NOTHING);
				done = true;
				System.out.println(e + " DID NOTHING!");
			}
			while (!done) {
				int randNr = rand.nextInt(8);
				if(randNr==0 && world.getTile(x - 1, y + 1).isWalkable()){
					System.out.println(e + " GOES NORTHWEST");
					input.setNextEvent(InputAction.GO_NORTHWEST);
					done = true;
				} else if(randNr==1 && world.getTile(x, y + 1).isWalkable()){
					System.out.println(e + " GOES NORTH");
					input.setNextEvent(InputAction.GO_NORTH);
					done = true;
				} else if(randNr==2 && world.getTile(x + 1, y + 1).isWalkable()){
					System.out.println(e+" GOES NORTHEAST");
					input.setNextEvent(InputAction.GO_NORTHEAST);
					done = true;
				} else if(randNr==3 && world.getTile(x - 1, y).isWalkable()){
					System.out.println(e+" GOES WEST");
					input.setNextEvent(InputAction.GO_WEST);
					done = true;
				} else if(randNr==4 && world.getTile(x, y - 1).isWalkable()){
					System.out.println(e + " GOES SOUTH");
					input.setNextEvent(InputAction.GO_SOUTH);
					done = true;
				} else if(randNr==5 && world.getTile(x + 1, y).isWalkable()){
					System.out.println(e+" GOES EAST");
					input.setNextEvent(InputAction.GO_EAST);
					done = true;
				} else if(randNr==6 && world.getTile(x - 1, y - 1).isWalkable()){
					System.out.println(e + " GOES SOUTHWEST");
					input.setNextEvent(InputAction.GO_SOUTHWEST);
					done = true;
				} else if(randNr==7 && world.getTile(x + 1, y - 1).isWalkable()){
					System.out.println(e + " GOES SOUTHEAST");
					input.setNextEvent(InputAction.GO_SOUTHEAST);
					done = true;
				} 
			}
			
		}
		
	}

	private void track(Entity target) {
		// TODO Auto-generated method stub
		// implement some algorithm that tries to get to the position of 'target'
	}

	/**
	 * Add an entity to the AISystem
	 * 
	 * @param e
	 */
	public void addEntity(Entity e){
		System.out.println("Adding enemy");
		entities.add(e);		
	}
	
	/**
	 * Removes an entity from the AISystem
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
}
