package model;

import exception.EmptyStringException;

public class TextField extends Field<String> {
	
	/**
	 * Field constructor for integers.
	 * @param name Field name
	 * @param value Field value
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	public TextField(String name, String value) throws NullPointerException, EmptyStringException {
		super(name, FieldType.Text, value);
	}

	/**
	 * Set the current value of the field
	 * @param value The new value
	 * @throws NullPointerException
	 */
	@Override
	public void setValue(String value) throws NullPointerException {
		if (value == null)
			throw new NullPointerException("Null was passed");
		
		this.value = value;
	}
}
