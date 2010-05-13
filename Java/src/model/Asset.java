/**
 * 
 */
package model;

import java.util.GregorianCalendar;

import gui.Describable;

/**
 * A generalized resource.
 *
 */
public interface Asset extends Describable {
	
	public AssetType getType();

}
