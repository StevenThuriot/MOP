package model;

import exception.DependencyException;

public class UnavailableTaskState implements TaskState {
	public Boolean canBeExecuted() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean canBeFinished() {
		// TODO Auto-generated method stub
		return false;
	}


	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setStatus(Status newStatus) {
		// TODO Auto-generated method stub
		
	}

	public void updateTaskStatus(Status newStatus) throws DependencyException {
		// TODO Auto-generated method stub
		
	}

	
	public void updateTaskStatusRecursively(Status newStatus) {
		// TODO Auto-generated method stub
		
	}
}
