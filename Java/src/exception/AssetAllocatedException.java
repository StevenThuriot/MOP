package exception;

@SuppressWarnings("serial")
public class AssetAllocatedException extends Exception{

	public AssetAllocatedException()
	{
		super("Invitation was already created for this combination User/Task");
	}
	public AssetAllocatedException(String message)
	{
		super(message);
	}
}
