package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.GregorianCalendar;

import exception.EmptyStringException;
import exception.NotAvailableException;
import exception.ResourceBusyException;
import model.Reservation;
import model.Resource;
import model.ResourceType;
import model.User;
import model.repositories.RepositoryManager;

/**
 * Controller to interact with resources
 */
public class ResourceController {
    /**
     * Repository Manager
     */
    private RepositoryManager manager;
    
    /**
     * Constructor that takes a RepositoryManager as argument. Will throw NullPointerException if the latter was null.
     * @param manager
     */
	public ResourceController(RepositoryManager manager) {
	    if(manager==null)
	        throw new NullPointerException();
        this.manager = manager;
    }

    /**
	 * Create a new resource
	 * @param description
	 * @param type
	 * @return
	 * @throws EmptyStringException
	 */
	public Resource createResource(String description, ResourceType type) throws EmptyStringException {
	    Resource res = new Resource(description, type);
	    if(manager.add(res))
	    	return res;
	    return null;
	}

	/**
	 * Remove a resource
	 * @param r
	 * @throws ResourceBusyException
	 */
	public void removeResource(Resource r) throws ResourceBusyException {
		manager.remove(r);
	}
	
	/**
	 * Get a list of all reservations
	 * @return
	 */
	public List<Reservation> getReservations() {        
        ArrayList<Reservation> reservations = new  ArrayList<Reservation>();
        for(Resource resource : manager.getResources())
        {
            List<Reservation> reservation = resource.getReservations();
            reservations.addAll(reservation);
        }
        return  Collections.unmodifiableList(reservations);
	}
	
	/**
	 * Get a list of all resources
	 * @return
	 */
	public List<Resource> getResources() {
		return  manager.getResources();
	}

	/**
	 * Create a new reservation
	 * @param startTime
	 * @param date
	 * @param duration
	 * @param resource
	 * @param user
	 * @return
	 * @throws NotAvailableException
	 */
	public Reservation createReservation(GregorianCalendar startTime, int duration, Resource resource, User user) throws NotAvailableException {
		return resource.createReservation(startTime,  duration, user);
	}
}
