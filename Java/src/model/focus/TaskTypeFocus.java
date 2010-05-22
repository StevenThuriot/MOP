package model.focus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Task;
import model.TaskDeadlineComparator;
import model.TaskType;
import exception.ArrayLengthException;

public class TaskTypeFocus extends FocusStrategy {
	private int amount;
	private TaskType taskType;
	public TaskTypeFocus(Object[] options) throws ArrayLengthException
	{
		if(options.length==2){
			this.amount=(Integer)options[0];
			this.taskType = (TaskType) options[1];
		}else
			throw new ArrayLengthException();
	}
	
	/**
	 * Sort the given list of tasks by using a Comparator that compares deadlines.
	 */
	protected List<Task> sort(List<Task> tasks)
	{
		Collections.sort(tasks,new TaskDeadlineComparator());
		return tasks;
	}
	/**
	 * Method that returns only a few tasks out of a list. This 'few' is defined by the given tasktype
	 * @param tasks Original list of tasks
	 * @return sublist of the original list
	 */
	protected List<Task> filter(List<Task> tasks)
	{
	    if(amount==0)
	        return new ArrayList<Task>();
	    if(amount > tasks.size())
	        amount = tasks.size();
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task task : tasks)
		{
			if(task.getTaskType().equals(this.taskType))
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
}
