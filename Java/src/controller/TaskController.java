/**
 * 
 */
package controller;

import java.util.*;

import model.focus.FocusType;

import exception.ArrayLengthException;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.UnknownStateException;

import model.Resource;
import model.Task;
import model.TaskTimings;
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
	public Task createTask(String description,TaskTimings timings, 
			ArrayList<Task> dependencies, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		Task t = new Task(description, user, timings, dependencies, manager.getClock());

		return t;
	}
	
	public Task createTask(String description, TaskTimings timings, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		Task t = new Task(description, user, timings, manager.getClock());
		
		return t;
	}

	/**
	 * Let a task remove itself
	 * @param t
	 */
	public void removeTask(Task t){
		t.remove();
	}
	
	/**
	 * Let a task remove itself
	 * @param t
	 * @throws DependencyException
	 */
	public void removeTaskRecursively(Task t){
		t.removeRecursively();
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
	 * Change the current state to Succesful
	 * @param t the task to change
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 */
	public void setSuccessful(Task t) throws IllegalStateChangeException, BusinessRule2Exception, BusinessRule3Exception
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
	
	public void parseStateString(Task t, String state) throws IllegalStateChangeException, UnknownStateException, BusinessRule2Exception, BusinessRule3Exception
	{
		t.parseStateString(state);
	}
}
