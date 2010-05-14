package model;

public abstract class TaskTypeConstraint {
	
	
	/**
	 * Indicates whether this constraint is satisfied or not.
	 * @return
	 */
	public abstract boolean satisfied(Task t);
	
}
