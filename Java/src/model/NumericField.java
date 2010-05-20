package model;

import exception.EmptyStringException;

public class NumericField extends Field<Integer> {
	/**
	 * Field constructor for integers.
	 * @param name Field name
	 * @param value Field value
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	public NumericField(String name, int value) throws NullPointerException, EmptyStringException {
		super(name, FieldType.Numeric, value);
	}

	/**
	 * Set the current value of the field
	 * @param value The new value
	 * @throws NullPointerException
	 */
	@Override
	public void setValue(Integer value) throws NullPointerException {
		if (value == null)
			throw new NullPointerException("Null was passed");
		
		this.value = value;
	}	
}
