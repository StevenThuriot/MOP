package model.focus;

import java.util.List;

import model.Task;

/**
 * Compositor Class
 * @author bart
 *
 */
public class FocusWork {
	private FocusStrategy strategy;
	public FocusWork(FocusStrategy strategy)
	{
		this.strategy = strategy;
	}
	public List<Task> getTasks()
	{
		return strategy.showtasks();
	}
}
