package model.focus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Task;
import model.TaskDurationComparator;

public class DurationFocus extends FocusStrategy {

	private int minimum,maximum;
	public DurationFocus(int minimum,int maximum)
	{
		this.minimum = minimum;
		this.maximum = maximum;
	}
	@Override
	public List<Task> showtasks() {
		List<Task> tasks = this.filterByDuration(this.getCurrentUser().getTasks());
		Collections.sort(tasks,new TaskDurationComparator());
		return tasks;
	}
	
	public List<Task> filterByDuration(List<Task> taken)
	{
		ArrayList<Task> filtered = new ArrayList<Task>();
		for(Task current : taken)
			if(current.getDuration() < this.maximum && current.getDuration() > this.minimum)
				filtered.add(current);
		return filtered;
	}

}
