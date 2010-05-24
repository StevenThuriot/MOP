package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
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
	
	/**
	 * Checks whether the constraints are satisfied for said Task at the specified timings
	 */
	protected boolean checkConstraints(Task task, GregorianCalendar begin, int duration){
		boolean isSatisfied = true;
		for(TaskTypeConstraint constraint: this.getConstraints()){
			isSatisfied &= constraint.checkConstraint(task,begin,duration);
		}
		return isSatisfied;
	}
	
	/**
	 * Returns whether the AssetType is required for Tasks of this Type
	 */
	protected boolean isAssetTypeRequired(AssetType assetType){
		ArrayList<AssetType> types = new ArrayList<AssetType>();
		for(TaskTypeConstraint constraint: this.getConstraints()){
			types.add(constraint.getAssetType());
		}
		return types.contains(assetType);
	}
	
	/**
	 * Returns whether the Asset Constraint is full.
	 */
	protected boolean isAssetConstraintFull(Task task, AssetType assetType){
		for(TaskTypeConstraint constraint: this.getConstraints()){
			if(constraint.getAssetType() == assetType){
				return constraint.isAssetConstraintFull(task);
			}
		}
		return false;
	}
	
	/**
	 * Returns earliest execution for assets based on constraints
	 */
	protected GregorianCalendar getEarliestAssetConstrExecTime(Task task){
		GregorianCalendar earliest = new GregorianCalendar(0, 0, 0);
		for(TaskTypeConstraint constraint : this.getConstraints()){
			GregorianCalendar temp =  constraint.getEarliestExecTime(task);
			if(temp != null){
				if(temp.after(earliest))
					earliest = temp;
			}else
				return null;
			
		}
		return earliest;
	}
}
