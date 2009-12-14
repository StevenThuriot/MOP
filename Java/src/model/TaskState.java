package model;

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
	 * String parser used to help set the state through the GUI or parse the XML file
	 * If the parser doesn't recognise the given state, it will remain in its default state,
	 * which is Unfinished.
	 * @param state
	 * @throws IllegalStateChangeException
	 * @throws UnknownStateException 
	 */
	protected final void parseString(String state) throws IllegalStateChangeException, UnknownStateException
	{
		/*
		 * no if statement for unfinished:
		 * It is the default state anyway and 
		 * impossible to change to from successful and 
		 * failed in our implementation
		 */

		boolean parsed = false;
		
		if (state.equals("Successful")) 
		{
			this.getContext().setSuccessful();
			parsed = true;
		}
		
		if (state.equals("Failed")) 
		{
			this.getContext().setFailed();
			parsed = true;
		}
		
		if (!parsed) {
			throw new UnknownStateException();
		}
	}

	/**
	 * Adds a dependency to the current task.
	 * @param 	dependency
	 * 			The dependency to be added.
	 * @post	The task is now dependent on <dependency>
	 * 			| (new.getDependentTasks()).contains(dependent)
	 * @throws 	BusinessRule1Exception
	 * 			Adding the dependency would violate business rule 1.
	 * 			| !this.depencySatisfiesBusinessRule1(dependent)
	 * @throws 	DependencyCycleException
	 * 			Adding the dependency would create a dependency cycle.
	 * 			| !this.dependencyHasNoCycle()
	 * @throws IllegalStateCallException If the call is not allowed in the current state
	 */
	protected void addDependency(Task dependency) throws BusinessRule1Exception, DependencyCycleException, IllegalStateCallException{
		throw new IllegalStateCallException();
	}	
	
	/**
	 * Adds a resource to the resources required for this task.
	 * @throws IllegalStateCallException 
	 */
	protected void addRequiredResource(Resource resource) throws IllegalStateCallException{
		throw new IllegalStateCallException();
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
	 * Get the context (The task which this status belongs to)
	 * @return
	 */
	protected Task getContext() {
		return context;
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
	 * Returns whether a task is succesful or not.
	 * @return
	 */
	protected Boolean isSuccesful()
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
	 * Removes a dependency from this task.
	 * @param 	dependency
	 * 			The dependency to be removed.
	 * @throws DependencyException 
	 * @throws IllegalStateCallException 
	 * @throws DependencyException 
	 * @post 	The task is no longer dependent on <dependency>
	 * 			|! (new.getDependentTasks()).contains(dependent)
	 */
	public void removeDependency(Task dependency) throws IllegalStateCallException, DependencyException{
		throw new IllegalStateCallException();
	}
	
	/**
	 * Removes a resource from the resources required for this task.
	 * @throws IllegalStateCallException 
	 */
	public void removeRequiredResource(Resource resource) throws IllegalStateCallException{
		throw new IllegalStateCallException();
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
	
	/**
	 * Sets <newDescription> to be the new description of this task.
	 * @param	newDescription
	 * 			The new description
	 * @throws EmptyStringException 
	 * @throws IllegalStateCallException 
	 * @post	| new.getDescription()== newDescription
	 */
	protected void setDescription(String newDescription) throws EmptyStringException, NullPointerException, IllegalStateCallException {
		throw new IllegalStateCallException();
	}
	
	/**
	 * Set the current state to failed
	 * @throws IllegalStateChangeException
	 */
	protected void setFailed() throws IllegalStateChangeException
	{
		throw new IllegalStateChangeException();
	}
	
	/**
	 * Set the current state to successful
	 * @throws IllegalStateChangeException
	 */
	protected void setSuccessful() throws IllegalStateChangeException 
	{
		throw new IllegalStateChangeException();
	}
	
	public abstract String toString();
	
}
