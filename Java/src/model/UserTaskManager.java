package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Invitation.InvitationState;

/**
 * Class that provides functionality for the User's Task
 * Manages both tasks the user is owner of, aswell as tasks 
 * he works on as a helper
 * @author bart
 *
 */
public class UserTaskManager {
	
	/**
	 * User this Manager belongs to
	 */
	private User user;
	
	/**
	 * Invitations the user has. Pending or Accepted
	 */
	private List<Invitation> invitations;
	
	/**
	 * Tasks this user owns
	 */
	private List<Task> ownedTasks;
	
	/**
	 * Constructor taking a user as argument
	 * @param user2
	 */
	public UserTaskManager(User user) {
		this.user = user;
		this.ownedTasks = new ArrayList<Task>();
		this.invitations = new ArrayList<Invitation>();
	}
	/**
	 * Get all the Tasks this user owns
	 * @return
	 */
	protected List<Task> getOwnedTasks() {
		return Collections.unmodifiableList(this.ownedTasks);
	}
	/**
	 * Get the pending or accepted invitations for this user
	 * Declined invitations are not shown
	 * @return
	 */
	protected List<Invitation> getInvitations() {
		ArrayList<Invitation> pendingOrAcceptedInvitations = new ArrayList<Invitation>();
		for(Invitation invi:this.invitations)
			if(!invi.getState().equals(InvitationState.PENDING))
				pendingOrAcceptedInvitations.add(invi);
		return Collections.unmodifiableList(pendingOrAcceptedInvitations);
	}
	/**
	 * Get all the tasks this user owns aswell as helps on
	 * @return
	 */
	protected List<Task> getTasks()
	{
		ArrayList<Task> allTasks = new ArrayList<Task>();
		for(Task task:this.ownedTasks)
			allTasks.add(task);
		for(Invitation invitation:this.invitations)
			if(invitation.getState()==InvitationState.ACCEPTED)
				allTasks.add(invitation.getTask());
		return Collections.unmodifiableList(allTasks);
	}
	/**
	 * Add a task to the user as owner
	 * @param task
	 */
	protected void add(Task task)
	{
		this.ownedTasks.add(task);
	}
	/**
	 * Add an invitation to the user
	 * @param invitation
	 */
	protected void add(Invitation invitation)
	{
		this.invitations.add(invitation);
	}
	/**
	 * Remove a task this user owns
	 * @param task
	 */
	protected void remove(Task task)
	{
		this.ownedTasks.remove(task);
	}
	/**
	 * Remove the invitation from the User
	 * @param invitation
	 */
	protected void remove(Invitation invitation)
	{
		this.invitations.remove(invitation);
	}
	
	/**
	 * Return the current user
	 * @return
	 */
	protected User getUser()
	{
		return this.user;
	}
	
}
