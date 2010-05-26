package exception;

@SuppressWarnings("serial")
public class EmptyListPassedToMenuException extends Exception {

	/**
	 * Normal constructor
	 */
	public EmptyListPassedToMenuException() {
		this("An empty list was passed to the menu.");
	}

	/**
	 * Constructor with message
	 * @param arg0
	 */
	public EmptyListPassedToMenuException(String arg0) {
		super(arg0);
	}
}
