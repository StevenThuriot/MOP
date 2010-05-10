package exception;

@SuppressWarnings("serial")
public class InvitationInvitesOwnerException extends Exception {

	public InvitationInvitesOwnerException()
	{
		super("You can't invite the owner of a task to help on that task");
	}
}
