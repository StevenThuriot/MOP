package model;

import java.util.GregorianCalendar;

import exception.NotAvailableException;

public class Reservation {
	/**
	 * The resource for which this reservation is made.
	 */
	private Resource reservedResource;
	
	/**
	 * The time for which this reservation is made.
	 */
	private GregorianCalendar time;
	
	/**
	 * The duration of this reservation.
	 */
	private int duration;
	
	/**
	 * The user who made this reservation.
	 */
	private User user;

	/**
	 * Makes a new Reservation with the given time, duration and resource, and
	 * for the given user.
	 * @param 	newUser
	 * 			The user for which this reservation is made.
	 * @param 	newTime
	 * 			The time for which this reservation is made. 
	 * @param 	newDuration
	 * 			The duration the reservation will be made for.
	 * @param 	newResource
	 * 			The resource that the reservation applies to.
	 * @throws 	NotAvailableException
	 * 			The resource is not available at the given time.
	 * 			|!newResource.availableAt(newTime)
	 */
	//TODO : update status of resource object
	public Reservation(User newUser, GregorianCalendar newTime, int newDuration, Resource newResource) throws NotAvailableException{
		setTime(newTime);
		
		setDuration(newDuration);
		
		if(!newResource.availableAt(newTime, newDuration))
			throw new NotAvailableException(
					"This resource is not available at the given time.");
		
		setReservedResource(newResource);
		newResource.addReservation(this);
		setUser(newUser);
	}
	

	/**
	 * Sets the user that made this reservation.
	 * @post	| new.getUser() == newUser
	 */
	private void setUser(User newUser) throws NullPointerException{		
		if (newUser == null)
			throw new NullPointerException("Null was passed");
		
		user = newUser;
	}
	
	/**
	 * Returns the start date for this reservation.
	 */
	public GregorianCalendar getTime(){
		return time;
	}
	
	/**
	 * Sets the start date for this reservation to be <newTime>.
	 * @post	| new.getTime() == newTime
	 */
	private void setTime(GregorianCalendar newTime) throws NullPointerException{
		if (newTime == null)
			throw new NullPointerException("Null was passed");
		time = newTime;
	}
	
	/**
	 * Returns the duration of this reservation.
	 * This is expressed in minutes.
	 */
	public int getDuration(){
		return duration;
	}
	
	/**
	 * Sets the duration of this reservation to be <newDuration>.
	 * This is expressed in minutes.
	 * @post	| new.getDuration() == newDuration
	 */
	private void setDuration(int newDuration){		
		duration = newDuration;
	}
	
	/**
	 * Returns the resource that is reservation is made for.
	 */
	public Resource getReservedResource(){
		return reservedResource;
	}
	
	/**
	 * Sets the resource reserved by this reservation to be <newResource>.
	 * @post	|new.getReservedResource() == newResource
	 */
	private void setReservedResource(Resource newResource) throws NullPointerException{
		if (newResource == null)
			throw new NullPointerException("Null was passed");
		
		reservedResource = newResource;
	}
	
	/**
	 * Returns the user that made this reservation.
	 */
	public User getUser(){
		return user;
	}
}
