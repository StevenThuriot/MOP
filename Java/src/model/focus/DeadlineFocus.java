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
	@Override
	public List<Task> showtasks() {
		List<Task> allTasks = this.getCurrentUser().getTasks();
		Collections.sort(allTasks,new TaskDeadlineComparator());
		return allTasks.subList(0, amount - 1);
	}
}
