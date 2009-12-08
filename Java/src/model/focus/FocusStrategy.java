package model.focus;
import java.util.List;
import model.Task;
import model.User;

/**
 * Strategy class
 *
 */
public class FocusStrategy {

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
