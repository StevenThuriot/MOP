package model.focus;

import java.util.ArrayList;
import java.util.List;

import model.Task;
import model.User;

/**
 * Compositor Class
 */
public class FocusWork {
    private User currentUser;
    private FocusStrategy strategy;
    public FocusWork(User currentUser,FocusStrategy strategy)
    {
        this.currentUser = currentUser;
        this.strategy = strategy;
    }
    /**
     * Static method to make sure a list of tasks gets shown the correct way by using a strategy.
     * @param currentUser
     * @param strategy
     * @return
     */
	public List<Task> getTasks()
	{
		List<Task> tasks = this.copyList(currentUser.getTasks());
		return strategy.filter(strategy.sort(tasks));		
	}
	
	/**
	 * Copy the list from an unmodifiable List to a new list so sorting and filtering can occur
	 * @param original
	 * @return
	 */
	private List<Task> copyList(List<Task> original)
	{
	    List<Task> tasks = new ArrayList<Task>();
	    for(Task task:original)
	        tasks.add(task);
	    return tasks;
	}
	
	/**
	 * Method to return the strategy used. Only used for testing purposes
	 */
	public FocusStrategy getStrategy()
	{
	    return strategy;
	}
}
