package model;

import java.util.GregorianCalendar;

public class Clock {

	/**
	 * The time as indicated by this clock.
	 */
	private GregorianCalendar currentTime;
	
	/**
	 * Initializes a new clock, indicating the time <time>
	 * @param	time
	 * 			The time to be written on the clock
	 * @post	new.getTime().equals(time)
	 */
	public Clock(GregorianCalendar time){
		this.currentTime = time;
	}
	
	/**
	 * Initializes a new clock, set to the local time.
	 * @post	Immediately after initialization, the clock is set to the local 
	 * 			system time.
	 * 			However, this is not kept consistent.
	 */
	public Clock(){
		this.currentTime = new GregorianCalendar();
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
	 * @post	<newTime> is the new time on the clock
	 * 			|new.getTime().equals(newTime)
	 */
	public void setTime(GregorianCalendar newTime){
		this.currentTime = newTime;
	}
}
