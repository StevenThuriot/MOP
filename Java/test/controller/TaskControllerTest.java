package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Resource;
import model.Task;
import model.User;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import controller.TaskController;
import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;


public class TaskControllerTest {

	private TaskController controller;
	private User user;
	private RepositoryManager manager;
	@Before
	public void setUp()
	{
		manager = new RepositoryManager();
		controller = new TaskController(manager);
		user = new User("John");
	}
	
	@After
	public void tearDown()
	{
		controller = null;
	}
	
	/**
	 * TODO: Duration
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 */
	@Test
	public void createTask() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Beschrijving", new GregorianCalendar(), end, 120, new ArrayList<Task>(), new ArrayList<Resource>(), user);
		assertTrue(controller.getTasks(user).contains(taak));
	}
	
	/**
	 * Test to see if tasks are removed
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws DependencyException
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 */
	@Test
	public void removeTask() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, DependencyException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Beschrijving", new GregorianCalendar(), end, 120, new ArrayList<Task>(), new ArrayList<Resource>(), user);
		controller.removeTask(taak);
		assertFalse(controller.getTasks(user).contains(taak));
	}
	
	/**
	 * Test to see if the controller's constructor checks for null
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws NullPointerException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 */
	@Test(expected=NullPointerException.class)
	public void createController() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		@SuppressWarnings("unused")
		TaskController test = new TaskController(null);
	}
}