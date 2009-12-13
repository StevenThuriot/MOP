package exception;

@SuppressWarnings("serial")
public class BusinessRule2Exception extends Exception {
	public BusinessRule2Exception()
	{
		super("Business rule 2 violation.");
	}	
	
	public BusinessRule2Exception(String message)
	{
		super(message);
	}
}
