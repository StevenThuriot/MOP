package model;

public class ResourceType implements AssetType{
	
	private String typeDescription;
	
	public ResourceType(String typeDescription){
		this.typeDescription = typeDescription;
	}
	
	public String getTypeDescription() {
		return typeDescription;
	}

	public String getDescription() {
		return getTypeDescription();
	}
	
	public String toString() {
		return getTypeDescription();
	}
	
	

	
	
}
