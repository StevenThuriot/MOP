package model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Resource;
import model.ResourceType;
import model.Task;
import model.User;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.AssetAllocatedException;
import exception.BusinessRule1Exception;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.InvitationInvitesOwnerException;
import exception.NoReservationOverlapException;
import exception.NotAvailableException;
import exception.ResourceBusyException;

public class ResourceTest {

	private Resource resource;
	private ResourceType resourceType;
	private User user;
	private RepositoryManager manager;
	
	
	Task task1;
	
	@Before
	public void setUp() throws Exception {
		resourceType = new ResourceType("");
		resource = new Resource("Description",resourceType);
		user = new User("John",new UserType(""));
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		manager = new RepositoryManager();
		// 4 days to finish task
		task1 = new Task("Descr",user, new TaskTimings(new GregorianCalendar(),endDate,1440), manager.getClock());
		
	}

	@After
	public void tearDown() throws Exception {
		user = null;
		resource = null;
		task1 = null;
		manager = null;
	}
	
	/**
	 * Should throw an exception when an empty description is given
	 * @throws EmptyStringException 
	 */
	@Test(expected=EmptyStringException.class)
	public void emptyDescription() throws EmptyStringException
	{
		resource = new Resource("", new ResourceType("")); 
	}
	
	/**
	 * Should throw an exception when null description is given
	 * @throws EmptyStringException
	 */
	@Test(expected=NullPointerException.class)
	public void nullDescription() throws EmptyStringException, NullPointerException
	{
		resource = new Resource(null, null);
	}
	

	@Test
	public void doesEqualsWork() throws EmptyStringException
	{
		Resource res1 = new Resource("Room1",new ResourceType(""));
		Resource res2 = new Resource("room2",new ResourceType(""));
		assertFalse(res1.equals(res2));
		assertTrue(res1.equals(res1));
	}
	

	
	
	
	/**
	 * Tests the behavior of a newly made Resource object
	 */
	@Test
	public void initialization(){
		//Description should be "Description"
		assertEquals("Description", resource.getDescription());
		
		//Reservations and tasks using should be initialized as empty arraylists
		assertTrue(resource.getReservations().isEmpty());
	}
	
	/**
	 * Tests the behavior of reservations
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 */
	@Test(expected=NotAvailableException.class)
	public void reservations() throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException{
		
		GregorianCalendar startDate = new GregorianCalendar();
		new Reservation(startDate, 100, resource, task1);
		
		startDate.add(Calendar.MINUTE, 50);
		// Overlap - exception should be thrown
		new Reservation(startDate, 120, resource, task1);		
	}
	
	/**
	 * Tests the behavior of reservations
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws EmptyStringException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 */
	@Test(expected=NullPointerException.class)
	public void reservations2() throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException{
		new Reservation(new GregorianCalendar(), 100, null, task1);
	}
	
	/**
	 * Tests the behavior of reservations
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws EmptyStringException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 */
	@Test(expected=NullPointerException.class)
	public void reservations3() throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException{
		new Reservation(null, 100, resource, task1);
	}
	
	/**
	 * Tests the behavior of reservations
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws EmptyStringException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 */
	@Test
	public void reservations4() throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException{
		Reservation s = new Reservation(new GregorianCalendar(), 100, resource, task1);
		assertEquals(100, s.getDuration());
	}
	
	/**
	 * Tests the behavior of reservations
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws EmptyStringException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 */
	@Test
	public void reservations5() throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException{
		Reservation s = new Reservation(new GregorianCalendar(), 100, resource, task1);
		assertEquals(resource, s.getReservedResource());
	}
	
	/**
	 * Tests the behavior of reservations
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws EmptyStringException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 */
	@Test(expected=NullPointerException.class)
	public void reservations6() throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException{
		new Reservation(new GregorianCalendar(), 100, resource, null);
	}
	
	/**
	 * Tests the behavior of reservations
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws EmptyStringException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 */
	@Test
	public void reservations7() throws NotAvailableException, NoReservationOverlapException, AssetAllocatedException{
		Reservation s = new Reservation(new GregorianCalendar(), 100, resource, task1);
		assertEquals(task1, s.getTask());
	}
	
	@Test
	public void removeReservationTest() throws AssetAllocatedException, NotAvailableException, NoReservationOverlapException
	{
		Reservation reservation = new Reservation(new GregorianCalendar(),0,resource,task1);
		assertFalse(task1.getTaskAssetManager().getAssetAllocations().isEmpty());
		reservation.remove();
		assertTrue(task1.getTaskAssetManager().getAssetAllocations().isEmpty());
	}
    
    /**
     * Testing the toString method
     */
    @Test
    public void testToString()
    {
    	assertEquals("Description", resource.toString());
    }
    
    /**
     * Testing the getType method
     */
    @Test
    public void testGetType()
    {
    	assertEquals(resourceType, resource.getType());
    }
    
    /**
     * Testing the setType method
     */
    @Test(expected=NullPointerException.class)
    public void testSetType()
    {
    	resource.setType(null);
    }

}
