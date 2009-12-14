package model;

import java.util.GregorianCalendar;
import java.util.List;

import exception.*;

public class UnfinishedTaskState extends TaskState {
	protected UnfinishedTaskState(Task context) {
		super(context);
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
	 */
	protected void addDependency(Task dependency) throws BusinessRule1Exception, DependencyCycleException{
		if(!this.getContext().dependencySatisfiesBusinessRule1(dependency))
			throw new BusinessRule1Exception(
			"This dependency would not satisfy business rule 1");
		
		if(!this.getContext().dependencyHasNoCycle(dependency))
			throw new DependencyCycleException(
			"This dependency would create a dependency cycle");
		
		this.getContext().getTaskDependencyManager().addDependency(dependency);
	}	
	
	/**
	 * Adds a resource to the resources required for this task.
	 */
	protected void addRequiredResource(Resource resource){
		this.getContext().doAddRequiredResource(resource);
	}
		
	/**
	 * Returns whether a task can be executed right now.
	 * This is true when all its dependencies are (successfully) finished and
	 * all of its required resources are available.
	 */
	@Override
	protected Boolean canBeExecuted(){
		
		boolean resourceReady = true;
		boolean depReady = true;
		
		GregorianCalendar now = new GregorianCalendar();
		
		for(Resource r: this.getContext().getRequiredResources()){
			resourceReady = resourceReady && (r.availableAt(now, this.getContext().getDuration()));
		}
		
		for(Task t: this.getContext().getDependencies()){
			depReady = depReady && t.isSuccesful();
		}
		
		return resourceReady && depReady;
	}
	
	/**
	 * Returns a boolean indicating whether the current task can be finished.
	 * A task can not be finished when it is failed or any of its dependencies is failed.
	 */
	@Override
	protected boolean canBeFinished()
	{
		boolean canBeF = true;
		
		for(Task t: this.getContext().getDependencies()){
			canBeF = canBeF && t.canBeFinished();
		}
		return canBeF;
	}
	
	/**
	 * Returns whether a task is unfinished or not.
	 * @return
	 */
	@Override
	protected Boolean isUnfinished()
	{
		return true;
	}
	
	/**
	 * Removes a dependency from this task.
	 * @param 	dependency
	 * 			The dependency to be removed.
	 * @throws DependencyException 
	 * @throws DependencyException 
	 * @post 	The task is no longer dependent on <dependency>
	 * 			|! (new.getDependentTasks()).contains(dependent)
	 */
	public void removeDependency(Task dependency) throws DependencyException {
		this.getContext().getTaskDependencyManager().removeDependency(dependency);
	}
	
	/**
	 * Removes a resource from the resources required for this task.
	 */
	public void removeRequiredResource(Resource resource){
		this.getContext().doRemoveRequiredResource(resource);
	}
	
	/**
	 * Returns whether the current task satisfies the business rule 2.
	 * @return Boolean
	 */
	protected Boolean satisfiesBusinessRule2()
	{
		List<Task> list = this.getContext().getTaskDependencyManager().getDependencies();
		boolean failed = false;
		boolean unfinished = false;
		
		for (Task task : list) {
			if (task.isFailed()) {
				failed = true;
			}
			
			if (task.isUnfinished()) {
				unfinished = true;
			}
		}
		
		if (failed) {
			return false;
		}
		
		if (unfinished && this.canBeExecuted()) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Returns whether the current task satisfies the business rule 3.
	 * @return Boolean
	 */
	protected Boolean satisfiesBusinessRule3()
	{
		GregorianCalendar currentTime = new GregorianCalendar();
		GregorianCalendar startTime = this.getContext().getStartDate();
		GregorianCalendar dueTime = this.getContext().getDueDate();
		boolean answer = false;
		
		//Before start time and Unvailable
		if ( currentTime.before(startTime) && !this.canBeExecuted() ) {
			//Rule succeeds, continue to next check
			answer = true;
		}
		
		//After or at the duetime
		if (answer && !currentTime.before(dueTime) ) {
			//Rule fails
			answer = false;
		}
		
		return answer;
	}
	
	/**
	 * Sets <newDescription> to be the new description of this task.
	 * @param	newDescription
	 * 			The new description
	 * @throws EmptyStringException 
	 * @post	| new.getDescription()== newDescription
	 */
	@Override
	protected void setDescription(String newDescription)
			throws EmptyStringException, NullPointerException {
		this.getContext().doSetDescription(newDescription);
	}

	
	/**
	 * Set the current state to failed
	 */
	protected void setFailed()
	{
		this.getContext().doSetState( new FailedTaskState(this.getContext()) );
	}
	
	/**
	 * Set the current state to successful
	 * @throws IllegalStateChangeException
	 */
	protected void setSuccessful() throws IllegalStateChangeException 
	{
		boolean check = false;
		if ( this.canBeExecuted() ) {
			TaskState newState = new SuccessfulTaskState( this.getContext() );
			if (newState.satisfiesBusinessRule2() && newState.satisfiesBusinessRule3()) {
				this.getContext().doSetState(newState);
				check = true;
			}
		}
		
		if (!check) {
			throw new IllegalStateChangeException();
		}
	}

	@Override
	public String toString() {
		return "Unfinished";
	}
}
