/**
 * 
 */
package controller;

import java.util.*;

import controller.FocusFactory.FocusType;

import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCall;
import exception.IllegalStateChangeException;

import model.Resource;
import model.Task;
import model.User;

/**
 * Controller to interact with tasks.
 */
public class TaskController {
	
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
	 * @throws IllegalStateCall 
	 * @throws NullPointerException 
	 */
	public Task createTask(String description, GregorianCalendar startDate, 
			GregorianCalendar dueDate, int duration, 
			ArrayList<Task> dependencies, ArrayList<Resource> resources, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCall
	{
		Task t = new Task(description, user, startDate, dueDate, duration, dependencies, resources);

		return t;
	}
	
	public Task createTask(String description, GregorianCalendar startDate, 
			GregorianCalendar dueDate, int duration, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCall
	{
		Task t = new Task(description, user, startDate, dueDate, duration);
		
		return t;
	}

	/**
	 * Let a task remove itself
	 * @param t
	 * @throws DependencyException
	 */
	public void removeTask(Task t){
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
	 * Update a task fully
	 * @param newTask
	 * @param oldTask
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 */
	public void updateTask(Task newTask, Task oldTask) throws EmptyStringException, BusinessRule1Exception {
		oldTask.clone(newTask);
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
	 */
	public void addDependency(Task task, Task dependency) throws BusinessRule1Exception, DependencyCycleException
	{
		task.addDependency(dependency);
	}
	
	/**
	 * Remove dependency from task
	 * @param task
	 * @param dependency
	 * @throws DependencyException
	 */
	public void removeDependency(Task task, Task dependency) throws DependencyException{
		task.removeDependency(dependency);
	}
	
	/**
	 * Add required resource to task
	 * @param task
	 * @param resource
	 */
	public void addRequiredResource(Task task, Resource resource){
		task.addRequiredResource(resource);
	}
	
	/**
	 * Set new task description
	 * @param task
	 * @param description
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws IllegalStateCall 
	 */
	public void setTaskDescription(Task task, String description) throws NullPointerException, EmptyStringException, IllegalStateCall{
		task.setDescription(description);
	}
	
	/**
	 * Set task schedule
	 * @param task
	 * @param startDate
	 * @param dueDate
	 * @param duration
	 * @throws BusinessRule1Exception
	 */
	public void setTaskSchedule(Task task, GregorianCalendar startDate, GregorianCalendar dueDate, int duration) throws BusinessRule1Exception{
		task.updateTaskTiming(startDate, dueDate, duration);
	}
	
	/**
	 * Get the resources this task requiers
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
	 * Change the current state to Unfinished
	 * @param t the task to change
	 * @throws IllegalStateChangeException
	 */
	public void setUnfinished(Task t) throws IllegalStateChangeException
	{
		t.setUnfinished();
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
}
