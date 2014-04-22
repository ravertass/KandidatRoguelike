package se.chalmers.roguelike.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This class is holding information that the player should get as output
 * so the player know what is going on in the game
 *
 */
public class CombatLog {

	private final static int logSize = 20;
	private static List<String> log = new ArrayList<String>(logSize);
	private static int pointer = 0;
	private static int events = 0;
	private static boolean visible = true;

	/**
	 * a static method to add new event that should be written as output
	 * 
	 * @param logEvent
	 */
	public static void addToLog(String logEvent) {
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
	public static List<String> getLog() {
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
	 * Returns true if the log is shown
	 * @return
	 */
	public static boolean isVisible() {
		return visible;
	}
	
	/**
	 * Set true if the log should be visible
	 * @param showLog
	 */
	public static void setActive(boolean showLog){
		visible = showLog;
	}
	
	/**
	 * clears the log (useful for new games etc)
	 */
	public static void reset(){
		log.clear();
		pointer = 0;
		events = 0;
	}
}