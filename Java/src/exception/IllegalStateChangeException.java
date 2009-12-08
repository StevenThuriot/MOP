package exception;

@SuppressWarnings("serial")
public class IllegalStateChangeException extends Exception {
	public IllegalStateChangeException() {
		super("Not allowed to change to this state from current state.");
	}
	
	public IllegalStateChangeException(String message) {
		super(message);
	}
}
