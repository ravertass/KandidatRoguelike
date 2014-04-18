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
	private static List<String> log = new ArrayList<>(logSize);
	private static int pointer = 0;
	private static int events = 0;

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
		List<String> listOfEvents = new ArrayList<>();
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
}