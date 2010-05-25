/**
 * 
 */
package controller;

import java.util.*;

import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.UnknownStateException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;

import model.Field;
import model.Project;
import model.Task;
import model.TaskFactory;
import model.TaskTimings;
import model.TaskType;
import model.TaskTypeConstraint;
import model.User;
import model.UserType;
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
	 * Create a new task without dependencies
	 * @param type
	 * @param fields
	 * @param description
	 * @param owner
	 * @param timings
	 * @return
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws NullPointerException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	public Task createTask(String description, TaskType type, List<Field> fields, User owner, TaskTimings timings) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		return TaskFactory.createTask(description, type, fields, owner, timings, manager.getClock());
	}
	
	/**
	 * Create a new task with dependencies
	 * @param type
	 * @param fields
	 * @param description
	 * @param owner
	 * @param timings
	 * @param dependencies
	 * @return
	 * @throws WrongUserForTaskTypeException 
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws NullPointerException 
	 * @throws BusinessRule2Exception 
	 */
	@SuppressWarnings("unchecked")
	public Task createTask(String description, TaskType type, List<Field> fields, User owner, TaskTimings timings, ArrayList<Task> dependencies) throws NullPointerException, WrongFieldsForChosenTypeException, BusinessRule1Exception, DependencyCycleException, EmptyStringException, IllegalStateCallException, BusinessRule3Exception, WrongUserForTaskTypeException, BusinessRule2Exception 
	{
		return TaskFactory.createTask(description, type, fields, owner, timings, dependencies, manager.getClock());
	}

	/**
	 * Create and return a new Tasktype according to the given variables
	 * @param name
	 * @param fields
	 * @param constraints
	 * @return the newly created tasktype
	 */
	@SuppressWarnings("unchecked")
	public TaskType addTaskType(String name,ArrayList<Field> fields, ArrayList<TaskTypeConstraint> constraints, ArrayList<UserType> userTypes)
	{
		TaskType type = new TaskType(name, fields, constraints, userTypes);
		manager.add(type);
		return type;
	}
	
	/**
	 * Returns all existing tasktypes
	 * @return
	 */
	public List<TaskType> getAllTypes()
	{
		return manager.getTaskTypes();
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
	 * @throws BusinessRule2Exception 
	 */
	public void addDependency(Task task, Task dependency) throws BusinessRule1Exception, DependencyCycleException, IllegalStateCallException, BusinessRule2Exception
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
	
	/**
	 * Method that returns wether or not a Task requires resources
	 * @return
	 */
	public boolean hasRequiredAssets(Task taak)
	{
		if(taak.hasRequiredAssets())
			return true;
		return false;
	}
	
	public List<TaskTypeConstraint> getRequiredAssets(Task taak)
	{
		return taak.getRequiredResources();
	}
}
