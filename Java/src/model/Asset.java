/**
 * 
 */
package model;

import gui.Describable;

/**
 * A generalized resource.
 *
 */
public interface Asset extends Describable {
	
	public AssetType getType();

}
