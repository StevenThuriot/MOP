package model;

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
import exception.TaskFailedException;

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
	 * Tests the initialization behavior of Task.
	 * @throws EmptyStringException 
	 */
	@Test
	public void initialization() throws EmptyStringException{
		// <task> is initialized with no required resources
		assertTrue(task.getRequiredResources().isEmpty());
		// <task> is initialized with no dependencies or depending tasks
		assertTrue(task.getDependencies().isEmpty());
		assertTrue(task.getDependentTasks().isEmpty());
		// <task> is linked to the user John, and is given the name "Descr"
		assertTrue(task.getUser() == user);
		assertTrue(task.getDescription() == "Descr");
		// <task> should satisfy the business rules
		assertTrue(task.satisfiesBusinessRule1());
		// TODO: other BR's here?
		
		//Try a different task, that fails business rule 1
		startDate = new GregorianCalendar(2009,10,1,12,00);
		endDate = new GregorianCalendar(2009,10,2,12,00);
		//One day is available, but the duration is 25 hours. Business Rule exception should be thrown.
		//Task "MOP" can never be completed in time :>
		try {
			Task task2 = new Task("MOP", user, startDate, endDate, 1500);
			fail();
			} catch (BusinessRule1Exception e) {/*Success*/}		
			
	}

	
	/**
	 * Testing status with scenario:
	 * Status variable = unfinished
	 * Resource = available
	 * No dependent tasks
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
	 * Dependencies: 1 Successful
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
	
	/**
	 * Tests on dependencies. Tests a proper dependency
	 */
	@Test
	public void dependencies1() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, DependencyException{
		// Try a proper dependency
		Task task2 = new Task("some name", user, startDate, endDate, 50);
		task.addDependency(task2);
		// Assure that it is properly initialized
		// For more details, see TaskDependencyManager and corresponding test class
		assertTrue(task.dependsOn(task2));
		assertTrue(task2.getDependentTasks().contains(task));
		//Remove it again
		task.removeDependency(task2);
		assertFalse(task.dependsOn(task2));
		assertFalse(task2.getDependentTasks().contains(task2));
	}
	
	/**
	 * Another test on dependencies. Tests a dependency that fails to satisfy business rule 1.
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 */
	@Test
	public void dependencies2() throws DependencyCycleException, EmptyStringException, BusinessRule1Exception{
		startDate = new GregorianCalendar(2009,10,4,12,00);
		endDate = new GregorianCalendar(2009,10,8,12,00);
		Task task2 = new Task("some name", user, startDate, endDate, 1380);
		// <task2> starts on the 4th and takes 23 hours. <task> takes another 2 hours
		// This dependency will not satisfy business rule 1
		try {
			task.addDependency(task2);
			fail();
		} catch (BusinessRule1Exception e) {/*Success*/}		
	}
	
	/**
	 * Tests the behavior of Task.earliestEndTime()
	 * @throws TaskFailedException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 */
	@Test
	public void earliestEnd() throws TaskFailedException, EmptyStringException, BusinessRule1Exception, DependencyCycleException{
		//<task> takes two hours to complete, earliest end time is 2 hours after the start date
		assertEquals(new GregorianCalendar(2009,10,1,14,00), task.earliestEndTime());
		
		startDate = new GregorianCalendar(2009,10,2,12,00);
		endDate = new GregorianCalendar(2009,10,8,12,00);
		Task task2 = new Task("some name", user, startDate, endDate, 1440);
		task.addDependency(task2);
		//<task2> takes 24 hours to complete, <task> takes another 2.
		// Earliest end time should be 26 hours after the startDate of <task2>
		assertEquals(new GregorianCalendar(2009, 10, 3, 14,00), task.earliestEndTime());		
	}
	
	/**
	 * Tests the behavior of the non-recursive remove method.
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 */
	@Test
	public void remove() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException{
		//Sets up a required resource, and a dependency in both directions
		task.addRequiredResource(resource);
		Task task2 = new Task("some dependency",user,startDate,endDate,120);
		Task task3 = new Task("some dependentTask",user,startDate,endDate,120);
		task.addDependency(task2);
		task3.addDependency(task);
		// remove the task
		task.remove();
		//Assures all links are broken
		assertFalse(task3.getDependencies().contains(task));
		assertFalse(task2.getDependentTasks().contains(task));
		assertFalse(user.getTasks().contains(task));
		assertFalse(resource.getTasksUsing().contains(task));
	}
	
	/**
	 * Tests the behavior of the recursive remove method.
	 * @throws EmptyStringException 
	 * @throws BusinessRule1Exception 
	 * @throws DependencyCycleException 
	 */
	@Test
	public void removeRecursively() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException{
		//Sets up 2 additional resources
		Resource resource2 = new Resource("some resource",ResourceType.Tool);
		Resource resource3 = new Resource("some other resource",ResourceType.Tool);
		//Sets up 3 additional tasks
		Task task2 = new Task("some dependency",user,startDate,endDate,120);
		task2.addRequiredResource(resource2);
		Task task3 = new Task("some dependentTask",user,startDate,endDate,120);
		task3.addRequiredResource(resource3);
		Task task4 = new Task("some other task",user, startDate, endDate,120);
		task3.addDependency(task4);
		//Sets up a hierarchy of 3 levels
		task3.addDependency(task2);
		task2.addDependency(task);
		//Removes <task> recursively
		task.removeRecursively();
		//Checks that the user has no link to any of the first 3 tasks anymore
		assertFalse(user.getTasks().contains(task));
		assertFalse(user.getTasks().contains(task2));
		assertFalse(user.getTasks().contains(task3));
		//Check that <task4> is not deleted
		assertTrue(user.getTasks().contains(task4));
		//Check that the resource are no longer required, and that task4 has no reference to task3 anymore
		assertFalse(resource.getTasksUsing().contains(task));
		assertFalse(resource2.getTasksUsing().contains(task2));
		assertFalse(resource3.getTasksUsing().contains(task3));
		assertFalse(task4.getDependentTasks().contains(task3));
	}

	
}
