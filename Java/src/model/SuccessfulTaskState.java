package model;

import java.util.GregorianCalendar;
import java.util.List;

public class SuccessfulTaskState extends TaskState {

	protected SuccessfulTaskState(Task context) {
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
		
		GregorianCalendar now = new GregorianCalendar();
		
		for(Resource r: this.getContext().getRequiredResources()){
			resourceReady = resourceReady && (r.availableAt(now, this.getContext().getDuration()));
		}

		return resourceReady;
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
				break;
			}
			
			if (task.isUnfinished()) {
				unfinished = true;
				break;
			}
		}
		
		if (failed || unfinished) {
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
		
		//Not before start time
		if ( !currentTime.before(startTime) ) {
			return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "Successful";
	}
}
