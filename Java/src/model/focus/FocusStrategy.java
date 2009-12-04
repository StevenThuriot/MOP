package model.focus;
import java.util.List;
import model.Task;
import model.User;

/**
 * Strategy class
 * @author bart
 *
 */
public class FocusStrategy {
	private User currentUser;
	/**
	 * Method that defines the default protocol for getting a Focus:
	 * 1. Get tasks
	 * 2. Filter by certain parameters
	 * 3. Sort the tasks
	 * 4. Return tasks
	 * @return
	 */
	public final List<Task> showtasks()
	{
		List<Task> tasks = this.getTasks();
		this.sort(tasks);
		this.filter(tasks);
		return tasks;
	}
	/**
	 * Get all the tasks from the current user.
	 * This method can be overrided
	 * @return
	 */
	protected List<Task> getTasks()
	{
		return currentUser.getTasks();
	}
	/**
	 * Method to filter tasks according to a certain strategy.
	 * This method is to be overrided by classes extending FocusStrategy
	 * @param tasks
	 * @return
	 */
	protected List<Task> filter(List<Task> tasks)
	{
		return tasks;
	}
	/**
	 * Method to sort tasks according to a certain strategy.
	 * This method is to be overrided by classes extending FocusStrategy
	 * @param tasks
	 * @return
	 */
	protected List<Task> sort(List<Task> tasks)
	{
		return tasks;
	}
}
