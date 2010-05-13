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
		this.startDate = (GregorianCalendar) startDate.clone();
		this.dueDate = (GregorianCalendar) endDate.clone();
	}
	public int getDuration() {
		return duration;
	}
	public GregorianCalendar getStartDate() {
		return (GregorianCalendar) startDate.clone();
	}
	public GregorianCalendar getDueDate() {
		return (GregorianCalendar) dueDate.clone();
	}
	private int duration;
	private GregorianCalendar startDate;
	private GregorianCalendar dueDate;
}
