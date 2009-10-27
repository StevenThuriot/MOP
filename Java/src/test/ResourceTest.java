package test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.GregorianCalendar;

import model.Resource;
import model.ResourceManager;
import model.ResourceType;
import model.Task;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.BusinessRule1Exception;
import exception.EmptyStringException;
import exception.ResourceBusyException;

public class ResourceTest {

	private Resource resource;
	private User user;
	
	@Before
	public void setUp() throws Exception {
		resource = ResourceManager.getInstance().createResource("Description",ResourceType.Room);
		user = new User("John");
	}

	@After
	public void tearDown() throws Exception {
		user = null;
		resource = null;
		//TODO: Verswijder alle resources uit ResourceManager
	}
	
	/**
	 * Should throw an exception when an empty description is given
	 * @throws EmptyStringException 
	 */
	@Test(expected=EmptyStringException.class)
	public void emptyDescription() throws EmptyStringException
	{
		resource = new Resource("", ResourceType.Room); 
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
	
	/**
	 * Create a resource via ResourceManager with empty description
	 * @throws EmptyStringException
	 */
	@Test(expected=EmptyStringException.class)
	public void createEmptyViaManager() throws EmptyStringException
	{
		ResourceManager.getInstance().createResource("", ResourceType.Room);
	}
	
	/**
	 * Create a resource successfully via the ResourceManager. Checks if it exisist
	 * @throws EmptyStringException
	 */
	@Test
	public void createViaManager() throws EmptyStringException
	{
		resource = ResourceManager.getInstance().createResource("ABC123", ResourceType.Room);
		assertTrue(ResourceManager.getInstance().findAll().contains(resource));
	}
	
	/**
	 * Check to see if a Resource is actually removed
	 * @throws ResourceBusyException 
	 */
	@Test
	public void removeResourceFromManager() throws ResourceBusyException
	{
		Resource rclone = resource.clone();
		ResourceManager.getInstance().remove(resource);
		assertTrue(ResourceManager.getInstance().findAll().contains(rclone));
	}

	/**
	 * Try to remove a resource that is required by a task
	 * Expected to throw a ResourceBusyException
	 * @throws ResourceBusyException
	 * @throws EmptyStringException 
	 * @throws BusinessRule1Exception 
	 */
	@Test(expected=ResourceBusyException.class)
	public void removeBusyResource() throws ResourceBusyException, EmptyStringException, BusinessRule1Exception
	{
		GregorianCalendar startDate = new GregorianCalendar(2009,10,1,12,00);
		GregorianCalendar endDate = new GregorianCalendar(2009,10,5,12,00);
		GregorianCalendar correctDuration = new GregorianCalendar();
		correctDuration.setTime(new Date((endDate.getTime().getTime() - startDate.getTime().getTime() - (1000 * 3600)))); //End - Begin - 1 hour
		Task task = new Task("Descr",user, startDate,endDate,1440);
		task.addRequiredResource(resource);
		
		ResourceManager.getInstance().remove(resource);
	}

}
