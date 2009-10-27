package exception;

@SuppressWarnings("serial")
public class ResourceBusyException extends Exception {
	public ResourceBusyException(String message)
	{
		super(message);
	}
}
