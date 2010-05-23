package model;

import exception.EmptyStringException;

public class NumericField extends Field<Integer> {
	/**
	 * Field constructor for integers.
	 * @param name Field name
	 * @param value Field value
	 * @param string 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	public NumericField(String name, int value, String id) throws NullPointerException, EmptyStringException {
		super(name, FieldType.Numeric, value,id);
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

	@Override
	public Field<Integer> clone() {
		Field<Integer> clonedField = null;
		try {
			clonedField = new NumericField(this.getName(), this.getValue(),this.getId());
		} catch (NullPointerException e) {
			//For this error to occur, the to be cloned field has to be invalid. This is not possible
		} catch (EmptyStringException e) {
			//For this error to occur, the to be cloned field has to be invalid. This is not possible
		}
		return clonedField;
	}	
}
