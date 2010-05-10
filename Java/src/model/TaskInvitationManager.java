package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.InvitationExistsException;
import exception.InvitationInvitesOwnerException;

/**
 * A TaskInvitationManager is an object that keeps track of the 
 * helper invitations sent per task to other users
 * @author bart
 *
 */
public class TaskInvitationManager {
		/**
		 * The task this InvitationManager is assigned to
		 */
		private Task task;
		
		/**
		 * Invitations for this task
		 */
		private List<Invitation> invitations;
		
		/**
		 * Constructor that provides binding between InvitationManager and Task
		 * @param task
		 */
		public TaskInvitationManager(Task task) {
			this.task = task;
			this.invitations = new ArrayList<Invitation>();
		}

		protected void add(Invitation invitation) throws InvitationExistsException, InvitationInvitesOwnerException
		{
			if(this.alreadyInvited(invitation))
				throw new InvitationExistsException();
			if(invitation.getUser().equals(invitation.getTask().getUser()))
				throw new InvitationInvitesOwnerException();
			invitations.add(invitation);
		}
		
		protected void remove(Invitation invitation) {
			this.invitations.remove(invitation);
		}
		
		private boolean alreadyInvited(Invitation checkingInvitation) {
			for(Invitation iteratedInvitation:invitations)
				if(iteratedInvitation.equals(checkingInvitation))
					return true;
			return false;
		}

		/**
		 * Return an unmodifiableList of invitations already added to this Task
		 * @return
		 */
		public List<Invitation> getInvitations()
		{
			return Collections.unmodifiableList(this.invitations);
		}
		
		/**
		 * Return the task
		 * @return
		 */
		protected Task getTask()
		{
			return task;
		}

		
}
