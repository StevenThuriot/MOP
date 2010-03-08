package model;

import java.util.GregorianCalendar;

/**
 * Class to help create Tasks. 
 * This class was introduced in the refactoring process in order to keep the parameter count low when constructing tasks
 * @author Bart Vangeneugden
 *
 */
public class TaskTimings {
	/**
	 * Constructor for TaskTimings
	 * @param duration
	 * @param startDate
	 * @param dueDate
	 */
	public TaskTimings(GregorianCalendar startDate, GregorianCalendar endDate, int duration) {
		super();
		this.duration = duration;
		this.startDate = startDate;
		this.dueDate = endDate;
	}
	public int getDuration() {
		return duration;
	}
	public GregorianCalendar getStartDate() {
		return startDate;
	}
	public GregorianCalendar getDueDate() {
		return dueDate;
	}
	private int duration;
	private GregorianCalendar startDate;
	private GregorianCalendar dueDate;
}
