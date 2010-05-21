package model;

import gui.Describable;

import java.util.GregorianCalendar;

public abstract class AssetAllocation implements Describable {
	
	protected Task task;
	
	public abstract boolean isAvailableAt(GregorianCalendar begin, int duration); 
	
	public Task getTask(){
		return this.task;
	}
	
	public abstract boolean hasOverlap(GregorianCalendar begin, int duration);
	
	public abstract boolean countsTowardsLimits(); 
	
	/**
	 * Destroy all bindings between the this allocation, the allocating task and the asset.
	 */
	public abstract void remove();
	
	public abstract AssetType getAssetType();
	
	public abstract AllocationType getAllocationType();

}
