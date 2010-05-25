package model;


public class FailedTaskState extends TaskState {

	protected FailedTaskState(Task context) {
		super(context);
	}
	
	/**
	 * Overriden method to set the state to failed.
	 * This method doesn't have to do anything as the current state is already set to failed
	 */
	@Override
	protected void setFailed()
	{
	}
	
	/**
	 * Returns whether a task is failed or not.
	 * @return
	 */
	@Override
	protected Boolean isFailed()
	{
		return true;
	}
	
	/**
	 * Returns whether the current task satisfies the business rule 2.
	 * @return Boolean
	 */
	protected Boolean satisfiesBusinessRule2()
	{
		return true;
	}
	
	/**
	 * Returns whether the current task satisfies the business rule 3.
	 * @return Boolean
	 */
	protected Boolean satisfiesBusinessRule3()
	{
		//Rule is always satisfied when failed
		return true;
	}

	@Override
	public String toString() {
		return "Failed";
	}
	
	
}
