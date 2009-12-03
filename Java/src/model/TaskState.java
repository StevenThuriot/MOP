package model;

import java.util.GregorianCalendar;


import exception.*;

public abstract class TaskState {
	private Task context;
		
	protected TaskState(Task context) {
		this.context = context;
	}

	protected Task getContext() {
		return context;
	}
	
	public abstract void setDescription(String newDescription) throws EmptyStringException, NullPointerException, IllegalStateCall; //Done
	public abstract void updateTaskTiming(GregorianCalendar newStart, GregorianCalendar newDue, int newDuration) throws BusinessRule1Exception;
	public abstract boolean canBeFinished(); //Done
	public abstract void updateTaskStatusRecursively(Status newStatus);
	public abstract Boolean canBeExecuted();
	
	public abstract void setSuccessful() throws IllegalStateChangeException;
	public abstract void setUnfinished() throws IllegalStateChangeException;
	public abstract void setFailed() throws IllegalStateChangeException;
}
