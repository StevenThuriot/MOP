package exception;

@SuppressWarnings("serial")
public class IllegalStateCallException extends Exception {
	public IllegalStateCallException()
	{
		super("Not allowed to call this method in the task's current state.");
	}
	
	public IllegalStateCallException(String message)
	{
		super(message);
	}
}
