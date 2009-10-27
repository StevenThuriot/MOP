package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.EmptyStringException;
import exception.ResourceBusyException;

public class ResourceManager {

	private static ResourceManager instance;
	private ArrayList<Resource> resources;
	
	public static ResourceManager getInstance() {
		if(instance==null)
		{
			instance = new ResourceManager();
		}
		return instance;
	}

	private ResourceManager()
	{
		resources = new ArrayList<Resource>();
	}
	public void remove(Resource r) throws ResourceBusyException {
		if (r.requiredByTask()) {
			throw new ResourceBusyException("The resource is in use and can't be removed.");
		} else {
			this.resources.remove(r);
			r.remove();
		}
	}

	public Resource createResource(String description, ResourceType type) throws EmptyStringException {
		Resource resource = new Resource(description, type);
		resources.add(resource);
		
		return resource;
	}

	public List<Resource> findAll() {
		return Collections.unmodifiableList(resources);
	}
	
	public List<Reservation> getReservations()
	{
		List<Resource> resourceList = this.findAll();
		
		ArrayList<Reservation> reservations = new  ArrayList<Reservation>();
		
		for(Resource resource : resourceList)
		{
			List<Reservation> reservation = resource.getReservations();
			reservations.addAll(reservation);
		}
		
		return  Collections.unmodifiableList(reservations);
	}

}
