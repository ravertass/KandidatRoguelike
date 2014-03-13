package se.chalmers.roguelike;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.util.Observer;
import se.chalmers.roguelike.util.Subject;

public class InputManager implements Subject {

	private ArrayList<Observer> observers;
	private Engine engine;

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
		this.engine = engine;
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
	/**
	 * This is where the input is passed on to all the system who subscribes to input.
	 */
	@Override
	public void notifyObservers(final Enum<?> e) {
		for (Observer o : observers) {
			o.notify(e);
		}

	}
	/**
	 * This is where the keyboard and mouse are checked for input and the appropriate event is sent to subscribers.
	 */
	public void update() {
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				System.exit(0); //Should be passed on to engine?
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)
					&& Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				notifyObservers(InputAction.SET_FULLSCREEN);
			}
			if (Keyboard.getEventKeyState()) {
				int key = Keyboard.getEventKey();
				if (key == Keyboard.KEY_W || key == Keyboard.KEY_NUMPAD8) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_NORTH);
				} else if (key == Keyboard.KEY_A || key == Keyboard.KEY_NUMPAD4) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_WEST);
				} else if (key == Keyboard.KEY_S || key == Keyboard.KEY_NUMPAD2) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_SOUTH);
				} else if (key == Keyboard.KEY_D || key == Keyboard.KEY_NUMPAD6) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_EAST);
				} else if (key == Keyboard.KEY_Q || key == Keyboard.KEY_NUMPAD7) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_NORTHWEST);
				} else if (key == Keyboard.KEY_E || key == Keyboard.KEY_NUMPAD9) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_NORTHEAST);
				} else if (key == Keyboard.KEY_Z || key == Keyboard.KEY_NUMPAD1) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_SOUTHWEST);
				} else if (key == Keyboard.KEY_C || key == Keyboard.KEY_NUMPAD3) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.GO_SOUTHEAST);
				} else if (key == Keyboard.KEY_NUMPAD5) {
					System.out.println("-----PLAYERDOESSOMETHING-----");
					notifyObservers(InputAction.DO_NOTHING);
				}
				
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F1)){
				Engine.debug = !Engine.debug; 
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F2)){
				//Engine.dungeon.unregister();
				engine.loadOverworld();
				// Engine.gameState = Engine.GameState.DUNGEON;
			}
		}
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if(engine.debug){
					System.out.println("Mouse click at X: "+Mouse.getX()+" Y: "+Mouse.getY());
				}
				notifyObservers(InputAction.MOUSECLICK);
			}

		}

	}

}
