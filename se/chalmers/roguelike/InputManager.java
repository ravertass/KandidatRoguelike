package se.chalmers.roguelike;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.util.Subject;

/**
 * The input manager is the subject in an observer-pattern. It sends the input to all the listeners so that
 * various systems can do what they want with it.
 */
public class InputManager implements Subject {

	private ArrayList<Observer> observers;
	private ArrayList<Observer> observersToAdd;
	private ArrayList<Observer> observersToRemove;
	private Engine engine;

	private long startTime;
	private int pressedKey;
	private boolean busy;


	private HashMap<Integer, InputAction> keyToAction;

	/**
	 * This is where you declare all the different events that the keyboard and mouse can cause. 
	 * This is where you declare all the different events that the keyboard and
	 * mouse can cause.
	 * 
	 * @author twister
	 * 
	 */
	public static enum InputAction {
		GO_NORTH, GO_SOUTH, GO_WEST, GO_EAST, GO_NORTHEAST, GO_NORTHWEST, GO_SOUTHWEST, GO_SOUTHEAST, 
		SET_FULLSCREEN, MOUSECLICK, DO_NOTHING, DUMMY, LOOT, INTERACTION, TURN_NORTH, TURN_SOUTH, TURN_WEST, 
		TURN_EAST, NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9, BACKSPACE, ENTER
	}
	
	/**
	 * Creates a new instance of the input manager
	 * @param engine the game engine that is being used
	 */
	public InputManager(Engine engine) {
		keyToAction = new HashMap<Integer, InputAction>();
		this.engine = engine;
		observers = new ArrayList<Observer>();
		observersToAdd = new ArrayList<Observer>();
		observersToRemove = new ArrayList<Observer>();
		setupKeyToAction();
		busy = false;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void addObserver(Observer o) {
		if(!busy){
			observers.add(o);
		} else {
			observersToAdd.add(o);
		}

	}

	@Override
	public void removeObserver(Observer o) {
		if(!busy){
			observers.remove(o);
		} else {
			observersToRemove.add(o);
		}

	}

	/**
	 * This is where the input is passed on to all the system who subscribes to
	 * input.
	 */
	@Override
	public void notifyObservers(final Enum<?> e) {
		busy = true;
		for (Observer o : observers) {
			o.notify(e);
		}
		startTime = System.currentTimeMillis();
		
		/*
		 * This code might look weird, but it's due to observers being added as a result of a notify
		 * when newGame() in engine runs from the main menu system. Without this structure it causes
		 * ConcurrentModificationException
		 */
		for(Observer o : observersToAdd){
			observers.add(o);
		}
		observersToAdd.clear();
		
		for(Observer o : observersToRemove){
			observers.remove(o);
		}
		observersToRemove.clear();
		busy = false;
	}

	/**
	 * This is where the keyboard and mouse are checked for input and the
	 * appropriate event is sent to subscribers.
	 */
	public void update() {
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(pressedKey)
				&& System.currentTimeMillis() - startTime > 180L) {
			if (keyToAction.get(pressedKey) == InputAction.GO_NORTHEAST
					|| keyToAction.get(pressedKey) == InputAction.GO_NORTH
					|| keyToAction.get(pressedKey) == InputAction.GO_NORTHWEST
					|| keyToAction.get(pressedKey) == InputAction.GO_WEST
					|| keyToAction.get(pressedKey) == InputAction.GO_SOUTHWEST
					|| keyToAction.get(pressedKey) == InputAction.GO_SOUTH
					|| keyToAction.get(pressedKey) == InputAction.GO_SOUTHEAST
					|| keyToAction.get(pressedKey) == InputAction.GO_EAST)
//					|| keyToAction.get(pressedKey) == InputAction.BACKSPACE)
				notifyObservers(keyToAction.get(pressedKey));
		}
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				System.exit(0); // Should be passed on to engine?
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
				Engine.debug = !Engine.debug;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
				engine.loadOverworld();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)
					&& Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				notifyObservers(InputAction.SET_FULLSCREEN);
			}
			if (Keyboard.getEventKeyState()) {
				int key = Keyboard.getEventKey();
				pressedKey = key;
				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && keyToAction.containsKey(key))
					notifyObservers(keyToAction.get(key));
				else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					if(key == Keyboard.KEY_W) {
						notifyObservers(InputAction.TURN_NORTH);
					} else if(key == Keyboard.KEY_S) {
						notifyObservers(InputAction.TURN_SOUTH);
					} else if(key == Keyboard.KEY_A) {
						notifyObservers(InputAction.TURN_WEST);
					} else if(key == Keyboard.KEY_D) {
						notifyObservers(InputAction.TURN_EAST);
					}
				}
			}
		}

		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Engine.debug) {
					System.out.println("Mouse click at X: " + Mouse.getX() + " Y: " + Mouse.getY());
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
		keyToAction.put(Keyboard.KEY_NUMPAD5, InputAction.DO_NOTHING);
		keyToAction.put(Keyboard.KEY_G, InputAction.LOOT);
		keyToAction.put(Keyboard.KEY_F, InputAction.INTERACTION);
		keyToAction.put(Keyboard.KEY_0, InputAction.NUM_0);
		keyToAction.put(Keyboard.KEY_1, InputAction.NUM_1);
		keyToAction.put(Keyboard.KEY_2, InputAction.NUM_2);
		keyToAction.put(Keyboard.KEY_3, InputAction.NUM_3);
		keyToAction.put(Keyboard.KEY_4, InputAction.NUM_4);
		keyToAction.put(Keyboard.KEY_5, InputAction.NUM_5);
		keyToAction.put(Keyboard.KEY_6, InputAction.NUM_6);
		keyToAction.put(Keyboard.KEY_7, InputAction.NUM_7);
		keyToAction.put(Keyboard.KEY_8, InputAction.NUM_8);
		keyToAction.put(Keyboard.KEY_9, InputAction.NUM_9);
		keyToAction.put(Keyboard.KEY_BACK, InputAction.BACKSPACE);
		keyToAction.put(Keyboard.KEY_RETURN, InputAction.ENTER);
	}
}
