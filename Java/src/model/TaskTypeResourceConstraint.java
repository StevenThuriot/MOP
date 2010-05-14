package model;

public class TaskTypeResourceConstraint extends TaskTypeConstraint {

	/**
	 * The resource type that this constraint specifies.
	 */
	private ResourceType resourceT;
	
	/**
	 * The amount of resources required.
	 */
	private int amount;
	
	/**
	 * Specifies whether the constraint is satisfied. In order to satisfy
	 * this type of constraint, the specified task must have access to 
	 * the required amount of resource of the specified type.
	 */
	public boolean satisfied(Task t){
		//TODO!
		return false;
	}
}
