package model;

import java.util.Comparator;

public class TaskDurationComparator implements Comparator<Task>{
	/**
	 * Compare Tasks by duration.
	 */
	public int compare(Task arg0, Task arg1) {
		if(arg0.getDuration() > arg1.getDuration())
			return 1;
		if(arg0.getDuration() < arg1.getDuration())
			return -1;			
		return 0;
	}

}
