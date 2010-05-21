package model;

import gui.Describable;

public class TaskTypeConstraint implements Describable{
	
	/**
	 * The required AssetType.
	 */
	private AssetType assetType;
	
	/**
	 * The minimum amount of assets of specified type required.
	 */
	private int min;
	
	/**
	 * The maximum amount of assets of specified type required.
	 */
	private int max;
	
	/**
	 * Creates a TaskTypeConstraint for a given AssetType <type>
	 * and a given amount <nb>
	 */
	public TaskTypeConstraint(AssetType type, int min, int max){
		assetType = type;
		this.min = min;
		this.max = max;
	}
	
	public AssetType getAssetType(){
		return assetType;
	}
	
	public int getMinimum(){
		return min;
	}
	
	public int getMaximum(){
		return max;
	}

	@Override
	public String getDescription() {
		return assetType.getDescription() + " min: " + getMinimum() + " max: " + getMaximum();
	}
}
