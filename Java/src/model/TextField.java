package model;

public class TextField extends Field<String> {
	public TextField(String name, String value) {
		super(name, FieldType.Text, value);
	}
}
