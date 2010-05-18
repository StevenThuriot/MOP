package model;

public class TextField extends Field<String> {
	
	/**
	 * Field constructor for integers.
	 * @param name Field name
	 * @param value Field value
	 */
	public TextField(String name, String value) {
		super(name, FieldType.Text, value);
	}

	/**
	 * Set the current value of the field
	 * @param value The new value
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}
}
