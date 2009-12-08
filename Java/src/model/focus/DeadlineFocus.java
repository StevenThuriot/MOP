package model.focus;

import java.util.Collections;
import java.util.List;

import model.Task;
import model.TaskDeadlineComparator;

public class DeadlineFocus extends FocusStrategy {

	private int amount;
	public DeadlineFocus(int amount) {
		this.amount = amount;
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
	 * Method that returns only a few tasks out of a list. This 'few' is defined by the given amount
	 * @param tasks Original list of tasks
	 * @return sublist of the original list
	 */
	protected List<Task> filter(List<Task> tasks)
	{
		return tasks.subList(0, amount - 1);
	}
}
