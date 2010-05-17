package model;

import gui.Describable;

import java.util.GregorianCalendar;

public interface AssetAllocation extends Describable {
	
	public boolean isAvailableAt(GregorianCalendar begin, int duration); 
	
	public Task getTask();
	
	public boolean hasOverlap(GregorianCalendar begin, int duration);
	
	/**
	 * Destroy all bindings between the this allocation, the allocating task and the asset.
	 */
	public void remove();
	
	public AssetType getAssetType();

}
