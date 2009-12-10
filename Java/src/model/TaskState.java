package model;

import java.util.GregorianCalendar;


import exception.*;

public abstract class TaskState {
	private Task context;
	
	/**
	 * Constructor
	 * @param context
	 */
	protected TaskState(Task context) {
		this.context = context;
	}

	/**
	 * Get the context (The task which this status belongs to)
	 * @return
	 */
	protected Task getContext() {
		return context;
	}	
	
	/**
	 * Sets <newDescription> to be the new description of this task.
	 * @param	newDescription
	 * 			The new description
	 * @throws EmptyStringException 
	 * @throws IllegalStateCall 
	 * @post	| new.getDescription()== newDescription
	 */
	protected void setDescription(String newDescription) throws EmptyStringException, NullPointerException, IllegalStateCall {
		throw new IllegalStateCall();
	}
	
	/**
	 * Returns a boolean indicating whether the current task can be finished.
	 * A task can not be finished when it is failed or any of its dependencies is failed.
	 */
	protected boolean canBeFinished()
	{
		return false;
	}
	
	/**
	 * Returns whether a task can be executed right now.
	 * This is true when all its dependencies are (successfully) finished and
	 * all of its required resources are available.
	 */
	protected Boolean canBeExecuted() 
	{
		return false;
	}

	/**
	 * Returns whether a task is succesful or not.
	 * @return
	 */
	protected Boolean isSuccesful()
	{
		return false;
	}
	
	/**
	 * Updates the task's dates
	 * @param newStart
	 * @param newDue
	 * @param newDuration
	 * @throws BusinessRule1Exception
	 */
	protected void updateTaskTiming(GregorianCalendar newStart, GregorianCalendar newDue, int newDuration) throws BusinessRule1Exception
	{
		throw new BusinessRule1Exception();
	}
	
	//Will the observer do this, and how?
	//protected void updateTaskStatusRecursively(Status newStatus);
		
	
	/**
	 * Set the current state to successful
	 * @throws IllegalStateChangeException
	 */
	protected void setSuccessful() throws IllegalStateChangeException 
	{
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Set the current state to unfinished
	 * @throws IllegalStateChangeException
	 */
	protected void setUnfinished() throws IllegalStateChangeException 
	{
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Set the current state to failed
	 * @throws IllegalStateChangeException
	 */
	protected void setFailed() throws IllegalStateChangeException
{
		throw new IllegalStateChangeException();
	}
	
}
