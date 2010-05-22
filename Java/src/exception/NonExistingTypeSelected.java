package exception;

@SuppressWarnings("serial")
public class NonExistingTypeSelected extends Exception {

	public NonExistingTypeSelected() {
		this("A non existing type was selected");
	}

	public NonExistingTypeSelected(String arg0) {
		super(arg0);
	}

}
