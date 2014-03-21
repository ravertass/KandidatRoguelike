package se.chalmers.roguelike;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.util.Subject;

public class InputManager implements Subject {

	private ArrayList<Observer> observers;
	private Engine engine;
	
	private long startTime;
	private int pressedKey;
	
	private HashMap<Integer, InputAction> keyToAction;

	/**
	 * This is where you delcare all the different events that the keyboard and mouse can cause. 
	 * @author twister
	 *
	 */
	public static enum InputAction {
		GO_NORTH, GO_SOUTH, GO_WEST, GO_EAST, GO_NORTHEAST, GO_NORTHWEST, GO_SOUTHWEST, GO_SOUTHEAST, SET_FULLSCREEN, MOUSECLICK, DO_NOTHING,
		DUMMY// TODO
		// add
		// more
		// stuff
		// here
	}

//	public InputManager() {
//		observers = new ArrayList<Observer>();
//	}
	public InputManager(Engine engine){
		keyToAction = new HashMap<Integer, InputAction>();
		this.engine = engine;
		observers = new ArrayList<Observer>();
		setupKeyToAction();
		startTime = System.currentTimeMillis();
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
	 * This is where the input is passed on to all the system who subscribes to input.
	 */
	@Override
	public void notifyObservers(final Enum<?> e) {
		for (Observer o : observers) {
			o.notify(e);
		}
		startTime = System.currentTimeMillis();

	}
	/**
	 * This is where the keyboard and mouse are checked for input and the appropriate event is sent to subscribers.
	 */
	public void update() {
		if(Keyboard.isKeyDown(pressedKey) && System.currentTimeMillis() - startTime > 180L) {
			if(keyToAction.containsKey(pressedKey))
				notifyObservers(keyToAction.get(pressedKey));
		}
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				System.exit(0); //Should be passed on to engine?
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F1)){
				Engine.debug = !Engine.debug; 
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F2)){
				engine.loadOverworld();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)
					&& Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				notifyObservers(InputAction.SET_FULLSCREEN);
			}
			if (Keyboard.getEventKeyState()) {
				int key = Keyboard.getEventKey();
				pressedKey = key;
				if(keyToAction.containsKey(key))
					notifyObservers(keyToAction.get(key));
				
			}
		}
		
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if(Engine.debug){
					System.out.println("Mouse click at X: "+Mouse.getX()+" Y: "+Mouse.getY());
				}
				notifyObservers(InputAction.MOUSECLICK);
			}

		}
		
		

	}
	
	/**
	 * This will setup the hashmap that contains the key mapped to the action.
	 */
	private void setupKeyToAction() {
		keyToAction.put(Keyboard.KEY_W, InputAction.GO_NORTH);
		keyToAction.put(Keyboard.KEY_NUMPAD8, InputAction.GO_NORTH);
		keyToAction.put(Keyboard.KEY_A, InputAction.GO_WEST);
		keyToAction.put(Keyboard.KEY_NUMPAD4, InputAction.GO_WEST);
		keyToAction.put(Keyboard.KEY_D, InputAction.GO_EAST);
		keyToAction.put(Keyboard.KEY_NUMPAD6, InputAction.GO_EAST);
		keyToAction.put(Keyboard.KEY_S, InputAction.GO_SOUTH);
		keyToAction.put(Keyboard.KEY_NUMPAD2, InputAction.GO_SOUTH);
		keyToAction.put(Keyboard.KEY_Q, InputAction.GO_NORTHWEST);
		keyToAction.put(Keyboard.KEY_NUMPAD7, InputAction.GO_NORTHWEST);
		keyToAction.put(Keyboard.KEY_E, InputAction.GO_NORTHEAST);
		keyToAction.put(Keyboard.KEY_NUMPAD9, InputAction.GO_NORTHEAST);
		keyToAction.put(Keyboard.KEY_Z, InputAction.GO_SOUTHWEST);
		keyToAction.put(Keyboard.KEY_NUMPAD1, InputAction.GO_SOUTHWEST);
		keyToAction.put(Keyboard.KEY_C, InputAction.GO_SOUTHEAST);
		keyToAction.put(Keyboard.KEY_NUMPAD3, InputAction.GO_SOUTHEAST);
		keyToAction.put(Keyboard.KEY_W, InputAction.GO_NORTH);
		keyToAction.put(Keyboard.KEY_W, InputAction.GO_NORTH);
		keyToAction.put(Keyboard.KEY_NUMPAD5, InputAction.DO_NOTHING);
		
	}
	
	

}
