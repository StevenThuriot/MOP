package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import exception.AssetAllocatedException;

/**
 * A TaskAssetManager is an object that keeps track of the 
 * helper invitations sent per task to other users
 * @author bart
 *
 */
public class TaskAssetManager {
		/**
		 * The task this AssetManager is assigned to
		 */
		private Task task;
		
		/**
		 * Invitations for this task
		 */
		private List<AssetAllocation> assetAllocations;
		
		/**
		 * Constructor that provides binding between AssetManager and Task
		 * @param task
		 */
		public TaskAssetManager(Task task) {
			this.task = task;
			this.assetAllocations = new ArrayList<AssetAllocation>();
		}

		protected void add(AssetAllocation assetAllocation) throws AssetAllocatedException
		{
			if(this.alreadyAllocated(assetAllocation))
				throw new AssetAllocatedException();
			assetAllocations.add(assetAllocation);
		}
		
		protected void remove(AssetAllocation assetAllocation) {
			this.assetAllocations.remove(assetAllocation);
		}
		
		/**
		 * Removes all resource allocations. It is the allocation's resposibilty to remove all it's bindings. 
		 */
		protected void removeAll(){
			while(!assetAllocations.isEmpty()){
				assetAllocations.get(0).remove();
			}
		}
		
		private boolean alreadyAllocated(AssetAllocation checkingAssetAllocation) {
			for(AssetAllocation iteratedAssetAllocation:assetAllocations)
				if(iteratedAssetAllocation.equals(checkingAssetAllocation))
					return true;
			return false;
		}

		/**
		 * Return an unmodifiableList of asset allocations already added to this Task
		 * @return
		 */
		public List<AssetAllocation> getAssetAllocations()
		{
			return Collections.unmodifiableList(this.assetAllocations);
		}
		
		/**
		 * Return the task
		 * @return
		 */
		protected Task getTask()
		{
			return task;
		}
		
		/**
		 * Checks whether there is an overlap between these timings and the allocated assets.
		 * @pre All current AssetAllocations are overlapping.
		 */
		protected Boolean checkOverlap(GregorianCalendar begin, int duration) {

			boolean hasOverlap = true;
			for(AssetAllocation asset: getAssetAllocations()){
				hasOverlap &= asset.hasOverlap(begin, duration);
			}
			return hasOverlap;
		}
		
//		protected Boolean assetsAvailableAt(GregorianCalendar begin, int duration){
//			//TODO implement me.
//			return true;
//		}
		
		protected Map<AssetType,Integer> getAssetsAvailableAt(GregorianCalendar begin, int duration){
			HashMap<AssetType,Integer> assetMap = new HashMap<AssetType, Integer>();
			for(AssetAllocation allocation:assetAllocations){
				Integer typeCount = assetMap.get(allocation.getAssetType());
				if(typeCount==null)
					typeCount = 0;
				typeCount++;
				assetMap.put(allocation.getAssetType(), typeCount);
			}
			return assetMap;
		}
		
		

		
}