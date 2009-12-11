package controller;

import model.Project;
import model.User;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import controller.ProjectController;
import exception.EmptyStringException;


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
	 */
	@Test
	public void removeProject() throws EmptyStringException
	{
		Project p = controller.createProject("Project B",user);
		controller.removeProject(p);
		assertFalse(controller.getProjects(user).contains(p));
	}
	
	/**
	 * Remove a null project
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void removeNullProject() throws NullPointerException
	{
		controller.removeProject(null);
	}
}
