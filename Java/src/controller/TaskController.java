/**
 * 
 */
package controller;

import java.util.*;

import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;

import model.Resource;
import model.Task;
import model.Status;
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
	 */
	public Task createTask(String description, GregorianCalendar startDate, 
			GregorianCalendar dueDate, int duration, 
			ArrayList<Task> dependencies, ArrayList<Resource> resources, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException
	{
		Task t = new Task(description, user, startDate, dueDate, duration, dependencies, resources);

		return t;
	}
	
	public Task createTask(String description, GregorianCalendar startDate, 
			GregorianCalendar dueDate, int duration, User user) 
	throws EmptyStringException, BusinessRule1Exception, DependencyCycleException
	{
		Task t = new Task(description, user, startDate, dueDate, duration);
		
		return t;
	}

	/**
	 * Let a task remove itself
	 * @param t
	 * @throws DependencyException
	 */
	public void removeTask(Task t) /*throws DependencyException*/ {
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
	 * Update a tasks status
	 * @param t
	 * @param status
	 * @throws DependencyException
	 */
	public void updateTaskStatus(Task t, Status status) throws DependencyException {
		t.updateTaskStatus(status);
	}

	/**
	 * Update the status of the dependant tasks.
	 * @param task
	 */
	public void updateDependantTasks(Task task, Status status){
		task.updateTaskStatusRecursively(status);	
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
	 * Get the resources this task requiers
	 * @param task
	 * @return
	 */
	public List<Resource> getRequiredResources(Task task){
		return task.getRequiredResources();
	}
	
	/**
	 * Show all the tasks for today. If there aren't any, show the first 10 to come.
	 * @param user
	 * @return
	 */
	public List<Task> focusWork(User user)
	{
		ArrayList<Task> focus = new ArrayList<Task>();
		ArrayList<Task> list = new ArrayList<Task>(); 
		list.addAll(this.getTasks(user));
		
		GregorianCalendar today = new GregorianCalendar();
		Comparator<Task> taskComparator = new Comparator<Task>() {
			
			@Override
			public int compare(Task t1, Task t2) {
				return t1.getDueDate().compareTo(t2.getDueDate());
			}
		};
		
		for (Task task : list)
		{
			GregorianCalendar due = task.getDueDate();
			if (due.get(GregorianCalendar.DAY_OF_MONTH) == today.get(GregorianCalendar.DAY_OF_MONTH)
					&& due.get(GregorianCalendar.MONTH) == today.get(GregorianCalendar.MONTH)
					&& due.get(GregorianCalendar.YEAR) == today.get(GregorianCalendar.YEAR))
			{
				focus.add(task);
			}
		}
		Collections.sort(focus, taskComparator);
		if (focus.size() == 0)
		{	Collections.sort(list, taskComparator);
			
			int size = list.size();
			if (size > 10)
			{
				size = 10;
			}
			
			for (int i = 0; i < size; i++) {
				focus.add(list.get(i));
			}
		}
		return Collections.unmodifiableList(focus);		
	}
}
