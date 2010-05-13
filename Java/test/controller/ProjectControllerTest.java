package controller;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Project;
import model.TaskTimings;
import model.User;
import model.UserType;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import controller.ProjectController;
import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;


public class ProjectControllerTest {

	/**
	 * The controller we will be testing
	 */
	public ProjectController controller;
	
	private RepositoryManager manager;
	
	@Before
	public void setUp()
	{
	    manager = new RepositoryManager();
		controller = new ProjectController(manager);
	}
	
	@After
	public void tearDown()
	{
		controller = null;
	}
	
	/**
	 * Will test and create a project with an empty description
	 * Expected to throw an EmptyStringException
	 * @throws EmptyStringException
	 */
	@Test(expected=EmptyStringException.class)
	public void createFaultyProject() throws EmptyStringException
	{
		controller.createProject("");
	}
	
	/**
	 * Test for nullpointerexception on instantiating the controller with null as manager
	 */
	@Test(expected=NullPointerException.class)
	public void createNullController()
	{
	    controller = new ProjectController(null);
	}
	
	/**
	 * Create a project with a normal name.
	 * @throws EmptyStringException
	 */
	@Test
	public void createProject() throws EmptyStringException
	{
		Project p = controller.createProject("Project A");
		assertEquals(p,controller.getProjects().get(0));
	}
	
	/**
	 * Remove a project
	 * Warning: Test relies on correct execution of ProjectController.createProject
	 * @throws EmptyStringException
	 * @throws IllegalStateCallException 
	 */
	@Test
	public void removeProject() throws EmptyStringException, IllegalStateCallException
	{
		Project p = controller.createProject("Project B");
		controller.removeProject(p);
		assertFalse(controller.getProjects().contains(p));
	}
	
	/**
	 * Remove a null project
	 * @throws NullPointerException
	 * @throws IllegalStateCallException 
	 */
	@Test(expected=NullPointerException.class)
	public void removeNullProject() throws NullPointerException, IllegalStateCallException
	{
		controller.removeProject(null);
	}
	
	/**
	 * Tests the bindtask method
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 */
	@Test
	public void testBind() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
	    Project p = controller.createProject("Project A");
	    TaskController taskController = new TaskController(manager);
	    GregorianCalendar end = new GregorianCalendar();
	    end.add(Calendar.MONTH, 1);
	    controller.bind(p,taskController.createTask("Descr", new TaskTimings(new GregorianCalendar(), end, 120), new User("Bart",new UserType(""))));
	    assertTrue(!p.getTasks().isEmpty());
	}
}
