package model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FieldTest {
	private NumericField numField;
	private TextField textField;
	
	@Before
	public void setUp()
	{
		numField = new NumericField("Numbers", 1);
		textField = new TextField("Text", "This is text");
	}
	
	/**
	 * Testing if the standard value is correct
	 */
	@Test
	public void getNumber()
	{
		int i = numField.getValue();
		assertEquals(1, i);
	}
	
	/**
	 * Testing if the standard value is correct
	 */
	@Test
	public void getText()
	{
		String s = textField.getValue();
		assertEquals("This is text", s);
	}
	
	/**
	 * Testing if getName works
	 */
	@Test 
	public void getName()
	{
		String s1 = numField.getName();
		String s2 = textField.getName();
		
		assertEquals("Numbers", s1);
		assertEquals("Text", s2);
	}
	
	/**
	 * Testing setName
	 */
	@Test
	public void setName()
	{
		String s = "New text";
		numField.setName(s);
		
		assertEquals(s, numField.getName());
	}
	
	/**
	 * Testing getType
	 */
	@Test
	public void getType()
	{
		assertEquals(FieldType.Numeric, numField.getType());
		assertEquals(FieldType.Text, textField.getType());
	}
	
	/**
	 * Value is van het juiste type?
	 */
	@Test
	public void getValue()
	{
		Field valueField = new NumericField("integer", 101);
		assertEquals(101, valueField.getValue());
		
		Field valueField2 = new TextField("string", "Bart");
		assertEquals("Bart", valueField2.getValue());
		
		Field valueField3 = new TextField("string2", "Bart2");
		valueField3.setValue(101);
		assertEquals("101", valueField3.getValue());
	}
}
