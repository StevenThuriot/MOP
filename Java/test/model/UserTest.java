package model;

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
	 */
	@Before
	public void setUp() {
		UserType type = new UserType("descr");
		user = new User("John", type);
	}

	/**
	 * Clearing all variables
	 */
	@After
	public void tearDown() {
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
		assertEquals("John", user.getDescription());
		assertEquals("John", user.toString());
	}
	
	/**
	 * Testing the setname
	 * @throws EmptyStringException
	 */
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
	
	@Test
	public void type()
	{
		assertEquals("descr", user.getType().getDescription());
		assertEquals("descr", user.getType().getTypeDescription());
		assertEquals("descr", user.getType().toString());
	}
}
