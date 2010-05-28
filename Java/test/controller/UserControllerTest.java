package controller;
import static org.junit.Assert.*;

import model.User;
import model.UserType;
import model.repositories.RepositoryManager;

import org.junit.Before;
import org.junit.Test;

public class UserControllerTest {
	private DispatchController dController;
	private RepositoryManager manager;
	@Before
	public void setUp()
	{
		manager = new RepositoryManager();
		dController = new DispatchController(manager);
	}
	
	@Test
	public void testGetTypes()
	{
		dController.getUserController().createUserType("Type 1");
		assertEquals(1, dController.getUserController().getAllUserTypes().size());
		UserType type2 = dController.getUserController().createUserType("Type 2");
		assertEquals(2, dController.getUserController().getAllUserTypes().size());
		assertTrue(dController.getUserController().getAllUserTypes().contains(type2));
	}
	
	@Test
	public void testGetUsers()
	{
		UserType type2 = dController.getUserController().createUserType("Type 2");
		User user = dController.getUserController().createUser(type2, "Bart");
		assertTrue(manager.getUsers().contains(user));
	}
}
