package se.chalmers.roguelike.Systems;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.AI;
import se.chalmers.roguelike.Components.Input;

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
			if(randNr==0){
				input.setNextKey(Keyboard.KEY_Q);
			} else if(randNr==1){
				input.setNextKey(Keyboard.KEY_W);
			} else if(randNr==2){
				input.setNextKey(Keyboard.KEY_E);
			} else if(randNr==3){
				input.setNextKey(Keyboard.KEY_A);
			} else if(randNr==4){
				input.setNextKey(Keyboard.KEY_S);
			} else if(randNr==5){
				input.setNextKey(Keyboard.KEY_D);
			} else if(randNr==6){
				input.setNextKey(Keyboard.KEY_Z);
			} else if(randNr==7){
				input.setNextKey(Keyboard.KEY_X);
			} else if(randNr==8){
				input.setNextKey(Keyboard.KEY_C);
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
