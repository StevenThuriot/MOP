package model;

public class ResourceType implements AssetType{
	
	private String typeDescription;
	
	public ResourceType(String typeDescription){
		this.typeDescription = typeDescription;
	}
	
	@Override
	public String getTypeDescription() {
		return typeDescription;
	}

	@Override
	public String getDescription() {
		return getTypeDescription();
	}
	
	public String toString() {
		return getTypeDescription();
	}
	
	

	
	
}
