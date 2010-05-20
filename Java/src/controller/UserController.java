package controller;

import model.UserType;
import model.repositories.RepositoryManager;

public class UserController {
	private RepositoryManager manager;
	
	public UserController(RepositoryManager manager)
	{
		this.manager = manager;
	}
	
	/**
	 * Create and place a new user type
	 * @param id
	 * @param description
	 * @return
	 */
	public UserType createUserType(String description)
	{
		UserType type = new UserType(description);
		manager.add(type);
		return type;
	}
}
