package model;

import java.util.GregorianCalendar;

import exception.*;

public interface TaskState {
	public void setDescription(String newDescription) throws EmptyStringException, NullPointerException;
	public void updateTaskTiming(GregorianCalendar newStart, GregorianCalendar newDue, int newDuration) throws BusinessRule1Exception;
	public boolean canBeFinished();
	public void updateTaskStatus(Status newStatus) throws DependencyException;
	public void updateTaskStatusRecursively(Status newStatus);
	public void setStatus(Status newStatus);
	public Status getStatus();
	public Boolean canBeExecuted();
}
