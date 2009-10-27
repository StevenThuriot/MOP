package exception;

@SuppressWarnings("serial")
public class TaskFailedException extends Exception{

	public TaskFailedException(String message)
	{
		super(message);
	}
}
