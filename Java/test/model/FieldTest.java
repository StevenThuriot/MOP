package model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import exception.EmptyStringException;

public class FieldTest {
	private NumericField numField;
	private TextField textField;
	
	@Before
	public void setUp() throws NullPointerException, EmptyStringException
	{
		numField = new NumericField("Numbers", 1, null);
		textField = new TextField("Text", "This is text", null);
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
		Field valueField = new NumericField("integer", 101, null);
		assertEquals(101, valueField.getValue());
		
		Field valueField2 = new TextField("string", "Bart", null);
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
		Field valueField = new NumericField("integer", 101, null);
		assertEquals(101, valueField.getValue());
		
		Field valueField2 = new TextField("string", "Bart", null);
		assertEquals("Bart", valueField2.getValue());
		
		Field valueField3 = new TextField("string2", "Bart2", null);
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
		Field valueField = new NumericField("integer", 101, null);
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
		new TextField("integer", null, null);
	}
		
	/**
	 * Testing the anti nullpointer code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=NullPointerException.class)
	public void NamenullTest1() throws NullPointerException, EmptyStringException
	{
		new TextField(null, "string", null);
	}
		
	/**
	 * Testing the anti nullpointer code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=NullPointerException.class)
	public void NamenullTest2() throws NullPointerException, EmptyStringException
	{
		new NumericField(null, 101, null);
	}
		
	/**
	 * Testing the anti empty name code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=EmptyStringException.class)
	public void EmptyNameTest1() throws NullPointerException, EmptyStringException
	{
		new NumericField("", 101, null);
	}
		
	/**
	 * Testing the anti empty name code
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 */
	@Test(expected=EmptyStringException.class)
	public void EmptyNameTest2() throws NullPointerException, EmptyStringException
	{
		new TextField("", "string", null);
	}
	
	/**
	 * Testing the clones
	 */
	@Test
	public void cloneTest()
	{
		NumericField nf = (NumericField) numField.clone();
		TextField tf = (TextField) textField.clone();
		
		assertEquals(numField.getName(), nf.getName());
		assertEquals(numField.getType(), nf.getType());
		assertEquals(numField.getValue(), nf.getValue());
		
		assertEquals(textField.getName(), tf.getName());
		assertEquals(textField.getType(), tf.getType());
		assertEquals(textField.getValue(), tf.getValue());
		
	}
}
