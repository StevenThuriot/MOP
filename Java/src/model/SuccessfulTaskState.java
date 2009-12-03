package model;

import java.util.GregorianCalendar;


import exception.*;

public class SuccessfulTaskState extends TaskState {

	public SuccessfulTaskState(Task context) {
		super(context);
	}
	
	public Boolean canBeExecuted() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a boolean indicating whether the current task can be finished.
	 * A task can not be finished when it is failed or any of its dependencies is failed.
	 */
	public boolean canBeFinished() {
		boolean canBeF = true;
		
		for(Task t: this.getContext().getDependencies()){
			canBeF = canBeF && t.canBeFinished();
		}
		return canBeF;
	}

	/**
	 * Sets <newDescription> to be the new description of this task.
	 * @param	newDescription
	 * 			The new description
	 * @throws EmptyStringException 
	 * @throws IllegalStateCall 
	 * @post	| new.getDescription()== newDescription
	 */
	public void setDescription(String newDescription)
			throws EmptyStringException, NullPointerException, IllegalStateCall {
		throw new IllegalStateCall("Not allowed to change the task details while successful.");
	}

	public void updateTaskStatusRecursively(Status newStatus) {
		// TODO Auto-generated method stub
		
	}

	public void updateTaskTiming(GregorianCalendar newStart,
			GregorianCalendar newDue, int newDuration)
			throws BusinessRule1Exception {
		// TODO Auto-generated method stub
		
	}

	public void setFailed() throws IllegalStateChangeException {
		// TODO Auto-generated method stub
		
	}

	public void setSuccessful() throws IllegalStateChangeException {
		// TODO Auto-generated method stub
		
	}

	public void setUnfinished() throws IllegalStateChangeException {
		// TODO Auto-generated method stub
		
	}
}
