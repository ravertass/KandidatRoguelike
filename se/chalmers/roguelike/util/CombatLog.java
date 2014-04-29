package se.chalmers.roguelike.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.lwjgl.input.Mouse;

import se.chalmers.roguelike.Engine;
import se.chalmers.roguelike.InputManager;
import se.chalmers.roguelike.InputManager.InputAction;

/**
 * This class is holding information that the player should get as output
 * so the player know what is going on in the game
 *
 */
public class CombatLog implements Observer{

	private final int logSize = 20;
	private List<String> log;
	private int pointer;
	private int events;
	
	private List<String> debugLog;
	private int debugPointer;
	private int debugEvents;
	
	private boolean visible;
	private static CombatLog combatLog = null;

	public CombatLog(){
		pointer = 0;
		events = 0;
		visible = true;
		log = new ArrayList<String>(logSize);
		debugPointer = 0;
		debugEvents = 0;
		debugLog = new ArrayList<String>(logSize);
	}
	
	public static CombatLog getInstance(){
		if (combatLog == null){
			combatLog = new CombatLog();
		}
		return combatLog;
	}
	
	/**
	 * a static method to add new event that should be written as output
	 * 
	 * @param logEvent
	 */
	public void addToLog(String logEvent) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		String time = new SimpleDateFormat("[HH:mm:ss] ").format(cal.getTime());
		if (events < logSize) {
			log.add(time + logEvent);
			events++;
		} else {
			log.set(pointer, (time + logEvent));
		}
		pointer = (pointer + 1) % logSize;
	}

	/**
	 * Gives the list of events
	 * 
	 * @return
	 */
	public List<String> getLog() {
		List<String> listOfEvents = new ArrayList<String>();
		int index = pointer - 1;
		
		if (events == logSize) {
			if (index < 0) {
				index += logSize;
			}
			while (index != pointer) {
				listOfEvents.add(log.get(index));
				index--;
				if (index < 0) {
					index += logSize;
				}
			}
		} else {
			while (!(index < 0)) {
				listOfEvents.add(log.get(index));
				index--;
			}
		}
		return listOfEvents;
	}
	
	/**
	 * For getting debug data
	 * @return
	 */
	public List<String> getDebugLog() {
		List<String> listOfEvents = new ArrayList<String>();
		int index = debugPointer - 1;
		
		if (debugEvents == logSize) {
			if (index < 0) {
				index += logSize;
			}
			while (index != debugPointer) {
				listOfEvents.add(debugLog.get(index));
				index--;
				if (index < 0) {
					index += logSize;
				}
			}
		} else {
			while (!(index < 0)) {
				listOfEvents.add(debugLog.get(index));
				index--;
			}
		}
		return listOfEvents;
	}
	
	/**
	 * For adding debug data
	 * @param debugEvent
	 */
	public void addDebugEvent(String debugEvent) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		String time = new SimpleDateFormat("[HH:mm:ss] ").format(cal.getTime());
		if (debugEvents < logSize) {
			debugLog.add(time + debugEvent);
			debugEvents++;
		} else {
			debugLog.set(debugPointer, (time + debugEvent));
		}
		debugPointer = (debugPointer + 1) % logSize;
	}
	
	/**
	 * Returns true if the log is shown
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Set true if the log should be visible
	 * @param showLog
	 */
	public void setActive(boolean showLog){
		visible = showLog;
	}
	
	/**
	 * clears the log (useful for new games etc)
	 */
	public void reset(){
		log.clear();
		pointer = 0;
		events = 0;
	}

	@Override
	public void notify(Enum<?> i) {
		if(((InputAction) i == InputAction.MOUSECLICK) && Engine.gameState == Engine.GameState.DUNGEON) {
			if (Mouse.getX() < 16 && Mouse.getY() < 16){
				setActive(!visible);
			}
		}
	}
}