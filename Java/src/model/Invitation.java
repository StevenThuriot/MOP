package model;

import exception.InvitationExistsException;

/**
 * Invitation class that provides a link between a Helper User and a Task.
 * Invitation holds a status and provides communication between the User's Invitation manager and the Task's InvitationManager
 * @author bart
 *
 */
public class Invitation {
	protected enum InvitationState {
		ACCEPTED,PENDING,DECLINED
	}

	/**
	 * Task this invitation is created for
	 */
	private Task task;
	
	/**
	 * User this invitation is created for
	 */
	private User user;
	
	/**
	 * Status indicator for this Invitation
	 */
	private InvitationState status;
	
	public Invitation(Task task,User user) throws InvitationExistsException
	{
		this.task = task;
		this.user = user;
		
		this.task.addInvitation(this);
		this.user.addInvitation(this);
	}
	
	/**
	 * Method for retrieving the user for this Invitation
	 * @return
	 */
	protected User getUser()
	{
		return this.user;
	}
	/**
	 * Method for retrieving the task for this Invitation
	 * @return
	 */
	protected Task getTask()
	{
		return this.task;
	}
	
	/**
	 * Method to accept this invitation
	 */
	protected void accept()
	{
		this.status = InvitationState.ACCEPTED;
	}
	/**
	 * Method to decline this invitation
	 */
	protected void decline()
	{
		this.status = InvitationState.DECLINED;
	}

	public InvitationState getState()
	{
		return this.status;
	}

	public boolean equals(Invitation obj) {
		if (this == obj)
			return true;
		if(this.user == obj.user)
			if(this.task == obj.task)
				return true;
		return false;
	}
	
	
}
