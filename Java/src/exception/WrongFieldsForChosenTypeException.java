package exception;

@SuppressWarnings("serial")
public class WrongFieldsForChosenTypeException extends Exception {

	/**
	 * Default constructor in case a wrong field was passed
	 */
	public WrongFieldsForChosenTypeException() {
		super("The passed fields do not match the chosen type.");
	}

	/**
	 * String constructor in case a wrong field was passed and an extra message wants to be given.
	 */
	public WrongFieldsForChosenTypeException(String arg0) {
		super(arg0);
	}
}
