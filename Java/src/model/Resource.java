package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import gui.Describable;
import exception.EmptyStringException;

public class Resource implements Describable, Asset{

	
	/**
	 * A string giving a description for this resource.
	 * @invar	<description> must not be empty.
	 */
	private String description;
	
	/**
	 * An ArrayList keeping track of all the reservations that have been made
	 * for this resource.
	 */
	private ArrayList<Reservation> reservations;
	
	/**
	 * The resource's type
	 */
	private ResourceType type;
	
	
	
	/**
	 * Initiates a resource with the given description
	 * @param 	newDescription
	 * 			The description for this resource.
	 * @throws 	EmptyStringException
	 * 			The description given is empty.
	 * 			| newDescription == ""
	 */
	public Resource(String newDescription, ResourceType type) throws EmptyStringException
	{
		this.setDescription(newDescription);
		this.setType(type);
		reservations = new ArrayList<Reservation>();
	}
	
	/**
	 * Returns an ArrayList of the reservations made for this resource.
	 */
	public List<Reservation> getReservations() {
		return Collections.unmodifiableList(reservations);
	}

	/**
	 * Indicates whether a resource is reserved during the specified time span. This is a 
	 * "safe" implementation: whenever the resource is reserved anywhere during the time span,
	 * the method returns "false".
	 * @param 	gregorianCalendar
	 * 			The start date for the period to check.
	 * @param 	duration
	 * 			The duration of the period to check.
	 * @return	False if any reservation is made for a period that overlaps with the given period.
	 */
	public boolean availableAt(GregorianCalendar begin, int duration) {
	
		GregorianCalendar end = (GregorianCalendar) begin.clone();
		end.add(Calendar.MINUTE, duration);
		
		for(Reservation reservation: getReservations()){
			GregorianCalendar endReservation = (GregorianCalendar) reservation.getTime().clone();
			endReservation.add(Calendar.MINUTE, reservation.getDuration());
			if(reservation.getTime().before(end) && endReservation.after(begin) )
				return false;
		}
		return true;
	}
	
	/**
	 * Returns the description of this resource.
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Returns a String representation of the Resource. At the moment, returns the
	 * description.
	 */
	public String toString(){
		return getDescription();
	}
	
	/**
	 * Changes the description for this Resource.
	 * @param 	newDescription
	 * 			The new description
	 * @post	The resource now has this description.
	 * 			| new.getDescription() = newDescription
	 * @throws 	EmptyStringException
	 * 			The description given is empty
	 * 			| newDescription == ""
	 * @throws 	NullPointerException
	 * 			The description given is empty
	 * 			| newDescription == ""
	 */
	private void setDescription(String newDescription) throws EmptyStringException, NullPointerException {
		if (newDescription == null)
			throw new NullPointerException("Null has been passed");
		
		if(newDescription.length() == 0)
			throw new EmptyStringException("A resource should have a non-empty description");
		
		description = newDescription;
	}
	
	/**
	 * Adds a new reservation for this resource. The user of this method should make sure
	 * that the resource is available at the times specified in the reservation.
	 * @pre		This resource is available at the times specified in r.
	 * 			|this.availableAt(r.getTime(), r.getDuration())
	 * @param	r
	 * 			The new reservation to add.
	 */
	protected void addReservation(Reservation reservation){
		reservations.add(reservation);
	}

	/**
	 * Return the ResourceType of this Resource.
	 */
	public ResourceType getType() {
		return type;
	}

	protected void setType(ResourceType type) throws NullPointerException {
		if (type == null)
			throw new NullPointerException("Null has been passed");
		
		this.type = type;
	}
	
	/**
	 * Removes this reservation for this resource. Is protected as it should only be used by the reservation itself.
	 */
	protected void removeReservation(Reservation reservation){
		reservations.remove(reservation);
	}
	

}
