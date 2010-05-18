package model;

public class TaskTypeConstraint {
	
	/**
	 * The required AssetType.
	 */
	private AssetType assetT;
	
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
		assetT = type;
		this.min = min;
		this.max = max;
	}
	
	public AssetType getAssetType(){
		return assetT;
	}
	
	public int getMinimum(){
		return min;
	}
	
	public int getMaximum(){
		return max;
	}
	
}
