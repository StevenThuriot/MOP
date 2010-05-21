package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.WrongFieldsForChosenTypeException;
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
	 * @return A list containing the fields needed to describe this type of Task.
	 */
	@SuppressWarnings("unchecked")
	public List<Field> getTemplate(){
		List<Field> clonedTemplate = (List<Field>) template.clone();
		
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
	 * 
	 * @param fields The fields filled in by the GUI
	 * @return A clone of itself with all the fields filled in
	 * @throws WrongFieldsForChosenTypeException
	 */
	@SuppressWarnings("unchecked")
	public TaskType setTemplate(List<Field> fields, User owner) throws WrongFieldsForChosenTypeException
	{
		this.checkFields(fields);	
		TaskType taskType = this.clone();
		taskType.template = new ArrayList<Field>(fields);
		
		//check constraints + check user
		return taskType;
		//else throw new MAG_NIET_EXCEPTION!
	}
	
	/**
	 * Returns a clone of itself
	 * @return title says it all
	 */
	@SuppressWarnings("unchecked")
	public TaskType clone() {	
		ArrayList<Field> fields = new ArrayList<Field>(this.getTemplate());
		ArrayList<TaskTypeConstraint> straints = (ArrayList<TaskTypeConstraint>) constraints.clone();
		return new TaskType(this.getName(), fields, straints);
	}


	/**
	 * Checking the fields consistency
	 * @param type
	 * @param fields
	 * @return
	 * @throws WrongFieldsForChosenTypeException 
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkFields(List<Field> fields) throws WrongFieldsForChosenTypeException
	{
		if (fields.size() != template.size())
			return false;
		
		for (int i = 0; i < template.size(); i++) 
		{
			Field selectedField = template.get(i);
			Field templateField = null;
			
			try {
				templateField = fields.get(i);
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
			
			if (! (selectedField.getType().equals(templateField.getType()))) {
				return false;
			}
			
			if (! (selectedField.getName().equals(templateField.getName()))) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Describable interface
	 */
	public String getDescription() {
		return this.getName();
	}
}
