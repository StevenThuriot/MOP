package model;

import gui.Describable;

import java.util.GregorianCalendar;

public abstract class AssetAllocation implements Describable {
	
	protected Task task;
	
	public abstract boolean isAvailableAt(GregorianCalendar begin, int duration); 
	
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
	
	protected abstract boolean countsTowardsLimits(); 
	
	/**
	 * Destroy all bindings between the this allocation, the allocating task and the asset.
	 */
	protected abstract void remove();
	
	public abstract AssetType getAssetType();
	
	public abstract AllocationType getAllocationType();
	
	protected GregorianCalendar getEarliestAvailableTime(){
		return new GregorianCalendar(0, 0, 0);
	}

}
