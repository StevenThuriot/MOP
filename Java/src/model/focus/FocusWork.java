package model.focus;

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
		List<Task> tasks = currentUser.getTasks();
		return strategy.filter(strategy.sort(tasks));		
	}
}
