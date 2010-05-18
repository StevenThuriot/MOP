package model;

public class NumericField extends Field<Integer> {
	/**
	 * Field constructor for integers.
	 * @param name Field name
	 * @param value Field value
	 */
	public NumericField(String name, int value) {
		super(name, FieldType.Numeric, value);
	}

	/**
	 * Set the current value of the field
	 * @param value The new value
	 */
	@Override
	public void setValue(Integer value) {
		this.value = value;
	}	
}
