package controller;

import java.util.Collections;
import java.util.List;

import exception.InvitationExistsException;
import exception.InvitationInvitesOwnerException;
import exception.InvitationNotPendingException;
import model.Invitation;
import model.Task;
import model.User;
import model.repositories.RepositoryManager;

/**
 * Controller that manages invitations
 * @author bart
 *
 */
public class InvitationController {
	
	private RepositoryManager manager;
	
	/**
	 * Default constructor
	 */
	public InvitationController(RepositoryManager manager)
	{
		if(manager == null)
			throw new NullPointerException();
		this.manager = manager;
	}
	
	/**
	 * Create a new invitation that couples <user> to <task>
	 * @param task
	 * @param user
	 * @throws InvitationExistsException
	 * @throws InvitationInvitesOwnerException 
	 */
	public Invitation createInvitation(Task task, User user) throws InvitationExistsException, InvitationInvitesOwnerException
	{
		return new Invitation(task,user);
	}
	
	/**
	 * Remove the Invitation from the model
	 * @param invitation
	 */
	public void removeInvitation(Invitation invitation)
	{
		invitation.remove();
	}
	
	/**
	 * Accept an invitation
	 * @throws InvitationNotPendingException 
	 */
	public void acceptInvitation(Invitation invitation) throws InvitationNotPendingException
	{
		invitation.accept();
	}
	
	/**
	 * Decline an invitation
	 * @throws InvitationNotPendingException 
	 */
	public void declineInvitation(Invitation invitation) throws InvitationNotPendingException
	{
		invitation.decline();
	}
	
	/**
	 * Returns all the users in the system
	 */
	public List<User> getAllUsers()
	{
		return Collections.unmodifiableList(manager.getUsers());
	}
}
