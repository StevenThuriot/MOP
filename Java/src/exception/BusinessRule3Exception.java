package exception;

@SuppressWarnings("serial")
public class BusinessRule3Exception extends Exception {
	public BusinessRule3Exception()
	{
		super("Business rule 3 violation.");
	}	
	
	public BusinessRule3Exception(String message)
	{
		super(message);
	}
}
