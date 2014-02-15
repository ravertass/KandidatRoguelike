package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Input;
import se.chalmers.roguelike.Components.Position;
import se.chalmers.roguelike.World.World;

public class AISystem implements ISystem {

	/**
	 * A list of entities that has some kind of AI
	 */
	private ArrayList<Entity> entities;
	private AI ai;
	private Random rand;
	private World world;

	public AISystem(World world){
		entities = new ArrayList<Entity>();
		rand = new Random();
		this.world = world;
	}
	
	@Override
	public void update(){
		for (Entity e : entities){
			//move, attack or sleep?
			ai = e.getComponent(AI.class);
			Entity target = ai.getTarget();
			if (target != null){
				track(target);
			}
			
			Input input = e.getComponent(Input.class);
			int randNr = rand.nextInt(9);
			int x = e.getComponent(Position.class).getX();
			int y = e.getComponent(Position.class).getY();
			boolean done = true;
			while (!done) {
				if(randNr==0 && world.getTile(x - 1, y + 1).isWalkable()){
					input.setNextKey(Keyboard.KEY_Q);
					done = true;
				} else if(randNr==1 && world.getTile(x, y + 1).isWalkable()){
					input.setNextKey(Keyboard.KEY_W);
					done = true;
				} else if(randNr==2 && world.getTile(x + 1, y + 1).isWalkable()){
					input.setNextKey(Keyboard.KEY_E);
					done = true;
				} else if(randNr==3 && world.getTile(x - 1, y).isWalkable()){
					input.setNextKey(Keyboard.KEY_A);
					done = true;
				} else if(randNr==4 && world.getTile(x, y - 1).isWalkable()){
					input.setNextKey(Keyboard.KEY_S);
					done = true;
				} else if(randNr==5 && world.getTile(x, y + 1).isWalkable()){
					input.setNextKey(Keyboard.KEY_D);
					done = true;
				} else if(randNr==6 && world.getTile(x - 1, y - 1).isWalkable()){
					input.setNextKey(Keyboard.KEY_Z);
					done = true;
				} else if(randNr==7 && world.getTile(x + 1, y - 1).isWalkable()){
					input.setNextKey(Keyboard.KEY_C);
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
