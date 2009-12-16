package controller;

import java.util.GregorianCalendar;

import exception.TimeException;

import model.repositories.RepositoryManager;

public class TimeController {

	/**
	 * The RepositoryManager
	 */
	private RepositoryManager manager;
	
	/**
	 * Makes a new TimeController, bounded to the RepositoryManager <manager>.
	 */
	public TimeController(RepositoryManager manager){
		this.manager = manager;
	}
	
	/**
	 * Returns the current System's time.
	 */
	public GregorianCalendar getTime(){
		return manager.getClock().getTime();
	}
	
	/**
	 * Sets the current System's time.
	 * @param	newTime
	 * 			The new time to be given to the system.
	 * @throws 	TimeException
	 * 			The time <newTime> is before the current time.
	 */
	public void setTime(GregorianCalendar newTime) throws TimeException{
		manager.getClock().setTime(newTime);
	}
	
}
