package model;

abstract class Field<T> {
	private String name;
	private FieldType type;
	private T value;
	
	public Field(String name, FieldType type, T value) {
		this.setName(name);
		this.setType(type);
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
	
	public final void setType(FieldType type) {
		this.type = type;
	}
	
	public final T getValue() {
		return value;
	}

	public final void setValue(T value) {
		this.value = value;
	}
}
