package exception;

@SuppressWarnings("serial")
public class BadAllocationTimingException extends Exception {
	
	public BadAllocationTimingException(){
		super("Allocation timings are bad.");
	}
	
	public BadAllocationTimingException(String message){
		super(message);
	}

}
