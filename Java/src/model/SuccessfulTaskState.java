package model;

public class SuccessfulTaskState extends TaskState {

	public SuccessfulTaskState(Task context) {
		super(context);
	}

	/**
	 * Returns a boolean indicating whether the current task can be finished.
	 * A task can not be finished when it is failed or any of its dependencies is failed.
	 */
	@Override
	public boolean canBeFinished() {
		boolean canBeF = true;
		
		for(Task t: this.getContext().getDependencies()){
			canBeF = canBeF && t.canBeFinished();
		}
		return canBeF;
	}
}
