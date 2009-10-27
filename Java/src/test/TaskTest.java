package test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import model.Project;
import model.Resource;
import model.ResourceType;
import model.Status;
import model.Task;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.NotAvailableException;

import static org.junit.Assert.*;


public class TaskTest {
	/**
	 * Task to be tested
	 */
	private Task task;
	
	/**
	 * StartDate to be used in the Task objects
	 */
	private GregorianCalendar startDate;
	
	/**
	 * EndDate to be used in the Task objects
	 */
	private GregorianCalendar endDate;
	
	/**
	 * Correct Duration according to endDate - startDate & Business Rule 1
	 */
	private GregorianCalendar correctDuration;
	
	/**
	 * Resource to be added to the Task
	 */
	private Resource resource;
	private User user;

	/**
	 * Setting up the test.
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 */
	@Before
	public void setUp() throws BusinessRule1Exception, DependencyCycleException, EmptyStringException
	{
		user = new User("John");
		startDate = new GregorianCalendar(2009,10,1,12,00);
		endDate = new GregorianCalendar(2009,10,5,12,00);
		correctDuration = new GregorianCalendar();
		correctDuration.setTime(new Date((endDate.getTime().getTime() - startDate.getTime().getTime() - (1000 * 3600)))); //End - Begin - 1 hour
		resource = new Resource("Projector", ResourceType.Tool);
		task = new Task("Descr",user,startDate,endDate,120);
			
	}
	
	/**
	 * Tearing down variables after each test
	 */
	@After
	public void tearDown()
	{
		user = null;
		task = null;
		startDate = null;
		endDate = null;
		correctDuration = null;
	}
	
	/**
	 * Resources should be instantiated as an empty list
	 */
	@Test
	public void getResourceList()
	{
		assertEquals(new ArrayList<Resource>(), task.getRequiredResources());
		
	}
	
	/**
	 * Dependent tasks should be instantiated as an empty list 
	 */
	@Test
	public void getTaskList()
	{
		assertEquals(new ArrayList<Task>(), task.getDependencies());
	}
	
	/**
	 * Testing status with scenario:
	 * Status variable = unfinished
	 * Resource = available
	 * No dependant tasks
	 * Expected outcome: Available
	 * @throws DependencyException 
	 */
	@Test
	public void checkStatusOne() throws DependencyException
	{
		try {
			task.updateTaskStatus(Status.Unfinished);
		} finally {
			assertEquals(Status.Available, task.getStatus());
		}		
	}
	
	/**
	 * Testing status with scenario:
	 * Status variable = unfinished
	 * No dependent tasks
	 * Resource = unavailable
	 * Expected outcome: Unavailable
	 * @throws NotAvailableException 
	 * @throws DependencyException 
	 */
	@Test
	public void checkStatusTwo() throws NotAvailableException, DependencyException
	{
		task.addRequiredResource(resource);
		
		task.updateTaskStatus(Status.Unfinished);
		GregorianCalendar futuretime = new GregorianCalendar();
		futuretime.add(Calendar.DAY_OF_MONTH, 1);
		resource.createReservation(new GregorianCalendar(),120,user);
		assertEquals(Status.Unavailable, task.getStatus());
	}
	
	/**
	 * Testing status with scenario:
	 * Status variable = unfinished
	 * No dependent tasks
	 * Resource = available
	 * Current time <> schedule of the task
	 * Expected outcome: Available
	 */
	@Test
	public void checkStatusThree()
	{
		
	}
	
	/**
	 * Testing status with scenario:
	 * Status variable: Unfinished
	 * Resource: Available
	 * Dependencies: 1 Succesful
	 * Expected outcome: Available
	 */
	@Test
	public void checkStatusFour()
	{
		
	}
	/**
	 * Test to see if description is set properly
	 * @throws EmptyStringException
	 */
	@Test
	public void updateTaskDescription() throws EmptyStringException
	{
		task.setDescription("ABC123");
		assertEquals("ABC123", task.getDescription());
	}
	
	/**
	 * Testing a task attaching to a project
	 * @throws EmptyStringException 
	 */
	@Test
	public void assignTaskProject() throws EmptyStringException
	{
		Project p = new Project(user, "Proj");
		p.bindTask(task);
		
		assertTrue(p.getTasks().contains(task));
	}
	
}
