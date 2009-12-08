package exception;

@SuppressWarnings("serial")
public class IllegalStateCall extends Exception {
	public IllegalStateCall()
	{
		super("Not allowed to call this method in the task's current state.");
	}
	
	public IllegalStateCall(String message)
	{
		super(message);
	}
}
