package model;

public class FailedTaskState extends TaskState {

	protected FailedTaskState(Task context) {
		super(context);
	}
	/**
	 * Returns whether a task is performed or not.
	 * @return
	 */
	@Override
	protected Boolean isPerformed()
	{
		return true;
	}
	
}
