package exception;

@SuppressWarnings("serial")
public class AssetTypeNotRequiredException extends Exception {
	
	public AssetTypeNotRequiredException()
	{
		super("This AssetType is not required by this Task");
	}
	public AssetTypeNotRequiredException(String message)
	{
		super(message);
	}

}
