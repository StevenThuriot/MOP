/**
 * 
 */
package model;

/**
 * @author koen
 *
 */
public class UserType implements AssetType {

private String typeDescription;
	
	public UserType(String typeDescription){
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
