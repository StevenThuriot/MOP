package exception;

@SuppressWarnings("serial")
public class BusinessRule1Exception extends Exception {
	public BusinessRule1Exception()
	{
		super("Business rule 1 violation.");
	}	
	
	public BusinessRule1Exception(String message)
	{
		super(message);
	}
}
