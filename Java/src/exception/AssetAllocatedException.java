package exception;

@SuppressWarnings("serial")
public class AssetAllocatedException extends Exception{

	public AssetAllocatedException()
	{
		super("This AssetAllocation was already allocated");
	}
	public AssetAllocatedException(String message)
	{
		super(message);
	}
}
