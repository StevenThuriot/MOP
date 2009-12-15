/**
 * 
 */
package controller;

import java.util.*;

import controller.FocusFactory.FocusType;

import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.UnknownStateException;

import model.Resource;
import model.Task;
import model.User;
import model.repositories.RepositoryManager;

/**
 * Controller to interact with tasks.
 */
public class TaskController {
	
	/**
	 * The repository manager of the system.
	 */
	private RepositoryManager manager;
	/**
     * Constructor that takes a RepositoryManager as argument. Will throw NullPointerException if the latter was null.
     * @param manager
     */
    public TaskController(RepositoryManager manager)
    {
        if(manager==null)
            throw new NullPointerException();
        this.manager = manager;
    }
	/**
	 * Create a new task
	 * @param description
	 * @param startDate
	 * @param dueDate
	 * @param duration
	 * @param dependencies
	 * @param resources
	 * @param user
	 * @return
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 */
	public Task createTask(String description, GregorianCalendar startDate, 
			GregorianCalendar dueDate, int duration, 
			ArrayList<Task> dependencies, ArrayList<Resource> resources, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		Task t = new Task(description, user, startDate, dueDate, duration, dependencies, resources, manager.getClock());

		return t;
	}
	
	public Task createTask(String description, GregorianCalendar startDate, 
			GregorianCalendar dueDate, int duration, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		Task t = new Task(description, user, startDate, dueDate, duration, manager.getClock());
		
		return t;
	}

	/**
	 * Let a task remove itself
	 * @param t
	 * @throws IllegalStateCallException 
	 * @throws DependencyException
	 */
	public void removeTask(Task t) throws IllegalStateCallException{
		t.remove();
	}
	
	/**
	 * Get a users tasks
	 * @param user
	 * @return
	 */
	public List<Task> getTasks(User user) {
		return user.getTasks();
	}
	
	/**
	 * Get the dependent task of task t.
	 * @param t 
	 * @return
	 */
	public List<Task> getDependentTasks(Task t) {
		return t.getDependentTasks();
	}
	
	public Boolean hasDependentTasks(Task task)
	{
		int size = task.getDependentTasks().size();
		
		if (size == 0)
			return false;
		else
			return true;
	}
	
	public Boolean hasDependencies(Task task)
	{
		int size = task.getDependencies().size();
		
		if (size == 0)
			return false;
		else
			return true;
	}
	
	public Boolean hasRequiredResources(Task task)
	{
		int size = task.getRequiredResources().size();
		
		if (size == 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Get the tasks this task is dependent on.
	 * @param task
	 * @return
	 */
	public List<Task> getDependencies(Task task)
	{
		return task.getDependencies();
	}
	
	
	/**
	 * Add dependency to task
	 * @param task
	 * @param dependency
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws IllegalStateCallException 
	 */
	public void addDependency(Task task, Task dependency) throws BusinessRule1Exception, DependencyCycleException, IllegalStateCallException
	{
		task.addDependency(dependency);
	}
	
	/**
	 * Remove dependency from task
	 * @param task
	 * @param dependency
	 * @throws DependencyException
	 * @throws IllegalStateCallException 
	 */
	public void removeDependency(Task task, Task dependency) throws DependencyException, IllegalStateCallException{
		task.removeDependency(dependency);
	}
	
	/**
	 * Add required resource to task
	 * @param task
	 * @param resource
	 * @throws IllegalStateCallException 
	 */
	public void addRequiredResource(Task task, Resource resource) throws IllegalStateCallException{
		task.addRequiredResource(resource);
	}
	
	/**
	 * Set new task description
	 * @param task
	 * @param description
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws IllegalStateCallException 
	 */
	public void setTaskDescription(Task task, String description) throws NullPointerException, EmptyStringException, IllegalStateCallException{
		task.setDescription(description);
	}
	
	/**
	 * Set task schedule
	 * @param task
	 * @param startDate
	 * @param dueDate
	 * @param duration
	 * @throws BusinessRule1Exception
	 * @throws BusinessRule3Exception 
	 * @throws NullPointerException 
	 */
	public void setTaskSchedule(Task task, GregorianCalendar startDate, GregorianCalendar dueDate, int duration) throws BusinessRule1Exception, NullPointerException, BusinessRule3Exception{
		task.updateTaskTiming(startDate, dueDate, duration);
	}
	
	/**
	 * Get the resources this task requires
	 * @param task
	 * @return
	 */
	public List<Resource> getRequiredResources(Task task){
		return task.getRequiredResources();
	}
	
	/**
	 * Shows all the tasks according to a certain FocusType strategy.
	 * @param user
	 * @return
	 */
	public List<Task> focusWork(User user,FocusType type,int var1,int var2)
	{
		return FocusFactory.createFocus(type, user, var1, var2).getTasks();
		
	}
	
	/**
	 * Change the current state to Succesful
	 * @param t the task to change
	 * @throws IllegalStateChangeException
	 */
	public void setSuccessful(Task t) throws IllegalStateChangeException
	{
		t.setSuccessful();
	}
	
	/**
	 * Change the current state to Failed
	 * @param t the task to change
	 * @throws IllegalStateChangeException
	 */
	public void setFailed(Task t) throws IllegalStateChangeException
	{
		t.setFailed();
	}
	
	public void parseStateString(Task t, String state) throws IllegalStateChangeException, UnknownStateException
	{
		t.parseStateString(state);
	}
}
