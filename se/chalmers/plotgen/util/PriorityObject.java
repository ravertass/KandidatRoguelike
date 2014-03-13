package se.chalmers.plotgen.util;

public class PriorityObject<E> implements Comparable<PriorityObject> {

	private int priority;
	private E object;
	
	public PriorityObject(int priority, E object) {
		this.priority = priority;
		this.object = object;
	}

	public E get() {
		return object;
	}
	
	@Override
	public int compareTo(PriorityObject o) {
		if (priority > o.priority) {
			return 1;
		}
		if (priority < o.priority) {
			return -1;
		}
		return 0;
	}
}
