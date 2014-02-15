package se.chalmers.roguelike;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import se.chalmers.roguelike.Systems.ISystem;
import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.util.Subject;

public class InputManager implements ISystem, Subject {
	
	private ArrayList<Observer> observers;
	
	public static enum InputAction {
		GO_NORTH, GO_SOUTH, GO_WEST, GO_EAST, GO_NORTHEAST, GO_NORTHWEST, GO_SOUTHWEST, GO_SOUTHEAST //TODO add more stuff here
	}
	
	public InputManager() {
		observers = new ArrayList<Observer>();
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
		
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
		
	}

	@Override
	public void notifyObservers(final Enum<?> e) {
		for(Observer o : observers) {
			//TODO add stuff here
		}
		
	}

	@Override
	public void update() {
		
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				int key = Keyboard.getEventKey();
				if(key == Keyboard.KEY_W || key == Keyboard.KEY_NUMPAD8) {
					notifyObservers(InputAction.GO_NORTH);
				} else if(key == Keyboard.KEY_A || key == Keyboard.KEY_NUMPAD4) {
					notifyObservers(InputAction.GO_WEST);
				} else if(key == Keyboard.KEY_S || key == Keyboard.KEY_NUMPAD2) {
					notifyObservers(InputAction.GO_SOUTH);
				} else if(key == Keyboard.KEY_D || key == Keyboard.KEY_NUMPAD6) {
					notifyObservers(InputAction.GO_EAST);
				} else if(key == Keyboard.KEY_Q || key == Keyboard.KEY_NUMPAD7) {
					notifyObservers(InputAction.GO_NORTHWEST);
				} else if(key == Keyboard.KEY_E || key == Keyboard.KEY_NUMPAD9) {
					notifyObservers(InputAction.GO_NORTHEAST);
				} else if(key == Keyboard.KEY_Z || key == Keyboard.KEY_NUMPAD1) {
					notifyObservers(InputAction.GO_SOUTHWEST);
				} else if(key == Keyboard.KEY_C || key == Keyboard.KEY_NUMPAD3) {
					notifyObservers(InputAction.GO_SOUTHEAST);
				}
			}
			}
		
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}

}
