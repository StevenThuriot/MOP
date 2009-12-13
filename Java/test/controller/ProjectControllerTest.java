package controller;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Project;
import model.User;
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
import exception.IllegalStateCall;


public class ProjectControllerTest {

	/**
	 * The controller we will be testing
	 */
	public ProjectController controller;
	/**
	 * User of the system. This is normally passed through the DispatchController
	 */
	public User user;
	
	@Before
	public void setUp()
	{
	    RepositoryManager manager = new RepositoryManager();
		controller = new ProjectController(manager);
		user = new User("John");
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
		controller.createProject("", user);
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
		Project p = controller.createProject("Project A",user);
		assertEquals(p,controller.getProjects(user).get(0));
	}
	
	/**
	 * Remove a project
	 * Warning: Test relies on correct execution of ProjectController.createProject
	 * @throws EmptyStringException
	 * @throws IllegalStateCall 
	 */
	@Test
	public void removeProject() throws EmptyStringException, IllegalStateCall
	{
		Project p = controller.createProject("Project B",user);
		controller.removeProject(p);
		assertFalse(controller.getProjects(user).contains(p));
	}
	
	/**
	 * Remove a null project
	 * @throws NullPointerException
	 * @throws IllegalStateCall 
	 */
	@Test(expected=NullPointerException.class)
	public void removeNullProject() throws NullPointerException, IllegalStateCall
	{
		controller.removeProject(null);
	}
	
	/**
	 * Tests the bindtask method
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws IllegalStateCall 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 */
	@Test
	public void testBind() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCall, BusinessRule3Exception
	{
	    Project p = controller.createProject("Project A",user);
	    TaskController taskController = new TaskController();
	    GregorianCalendar end = new GregorianCalendar();
	    end.add(Calendar.MONTH, 1);
	    controller.bind(p,taskController.createTask("Descr", new GregorianCalendar(), end, 120, new User("Bart")));
	    assertTrue(!p.getTasks().isEmpty());
	}
}
