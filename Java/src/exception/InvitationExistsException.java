package exception;

@SuppressWarnings("serial")
public class InvitationExistsException extends Exception{

	public InvitationExistsException()
	{
		super("Invitation was already created for this combination User/Task");
	}
	public InvitationExistsException(String message)
	{
		super(message);
	}
}
