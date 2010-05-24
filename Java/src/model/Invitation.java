package model;

import java.util.GregorianCalendar;

import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.IllegalStateCallException;
import exception.InvitationInvitesOwnerException;
import exception.InvitationNotPendingException;
import gui.Describable;

/**
 * Invitation class that provides a link between a Helper User and a Task.
 * Invitation holds a status and provides communication between the User's Invitation manager and the Task's InvitationManager
 * @author bart
 *
 */
public class Invitation extends AssetAllocation implements Describable{
	public enum InvitationState {
		ACCEPTED,PENDING,DECLINED
	}
	
	/**
	 * User this invitation is created for
	 */
	private User user;
	
	/**
	 * Status indicator for this Invitation
	 */
	private InvitationState status;
	
	public Invitation(Task task,User user) throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		if(task==null || user==null)
			throw new NullPointerException();
		this.task = task;
		this.user = user;
		
		if(this.getUser().equals(this.getTask().getOwner()))
			throw new InvitationInvitesOwnerException();
		
		try {
			this.task.addAssetAllocation(this);
		} catch (IllegalStateCallException e) {
			throw new IllegalStateCallException("Invitation can not be made for finished task");
		}
		this.user.addInvitation(this);
		
		this.status = InvitationState.PENDING;
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
	public Task getTask()
	{
		return this.task;
	}
	
	/**
	 * Method to accept this invitation
	 * @throws InvitationNotPendingException 
	 */
	public void accept() throws InvitationNotPendingException
	{
		if(this.status == InvitationState.PENDING){
			this.status = InvitationState.ACCEPTED;
		}else{
			throw new InvitationNotPendingException(this.status);
		}
	}
	
	/**
	 * Method decline this invitation
	 * @throws InvitationNotPendingException 
	 */
	public void decline() throws InvitationNotPendingException
	{
		if(this.status == InvitationState.PENDING){
			this.status = InvitationState.DECLINED;
		}else{
			throw new InvitationNotPendingException(this.status);
		}
	}

	public InvitationState getState()
	{
		return this.status;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Invitation)
			if(this.user == ((Invitation) obj).user)
				if(this.task == ((Invitation) obj).task)
					return true;
		return false;
	}

	/**
	 * Destroy coupling with Task and User so this instance can be removed correctly
	 */
	public void remove() {
		task.removeAssetAllocation(this);
		user.removeInvitation(this);
		task = null;
		user = null;
	}
	
	public String toString()
	{
		return user.getName() + " is invited for " + task.getDescription() + ". State is " + this.getState().toString();
	}

	public String getDescription() {
		return this.toString();
	}

	@Override
	public boolean isAvailableAt(GregorianCalendar begin, int duration) {
		if(this.status == InvitationState.ACCEPTED)
			return true;
		return false;
	}

	/**
	 * Invitations shouldn't interfere with overlap so return true by default.
	 */
	@Override
	public boolean checkProposedAllocation(AssetAllocation assetAllocation) {
		return true;
	}

	@Override
	public UserType getAssetType() {
		return user.getType();
	}

	@Override
	public boolean countsTowardsLimits() {
		return (status == InvitationState.ACCEPTED || status == InvitationState.PENDING);
	}

	@Override
	public AllocationType getAllocationType() {
		return AllocationType.Invitation;
	}

	@Override
	protected GregorianCalendar getEarliestAvailableTime() {
		if(status == InvitationState.ACCEPTED)
			return new GregorianCalendar(0, 0, 0);
		else 
			return null;
	}
}
