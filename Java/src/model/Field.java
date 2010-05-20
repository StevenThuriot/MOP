package model;

import exception.EmptyStringException;

public abstract class Field<T> {
	/**
	 * Field name
	 */
	private String name;
	/**
	 * Type of field
	 */
	private FieldType type;
	/**
	 * The value of the field
	 */
	protected T value;
	
	/**
	 * Constructor
	 * @param name Field name
	 * @param type Type of field
	 * @param value The value of the field
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	public Field(String name, FieldType type, T value) throws NullPointerException, EmptyStringException {
		this.setName(name);
		this.type = type;
		this.setValue(value);
	}
	
	/**
	 * Get the name of the field
	 * @return Field name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Set the name of the field
	 * @param name The new name
	 * @throws EmptyStringException 
	 * @throws NullPointerException
	 */
	public final void setName(String name) throws EmptyStringException, NullPointerException {
		if (name == null)
			throw new NullPointerException("Null was passed");
		
		if(name.equals(""))
			throw new EmptyStringException("A task should have a non-empty description");
		
		this.name = name;
	}
	
	/**
	 * Get the type of the field
	 * @return Enumeration of the type.
	 */
	public final FieldType getType() {
		return type;
	}
	
	/**
	 * Get the current value of the field.
	 * @return Current value
	 */
	public final T getValue() {
		return value;
	}

	/**
	 * Set the current value of the field
	 * @param value The new value
	 */
	public abstract void setValue(T value) throws NullPointerException;
}
