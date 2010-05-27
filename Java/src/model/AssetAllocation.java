package model;

import gui.Describable;

import java.util.GregorianCalendar;

public abstract class AssetAllocation implements Describable {
	
	/**
	 * The task to which this AssetAllocation allocates an asset to.
	 */
	protected Task task;
	
	/**
	 * Returns whether the asset is available at the specified time.
	 * @param begin
	 * @param duration
	 * @return
	 */
	public abstract boolean isAvailableAt(GregorianCalendar begin, int duration); 
	
	/**
	 * Return the task to which this AssetAllocation allocates an asset to.
	 * @return
	 */
	public Task getTask(){
		return this.task;
	}
	
	/**
	 * Returns whether the proposed allocation can be made alongside this allocation.
	 * @param assetAllocation The proposed AssetAllocation
	 * @return default true;
	 */
	protected boolean checkProposedAllocation(AssetAllocation assetAllocation){
		return true;
	}
	
	/**
	 * Returns if this AssetAllocation count towards the Constraint Limit.
	 * @return
	 */
	protected abstract boolean isFailed(); 
	
	/**
	 * Destroy all bindings between the this allocation, the allocating task and the asset.
	 */
	protected abstract void remove();
	
	/**
	 * Return the type of the asset allocated.
	 * @return
	 */
	public abstract AssetType getAssetType();
	
//	/**
//	 * Return what allocation kind this is.
//	 */
//	public abstract AllocationType getAllocationType();
	
	/**
	 * Return the earliest time at which this allocation becomes available.
	 */
	protected abstract GregorianCalendar getEarliestAvailableTime();

}
