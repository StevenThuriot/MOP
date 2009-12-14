package model;

import model.Project;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.EmptyStringException;
import exception.IllegalStateCallException;
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


	
	@Test
	public void setName() throws EmptyStringException{
		//Null argument - exception should be thrown
		try {
			user.setName(null);
			fail();
		} catch (NullPointerException e){}
		
		// Empty description - exception should be thrown
		try{
			user.setName("");
			fail();
		} catch (EmptyStringException e){}
		
		// Valid name - name should be changed
		user.setName("Mario");
		assertTrue(user.getName().equals("Mario"));
	}
	
}
