package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskType {

	/**
	 * A name describing this type of Task.
	 */
	private String name;
	
	/**
	 * An ArrayList containing the Fields describing a Task of this type.
	 */
	private ArrayList<Field> fields;
	
	/**
	 * Specifies the constraints relating to resources.
	 */
	private ArrayList<TaskTypeConstraint> constraints;

		
	
	public TaskType(String name, ArrayList<Field> fields, 
			ArrayList<TaskTypeConstraint> constraints){
		this.name = name;
		this.fields = fields;
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
	public List<Field> getTemplate(){
		return Collections.unmodifiableList(fields);
	}
	
	/**
	 * @return An unmodifiable list containing the constraints specified
	 * 			in this TaskType
	 */
	public List<TaskTypeConstraint> getConstraints(){
		return Collections.unmodifiableList(constraints);
	}
	
	
}
