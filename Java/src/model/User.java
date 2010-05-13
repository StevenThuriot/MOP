package model;

import java.util.Collections;
import java.util.List;
import gui.Describable;

import exception.EmptyStringException;

public class User implements Asset,Describable{
	
	/**
	 * A String that describes the name of the user.
	 */
	private String name;
	
	/**
	 * An ArrayList keeping track of all the tasks of this user.
	 * @invar	Every Task in <userTasks> must have this user as its responsible.
	 * 			| for every task in userTasks:
	 * 			| 	task.getUser() == this
	 */
	private UserTaskManager taskManager;
	
	/**
	 * This user's type
	 */
	private UserType type;

	/**
	 * Creates a new user, with no tasks.
	 * @param 	name
	 * 			The name of this user.
	 */
	public User(String name, UserType uType)
	{
		this.name = name;
		this.type = uType;
		taskManager = new UserTaskManager(this);
	}
	
	/**
	 * Returns the name of this user.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a description of this user.
	 */
	public String getDescription() {
		return getName();
	}
	
	/**
	 * Returns a String representation of this user. At the moment,
	 * this returns the user's name.
	 */
	public String toString(){
		return getName();
	}

	/**
	 * Sets <name> to be the new name of this user.
	 * @param	name
	 * @post	|new.getName() == name
	 */
	public void setName(String name) throws NullPointerException, EmptyStringException {
		if (name == null)
			throw new NullPointerException("Null was passed");
		
		if (name.length() == 0)
			throw new EmptyStringException("A valid name is needed");
	
		this.name = name;
	}


	/**
	 * Removes a task from the list of tasks from this user.
	 * This method is invoked whenever Task.remove() is called and
	 * should not be used directly.
	 */
	protected void removeTask(Task task){
		taskManager.remove(task);
	}
	
	/**
	 * Adds a task to the list of tasks from this user.
	 * This method is invoked whenever a task is created and should not be used
	 * directly.
	 */
	protected void addTask(Task task){
		taskManager.add(task);
	}
	

	/**
	 * Returns all the tasks that this user is responsible for.
	 */
	public List<Task> getTasks(){
		return  Collections.unmodifiableList(taskManager.getOwnedTasks());
	}
	
	/**
	 * Add an invitation to the user
	 * @param invitation
	 */
	public void addInvitation(Invitation invitation) {
		this.taskManager.add(invitation);
	}

	/**
	 * Remove an invitation from the user
	 * @param invitation
	 */
	public void removeInvitation(Invitation invitation) {
		this.taskManager.remove(invitation);
	}

	/**
	 * Retrieve the user's task manager
	 * @return
	 */
	protected UserTaskManager getUserTaskManager() {
		return taskManager;
	}

	public List<Invitation> getInvitations() {
		return taskManager.getInvitations();
	}

	@Override
	public UserType getType() {
		return type;
	}
	
}
