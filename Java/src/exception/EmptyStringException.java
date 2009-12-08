package exception;

@SuppressWarnings("serial")
public class EmptyStringException extends Exception {
	public EmptyStringException()
	{
		super("An empty string was passed");
	}
	
	public EmptyStringException(String message)
	{
		super(message);
	}
}
