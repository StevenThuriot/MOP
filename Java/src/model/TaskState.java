package model;

import exception.*;

public interface TaskState {
	public boolean canBeFinished();
	public void updateTaskStatus(Status newStatus) throws DependencyException;
	public void updateTaskStatusRecursively(Status newStatus);
	public void setStatus(Status newStatus);
	public Status getStatus();
	public Boolean canBeExecuted();
}
