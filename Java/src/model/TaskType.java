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
	private ArrayList<TaskTypeResourceConstraint> resourceReq;
	
	/**
	 * Specifies the constraints relating to helper users.
	 */
	private ArrayList<TaskTypeHelperConstraint> helperReq;
	
	/**
	 * Specifies the constraints relating to the owner user.
	 */
	private ArrayList<TaskTypeOwnerConstraint> ownerReq;
		
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
	 * Indicates whether the requirements of this task type are 
	 * @return
	 */
	public boolean satisfiesConstraints(Task t){
		boolean satisfied = true;
		for(TaskTypeResourceConstraint constraint: resourceReq){
			satisfied = satisfied && constraint.satisfied(t);
		}
		
		for(TaskTypeHelperConstraint constraint: helperReq){
			satisfied = satisfied && constraint.satisfied(t);
		}
		
		for(TaskTypeOwnerConstraint constraint: ownerReq){
			satisfied = satisfied && constraint.satisfied(t);
		}
		return satisfied;
	}
	
	
}
