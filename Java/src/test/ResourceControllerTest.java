package test;


import java.util.GregorianCalendar;

import model.Reservation;
import model.Resource;
import model.ResourceManager;
import model.ResourceType;
import model.User;

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
	
	@Before
	public void setUp() throws Exception {
		controller = new ResourceController();
		resource = new Resource("Room 101", ResourceType.Room);
		user = new User("John");
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
		assertTrue(ResourceManager.getInstance().findAll().contains(resource));
	}
	
	public void removeResource() throws EmptyStringException, ResourceBusyException
	{
		resource = controller.createResource("Room 101", ResourceType.Room);
		ResourceManager.getInstance().remove(resource);
		assertFalse(ResourceManager.getInstance().findAll().contains(resource));
	}
}