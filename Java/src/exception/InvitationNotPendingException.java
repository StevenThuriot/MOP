package exception;

import model.Invitation.InvitationState;

@SuppressWarnings("serial")
public class InvitationNotPendingException extends Exception {

	private InvitationState state = InvitationState.ACCEPTED;
	public InvitationNotPendingException()
	{
		super("Can't update this invitation's state. It is already been accepted or declined.");
	}
	public InvitationNotPendingException(InvitationState status) {
		this();
		this.state = status;
	}
	public InvitationState getState()
	{
		return state;
	}
}
