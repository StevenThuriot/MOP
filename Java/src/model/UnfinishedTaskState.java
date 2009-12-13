package model;

import java.util.GregorianCalendar;
import java.util.List;

import exception.*;

public class UnfinishedTaskState extends TaskState {
	protected UnfinishedTaskState(Task context) {
		super(context);
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
	 * Returns whether a task is unfinished or not.
	 * @return
	 */
	@Override
	protected Boolean isUnfinished()
	{
		return true;
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
}
