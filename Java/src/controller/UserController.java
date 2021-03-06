package controller;

import java.util.ArrayList;
import java.util.List;

import model.Task;
import model.User;
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
	
	public List<UserType> getAllUserTypes()
	{
		return manager.getUserTypes();
	}

	public User createUser(UserType uType, String name) {
		User newUser = new User(name, uType);
		manager.add(newUser);
		return newUser;
	}
	
	public List<Task> getAllUnfinishedTasks(User user)
	{
		List<Task> tasks = new ArrayList<Task>();
		for(Task task : user.getTasks())
			if(task.isUnfinished())
				tasks.add(task);
		return tasks;
	}
}
