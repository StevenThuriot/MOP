package test;

import model.Project;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.EmptyStringException;
import static org.junit.Assert.*;

public class UserTest {

	/**
	 * User object used in testing
	 */
	private User user;
	
	/**
	 * Setting all variables to be used in tests
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		user = new User("John");
	}

	/**
	 * Clearing all variables
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		user = null;
	}
	
	/**
	 * Testing the constructor variable
	 * Does it set the name?
	 */
	@Test
	public void nameTest()
	{
		assertEquals("John", user.getName());
	}

	/**
	 * Can I create a project with an empty String?
	 * Expected: EmptyStringException
	 * @throws EmptyStringException
	 */
	@Test(expected=EmptyStringException.class)
	public void createEmptyProject() throws EmptyStringException
	{
		new Project(user, "");
	}
	
	/**
	 * Can I create a project with null as a description?
	 * Expected: EmptyStringException
	 * @throws EmptyStringException
	 */
	@Test(expected=NullPointerException.class)
	public void createNullProject() throws EmptyStringException, NullPointerException
	{
		new Project(user,null);
	}
	
	/**
	 * Are projects created correctly?
	 * @throws EmptyStringException
	 */
	@Test
	public void createProject() throws EmptyStringException
	{
		Project p = new Project(user,"Descr");
		assertTrue(user.getProjects().contains(p));
	}
	
	/**
	 * Are projects removed? And is the removed project the same as the created one?
	 * @throws EmptyStringException
	 */
	@Test
	public void removeProject() throws EmptyStringException
	{
		Project p = new Project(user,"Descr");
		
		p.remove();
		assertFalse(user.getProjects().contains(p));		
	}
	
}