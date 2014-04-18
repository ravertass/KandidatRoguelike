package se.chalmers.roguelike.Systems;

import java.util.ArrayList;

import se.chalmers.roguelike.Entity;
import se.chalmers.roguelike.Components.TurnsLeft;
import se.chalmers.roguelike.InputManager.InputAction;
import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.util.Subject;

/**
 * The turn system handles reseting of turns if no entity has any turns left.
 */
public class TurnSystem implements ISystem, Subject {
	
	private ArrayList<Observer> observers;
	
	private ArrayList<Entity> entities;
	
	/**
	 * Initializes a new turnsystem
	 */
	public TurnSystem(){
		observers = new ArrayList<Observer>();
		entities = new ArrayList<Entity>();
	}
	
	/**
	 * runs with the game loop and checks if the turns should be reset for all entities or not
	 */
	public void update() {
		for(Entity e : entities){
			if(e.getComponent(TurnsLeft.class).getTurnsLeft() > 0){
				return; // means we found an entity that hasn't used up his turns
			}
		}
		// Otherwise: Reset turns
		for(Entity e : entities){
			int turnsLeft = e.getComponent(TurnsLeft.class).getTurnsLeft();
			e.getComponent(TurnsLeft.class).setTurnsLeft(++turnsLeft);
		}
		notifyObservers(InputAction.NEWTURN);
	}

	/**
	 * Adds an entity from the system
	 * 
	 * @param entity entity that should be added
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	/**
	 * Removes an entity from the system
	 * 
	 * @param entity entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
		
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}
	/**
	 * This will notify all observers that there is now a new turn.
	 */
	@Override
	public void notifyObservers(Enum<?> i) {
		for(Observer o : observers) {
			o.notify(InputAction.NEWTURN);
		}
		
	}
}
