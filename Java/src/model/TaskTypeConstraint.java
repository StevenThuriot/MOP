package model;

import java.util.GregorianCalendar;

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
	
	/**
	 * Checks whether the constraint is satisfied for said Task at the specified timings
	 */
	protected boolean checkConstraint(Task task, GregorianCalendar begin, int duration){
		int count = task.getAssetCountAvailableAt(begin, duration, this.getAssetType());
		return (this.getMinimum() <= count) && (count <= this.getMaximum());
	}
	
	/**
	 * Check if this Constraint is already full in the specified Task.
	 */
	protected boolean isAssetConstraintFull(Task task){
		return (task.getValidAssetCount(this.getAssetType()) == this.getMaximum());
	}
	
}
