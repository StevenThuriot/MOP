package exception;

@SuppressWarnings("serial")
public class UnknownStateException extends Exception {
	public UnknownStateException()
	{
		super("An unknown state has been passed to the parser.");
	}
	
	public UnknownStateException(String message)
	{
		super(message);
	}
}
