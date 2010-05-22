package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;
import gui.Describable;

public class TaskType implements Describable {

	/**
	 * A name describing this type of Task.
	 */
	private String name;
	
	/**
	 * An ArrayList containing the Fields describing a Task of this type.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Field> template;
	
	/**
	 * Specifies the constraints relating to resources.
	 */
	private ArrayList<TaskTypeConstraint> constraints;

		
	
	@SuppressWarnings("unchecked")
	public TaskType(String name, ArrayList<Field> fields, 
			ArrayList<TaskTypeConstraint> constraints){
		this.name = name;
		this.template = fields;
		this.constraints = constraints;
		}
	
	
	/**
	 * @return The name describing this type of Task.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return A cloned list containing the fields needed to describe this type of Task.
	 */
	@SuppressWarnings("unchecked")
	public List<Field> getTemplate(){
		List<Field> clonedTemplate = new ArrayList<Field>();
		for(Field templateField:this.template)
		{
			clonedTemplate.add(templateField.clone());
		}
		return Collections.unmodifiableList(clonedTemplate);
	}
	
	/**
	 * @return An unmodifiable list containing the constraints specified
	 * 			in this TaskType
	 */
	public List<TaskTypeConstraint> getConstraints(){
		return Collections.unmodifiableList(constraints);
	}


	/**
	 * Checking the fields consistency
	 * @param type
	 * @param fields
	 * @return
	 * @throws WrongFieldsForChosenTypeException 
	 */
	@SuppressWarnings("unchecked")
	public void checkFields(List<Field> fields) throws WrongFieldsForChosenTypeException
	{
		if (fields.size() != template.size())
			throw new WrongFieldsForChosenTypeException();
		
		for (int i = 0; i < template.size(); i++) 
		{
			Field selectedField = template.get(i);
			Field templateField = null;
			
			try {
				templateField = fields.get(i);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new WrongFieldsForChosenTypeException();
			}
			
			if (! (selectedField.getType().equals(templateField.getType()))) {
				throw new WrongFieldsForChosenTypeException();
			}
			
			if (! (selectedField.getName().equals(templateField.getName()))) {
				throw new WrongFieldsForChosenTypeException();
			}
		}
	}
	
	public boolean checkOwner(User owner) throws WrongUserForTaskTypeException
	{
		return true;
		//TODO: checken of de User een owner mag zijn?
	}

	/**
	 * Describable interface
	 */
	public String getDescription() {
		return this.getName();
	}
}
