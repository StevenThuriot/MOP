package model;

abstract class Field<T> {
	private String name;
	private FieldType type;
	private T value;
	
	public Field(String name, FieldType type, T value) {
		this.setName(name);
		this.type = type;
		this.setValue(value);
	}
	
	public final String getName() {
		return name;
	}
	
	public final void setName(String name) {
		this.name = name;
	}
	
	public final FieldType getType() {
		return type;
	}
	
	public final T getValue() {
		return value;
	}

	public final void setValue(T value) {
		this.value = value;
	}
}
