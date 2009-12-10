package model;

import exception.*;

public class UnavailableTaskState extends TaskState {

	protected UnavailableTaskState(Task context) {
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
	 * Returns whether a task can be executed right now.
	 * This is true when all its dependencies are (successfully) finished and
	 * all of its required resources are available.
	 */
	@Override
	protected Boolean canBeExecuted(){
		return false;
	}
}
