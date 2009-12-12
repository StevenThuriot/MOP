package controller;


import java.util.GregorianCalendar;

import model.Reservation;
import model.Resource;
import model.ResourceType;
import model.User;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import controller.ResourceController;
import exception.EmptyStringException;
import exception.NotAvailableException;
import exception.ResourceBusyException;

public class ResourceControllerTest {

	private ResourceController controller;
	private Resource resource;
	private User user;
	private RepositoryManager manager;
	
	
	@Before
	public void setUp() throws Exception {
		manager = new RepositoryManager();
		controller = new ResourceController(manager);
		resource = new Resource("Room 101", ResourceType.Room);
		user = new User("John");
	}
	
	/**
     * Test for nullpointerexception on instantiating the controller with null as manager
     */
    @Test(expected=NullPointerException.class)
    public void createNullController()
    {
        controller = new ResourceController(null);
    }

	@After
	public void tearDown() throws Exception {
		controller = null;
		resource = null;
		user = null;
	}
	
	/**
	 * Create a correct reservation
	 * TODO: Duration aanpassen
	 * @throws NotAvailableException 
	 */
	@Test
	public void createReservation() throws NotAvailableException
	
	{
		GregorianCalendar begin = new GregorianCalendar();
		Reservation reservatie = controller.createReservation(begin, 140, resource, user);
		assertEquals(reservatie, resource.getReservations().get(1));
	}
	
	/**
	 * Create an overlapping reservation. Should throw a NotAvailableException
	 * @throws NotAvailableException
	 */
	@Test(expected=NotAvailableException.class)
	public void createNoReservation() throws NotAvailableException
	{
		controller.createReservation(new GregorianCalendar(), 140, resource, user);
		controller.createReservation(new GregorianCalendar(), 30, resource, user);
	}
	
	/**
	 * Create a resource and look it up in the ResourceManager
	 * @throws EmptyStringException
	 */
	@Test
	public void createResource() throws EmptyStringException
	{
		resource = controller.createResource("Room 101", ResourceType.Room);
	    assertTrue(manager.getResources().contains(resource));
	}
	
	/**
	 * Does a resource get removed properly?
	 * Created and removed through the controller, checks existence in the RepositoryManager
	 * @throws EmptyStringException
	 * @throws ResourceBusyException
	 */
	@Test
	public void removeResource() throws EmptyStringException, ResourceBusyException
	{
		resource = controller.createResource("Room 101", ResourceType.Room);
		controller.removeResource(resource);
		assertFalse(manager.getResources().contains(resource));
	}
	
	/**
	 * Test the existence of a created resource throught the controller
	 * @throws EmptyStringException
	 */
	@Test
	public void testGetResources() throws EmptyStringException
	{
	    resource = controller.createResource("Room 101", ResourceType.Room);
	    assertTrue(controller.getResources().contains(resource));
	}
	
	/**
	 * Create a resource, create a reservation on that resource
	 * Get a general list of reservations, see of the reservation contains the one we created
	 * @throws NotAvailableException 
	 * @throws EmptyStringException 
	 */
	@Test
	public void testReservations() throws NotAvailableException, EmptyStringException
	{
	    resource = controller.createResource("Room 101", ResourceType.Room);
	    Reservation reservation = controller.createReservation(new GregorianCalendar(), 101, resource, new User("Bart"));
	    assertTrue(controller.getReservations().contains(reservation));
	}
}
