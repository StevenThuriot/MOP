package model;

import java.util.GregorianCalendar;

import exception.BusinessRule1Exception;
import exception.DependencyException;
import exception.EmptyStringException;

public class AvailableTaskState implements TaskState {

	@Override
	public Boolean canBeExecuted() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canBeFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String newDescription)
			throws EmptyStringException, NullPointerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(Status newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTaskStatus(Status newStatus) throws DependencyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTaskStatusRecursively(Status newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTaskTiming(GregorianCalendar newStart,
			GregorianCalendar newDue, int newDuration)
			throws BusinessRule1Exception {
		// TODO Auto-generated method stub
		
	}
	
}
