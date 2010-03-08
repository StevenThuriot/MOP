package exception;

@SuppressWarnings("serial")
public class ArrayLengthException extends Exception {
	public ArrayLengthException()
	{
		super("The passed settingsarray has a faulty length for the chosen type.");
	}
	
	public ArrayLengthException(String message)
	{
		super(message);
	}
}
