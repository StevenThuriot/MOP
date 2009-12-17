package model;

import java.util.GregorianCalendar;
import java.util.List;

import exception.TimeException;

import model.repositories.RepositoryManager;

public class Clock implements Subject {

	/**
	 * The time as indicated by this clock.
	 */
	private GregorianCalendar currentTime;
	
	/**
	 * The RepositoryManager for the system.
	 */
	private RepositoryManager manager;
	
	/**
	 * Initializes a new clock, indicating the time <time>
	 * @param	time
	 * 			The time to be written on the clock
	 * @post	new.getTime().equals(time)
	 */
	public Clock(RepositoryManager manager, GregorianCalendar time){
		this.currentTime = time;
		this.manager = manager;
	}
	
	/**
	 * Returns the time as indicated on the clock.
	 */
	public GregorianCalendar getTime(){
		return currentTime;
	}
	
	/**
	 * Changes the current time on the clock.
	 * @param 	newTime
	 * 			The new time on the clock.
	 * @throws TimeException 
	 * @post	<newTime> is the new time on the clock
	 * 			|new.getTime().equals(newTime)
	 */
	public void setTime(GregorianCalendar newTime) throws TimeException{
		if(newTime.before(this.getTime())){
			throw new TimeException("The new time is before the old time.");
		}
		this.currentTime = newTime;
		publish();
	}
	
	public RepositoryManager getManager(){
		return manager;
	}
	
	/**
	 * Notifies all tasks that this Clock has changed.
	 */
	public void publish(){
		List<Task> allTasks = this.getManager().getTasks();
		for(Task t: allTasks){
			t.update(this);
		}
	}
}
