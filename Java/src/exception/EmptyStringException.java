package exception;

@SuppressWarnings("serial")
public class EmptyStringException extends Exception {
	public EmptyStringException(String message)
	{
		super(message);
	}
}
