package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import gui.Describable;


import exception.EmptyStringException;
import exception.NotAvailableException;
import exception.ResourceBusyException;

public class Resource implements Describable{

	
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
	 * An ArrayList keeping track of all the tasks using this resource.
	 */
	private ArrayList<Task> tasksUsing;
	
	
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
		tasksUsing = new ArrayList<Task>();
	}
	
	/**
	 * Adds a task to the lists of task requiring this resource.
	 * This method is used whenever the resources required by a task are updated
	 * and should not be used directly.
	 */
	protected void addTaskUsing(Task t){
		tasksUsing.add(t);
	}
	
	/**
	 * Removes a task from the lists of task requiring this resource.
	 * This method is used whenever the resources required by a task are updated
	 * and should not be used directly.
	 * @pre	The given task is currently in the list of tasks requiring this resource
	 * 		|taskUsing.contains(t)
	 */
	protected void removeTaskUsing(Task t){
		tasksUsing.remove(t);
	}
	
	/**
	 * This methods indicates whether this resource is required by any task.
	 * @return	true if for any Task task in the system
	 * 			task.requiredResources().contains(this)
	 */
	public Boolean requiredByTask() {
		return !tasksUsing.isEmpty();
	}

	/**
	 * Makes a reservation for this resource at the given time and duration,
	 * and for the given user.
	 * @param 	startTime
	 * 			The start date for the reservation.
	 * @param 	duration
	 * 			The duration of the reservation.
	 * @param 	user
	 * 			The user that made the reservation.
	 * @throws 	NotAvailableException
	 * 			The resource is not available during the given time span.
	 * 			| !this.availableAt(startTime, duration)
	 */
	public Reservation createReservation(GregorianCalendar startTime, int duration, User user) throws NotAvailableException {
		
		if(!this.availableAt(startTime, duration))
			throw new NotAvailableException("The resource is already reserved at that time.");
		
		Reservation res = new Reservation(user, startTime, duration, this);
		this.addReservation(res);
		return res;
		
	}

	/**
	 * Removes a resource from the system.
	 * @throws 	ResourceBusyException 
	 * 			The resource is in use by some task and can not be removed.
	 */
	public void remove() throws ResourceBusyException{
		if(this.requiredByTask())
			throw new ResourceBusyException("Resource is still being used.");
		
		for(Task t: this.tasksUsing){
			t.removeRequiredResource(this);
		}
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
	public boolean availableAt(GregorianCalendar begin,
			int duration) {
		boolean available = true;
		
		//copying the begin date and adding duration to get the end date
		GregorianCalendar end = new GregorianCalendar();
		end.set(Calendar.YEAR, begin.get(Calendar.YEAR));
		end.set(Calendar.MONTH, begin.get(Calendar.MONTH));
		end.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH));
		end.set(Calendar.HOUR_OF_DAY, begin.get(Calendar.HOUR_OF_DAY));
		end.set(Calendar.MINUTE, begin.get(Calendar.MINUTE));
		end.add(Calendar.MINUTE, duration);
		
		for(Reservation r: getReservations()){
			// do the same copy and add but to get the end date of a reservation
			GregorianCalendar endReservation = new GregorianCalendar();
			endReservation.set(Calendar.YEAR, (r.getTime()).get(Calendar.YEAR));
			endReservation.set(Calendar.MONTH, (r.getTime()).get(Calendar.MONTH));
			endReservation.set(Calendar.DAY_OF_MONTH, (r.getTime()).get(Calendar.DAY_OF_MONTH));
			endReservation.set(Calendar.HOUR_OF_DAY, (r.getTime()).get(Calendar.HOUR_OF_DAY));
			endReservation.set(Calendar.MINUTE, (r.getTime()).get(Calendar.MINUTE));
			endReservation.add(Calendar.MINUTE, duration);
			
			//Logic: if <endReservation> is after <begin> and <r.getTime()> is
			//	before <end>, both timespans overlap.
			
			boolean overlap = (endReservation.after(begin) 
					&& r.getTime().before(end));
			available = available && !overlap;
		}
		
		return available;
		
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
	protected void addReservation(Reservation r){
		reservations.add(r);
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) throws NullPointerException {
		if (type == null)
			throw new NullPointerException("Null has been passed");
		
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (reservations == null) {
			if (other.reservations != null)
				return false;
		} else if (!reservations.equals(other.reservations))
			return false;
		if (tasksUsing == null) {
			if (other.tasksUsing != null)
				return false;
		} else if (!tasksUsing.equals(other.tasksUsing))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Resource clone()
	{
		Resource res = null;
		try {
			res = new Resource(this.description,this.type);
			res.reservations = (ArrayList<Reservation>) this.reservations.clone();
			res.tasksUsing = (ArrayList<Task>) this.tasksUsing.clone();
		} catch (EmptyStringException e) {
		}
		return res;
	}
	
	

}
