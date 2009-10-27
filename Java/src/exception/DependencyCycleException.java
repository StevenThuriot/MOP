package exception;

@SuppressWarnings("serial")
public class DependencyCycleException extends Exception {

	public DependencyCycleException(String message)
	{
		super(message);
	}
}
