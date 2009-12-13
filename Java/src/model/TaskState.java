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
	 * Returns whether a task can be executed right now.
	 * This is true when all its dependencies are (successfully) finished and
	 * all of its required resources are available.
	 */
	protected Boolean canBeExecuted() 
	{
		return false;
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
	 * Get the context (The task which this status belongs to)
	 * @return
	 */
	protected Task getContext() {
		return context;
	}
	
	/**
	 * Returns whether a task is performed or not.
	 * @return
	 */
	protected Boolean isPerformed()
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
	 * Returns whether a task is failed or not.
	 * @return
	 */
	protected Boolean isFailed()
	{
		return false;
	}

	/**
	 * Returns whether a task is unfinished or not.
	 * @return
	 */
	protected Boolean isUnfinished()
	{
		return false;
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
	 * Set <newDueDate> to be the new due date for this Task.
	 * 
	 * @param 	newDueDate
	 * 			The new due date for this Task.
	 * @post	<newDueDate> is the new due date for this Task.
	 * 			new.getDueDate() == newDueDate
	 */
	protected void setDueDate(GregorianCalendar newDueDate) throws NullPointerException {
		//TODO: Implement rules
		if (newDueDate == null)
			throw new NullPointerException("Null was passed");
		
		this.getContext().doSetDueDate( newDueDate );
	}		
	
	//Will the observer do this, and how?
	//protected void updateTaskStatusRecursively(Status newStatus);	
	
	/**
	 * Set the current state to failed
	 * @throws IllegalStateChangeException
	 */
	protected void setFailed() throws IllegalStateChangeException
	{
		//TODO: Implement method
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Set the current state to successful
	 * @throws IllegalStateChangeException
	 */
	protected void setSuccessful() throws IllegalStateChangeException 
	{
		//TODO: Implement method
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Set the current state to unfinished
	 * @throws IllegalStateChangeException
	 */
	protected void setUnfinished() throws IllegalStateChangeException 
	{
		//TODO: Implement method
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Returns whether the current task satisfies the business rule 2.
	 * @return Boolean
	 */
	protected abstract Boolean satisfiesBusinessRule2();
	
	/**
	 * Returns whether the current task satisfies the business rule 3.
	 * @return Boolean
	 */
	protected abstract Boolean satisfiesBusinessRule3();
	
}
