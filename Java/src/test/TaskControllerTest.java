package test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Resource;
import model.Task;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import controller.TaskController;
import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;


public class TaskControllerTest {

	private TaskController controller;
	private User user;
	@Before
	public void setUp()
	{
		controller = new TaskController();
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
	 */
	@Test
	public void createTask() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException
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
	 */
	@Test
	public void removeTask() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, DependencyException
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Beschrijving", new GregorianCalendar(), end, 120, new ArrayList<Task>(), new ArrayList<Resource>(), user);
		controller.removeTask(taak);
		assertFalse(controller.getTasks(user).contains(taak));
	}
}