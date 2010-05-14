package model;

public class TaskTypeOwnerConstraint extends TaskTypeConstraint{
	
	/**
	 * The user type that this constraint specifies.
	 */
	private UserType userT;
	
	/**
	 * Specifies whether the constraint is satisfied. In order to satisfy
	 * this type of constraint, the specified task must be owned by a user of
	 * the specified user type.
	 */
	public boolean satisfied(Task task){
		UserType actual = (task.getOwner().getType());
		return actual.equals(userT);
		}

}
