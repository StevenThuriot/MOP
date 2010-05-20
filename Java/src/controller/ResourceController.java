package controller;

import java.util.GregorianCalendar;
import java.util.List;

import model.Reservation;
import model.Resource;
import model.ResourceType;
import model.Task;
import model.repositories.RepositoryManager;
import exception.AssetAllocatedException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.NoReservationOverlapException;
import exception.NotAvailableException;

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
	 * Create a new reservation
	 * @param startTime
     * @param duration
     * @param resource
     * @param date
     * @return
	 * @throws NotAvailableException
     * @throws NoReservationOverlapException 
     * @throws AssetAllocatedException 
     * @throws IllegalStateCallException 
	 */
	public Reservation createReservation(GregorianCalendar startTime, int duration, Resource resource, Task task) throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException, IllegalStateCallException {
		return new Reservation(startTime,duration,  resource, task);
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
	    manager.add(res);
	    return res;
	}
	
	/**
	 * Get a list of all resources
	 * @return
	 */
	public List<Resource> getResources() {
		return  manager.getResources();
	}
	
	/**
	 * Create a new Resource Type and add it to the repository
	 * @param id
	 * @param name
	 * @return
	 */
	public ResourceType createResourceType(String name)
	{
		ResourceType type = new ResourceType(name);
		manager.add(type);
		return type;
	}
}
