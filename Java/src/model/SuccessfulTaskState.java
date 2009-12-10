package model;

import java.util.GregorianCalendar;

public class SuccessfulTaskState extends TaskState {

	protected SuccessfulTaskState(Task context) {
		super(context);
	}

	/**
	 * Returns a boolean indicating whether the current task can be finished.
	 * A task can not be finished when it is failed or any of its dependencies is failed.
	 */
	@Override
	protected boolean canBeFinished() {
		boolean canBeF = true;
		
		for(Task t: this.getContext().getDependencies()){
			canBeF = canBeF && t.canBeFinished();
		}
		return canBeF;
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
	 * Returns whether a task is succesful or not.
	 * @return
	 */
	@Override
	protected Boolean isSuccesful()
	{
		return true;
	}
}
