package model;

import java.util.Comparator;

public class TaskDeadlineComparator implements Comparator<Task> {

	/**
	 * Compare Tasks by deadline. If a deadline is closer then another one, it counts as higher.
	 */
	public int compare(Task arg0, Task arg1) {
		if(arg0.getDueDate().before(arg1.getDueDate()))
			return -1;
		if(arg0.getDueDate().after(arg1.getDueDate()))
			return 1;			
		return 0;
	}

}
