package exception;

@SuppressWarnings("serial")
public class AssetConstraintFullException extends Exception {
	
	public AssetConstraintFullException(){
		super("The Asset Constraint for this AssetType is already full");
	}
	
	public AssetConstraintFullException(String message){
		super(message);
	}

}
