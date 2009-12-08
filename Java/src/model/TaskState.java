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
	public void setDescription(String newDescription) throws EmptyStringException, NullPointerException, IllegalStateCall {
		throw new IllegalStateCall();
	}
	
	/**
	 * Returns a boolean indicating whether the current task can be finished.
	 * A task can not be finished when it is failed or any of its dependencies is failed.
	 */
	public boolean canBeFinished()
	{
		return false;
	}
	
	/**
	 * Returns whether a task can be executed right now.
	 * This is true when all its dependencies are (successfully) finished and
	 * all of its required resources are available.
	 */
	public Boolean canBeExecuted() 
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
	public void updateTaskTiming(GregorianCalendar newStart, GregorianCalendar newDue, int newDuration) throws BusinessRule1Exception
	{
		throw new BusinessRule1Exception();
	}
	
	//Will the observer put do this, and how?
	//public abstract void updateTaskStatusRecursively(Status newStatus);
		
	
	/**
	 * Set the current state to successful
	 * @throws IllegalStateChangeException
	 */
	public void setSuccessful() throws IllegalStateChangeException 
	{
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Set the current state to unfinished
	 * @throws IllegalStateChangeException
	 */
	public void setUnfinished() throws IllegalStateChangeException 
	{
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Set the current state to failed
	 * @throws IllegalStateChangeException
	 */
	public void setFailed() throws IllegalStateChangeException
{
		throw new IllegalStateChangeException();
	}
	
}
