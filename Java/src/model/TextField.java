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

	@Override
	public Field<String> clone() {
		Field<String> clonedField = null;
		try {
			clonedField = new TextField(this.getName(), this.getValue());
		} catch (NullPointerException e) {
			//For this error to occur, the to be cloned field has to be invalid. This is not possible
		} catch (EmptyStringException e) {
			//For this error to occur, the to be cloned field has to be invalid. This is not possible
		}
		return clonedField;
	}
}
