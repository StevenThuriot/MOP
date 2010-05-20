package model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import exception.EmptyStringException;
import exception.IllegalStateChangeException;

public class FieldTest {
	private NumericField numField;
	private TextField textField;
	
	@Before
	public void setUp() throws NullPointerException, EmptyStringException
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
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test
	public void setName() throws NullPointerException, EmptyStringException
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
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getValue() throws NullPointerException, EmptyStringException
	{
		Field valueField = new NumericField("integer", 101);
		assertEquals(101, valueField.getValue());
		
		Field valueField2 = new TextField("string", "Bart");
		assertEquals("Bart", valueField2.getValue());
	}
	
	/**
	 * Value is van het juiste type?
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getValue2() throws NullPointerException, EmptyStringException
	{
		Field valueField = new NumericField("integer", 101);
		assertEquals(101, valueField.getValue());
		
		Field valueField2 = new TextField("string", "Bart");
		assertEquals("Bart", valueField2.getValue());
		
		Field valueField3 = new TextField("string2", "Bart2");
		valueField3.setValue("101");
		assertEquals("101", valueField3.getValue());
		
		try {
			valueField3.setValue(101);
			fail();
		} catch (ClassCastException e) {
			
		}
	}

	/**
	 * Testing the anti nullpointer code
	 * @throws NullPointerException 
	 */
	@Test(expected=NullPointerException.class)
	public void ValuenullTest1()
	{
		numField.setValue(null);
	}
	
	/**
	 * Testing the anti nullpointer code
	 * @throws NullPointerException 
	 */
	@Test(expected=NullPointerException.class)
	public void ValuenullTest2()
	{
		textField.setValue(null);
	}
	
	/**
	 * Testing the anti nullpointer code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@SuppressWarnings("unchecked")
	@Test(expected=NullPointerException.class)
	public void ValuenullTest3() throws NullPointerException, EmptyStringException
	{
		Field valueField = new NumericField("integer", 101);
		valueField.setValue(null);
	}
		
	/**
	 * Testing the anti nullpointer code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=NullPointerException.class)
	public void ValuenullTest4() throws NullPointerException, EmptyStringException
	{
		new TextField("integer", null);
	}
		
	/**
	 * Testing the anti nullpointer code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=NullPointerException.class)
	public void NamenullTest1() throws NullPointerException, EmptyStringException
	{
		new TextField(null, "string");
	}
		
	/**
	 * Testing the anti nullpointer code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=NullPointerException.class)
	public void NamenullTest2() throws NullPointerException, EmptyStringException
	{
		new NumericField(null, 101);
	}
		
	/**
	 * Testing the anti empty name code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=EmptyStringException.class)
	public void EmptyNameTest1() throws NullPointerException, EmptyStringException
	{
		new NumericField("", 101);
	}
		
	/**
	 * Testing the anti empty name code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=EmptyStringException.class)
	public void EmptyNameTest2() throws NullPointerException, EmptyStringException
	{
		new TextField("", "string");
	}
}
