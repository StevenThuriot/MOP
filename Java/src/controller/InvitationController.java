package controller;

import exception.InvitationExistsException;
import model.Invitation;
import model.Task;
import model.User;

/**
 * Controller that manages invitations
 * @author bart
 *
 */
public class InvitationController {
	
	/**
	 * Default constructor
	 */
	public InvitationController()
	{
	}
	
	/**
	 * Create a new invitation that couples <user> to <task>
	 * @param task
	 * @param user
	 * @throws InvitationExistsException
	 */
	public void createInvitation(Task task, User user) throws InvitationExistsException
	{
		new Invitation(task,user);
	}
	
	/**
	 * Remove the Invitation from the model
	 * @param invitation
	 */
	public void removeInvitation(Invitation invitation)
	{
		invitation.remove();
	}
}
