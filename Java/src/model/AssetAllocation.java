package model;

import java.util.GregorianCalendar;

public interface AssetAllocation {
	
	public boolean isAvailableAt(GregorianCalendar begin, int duration); 
	
	public Task getTask();
	
	public boolean hasOverlap(GregorianCalendar begin, int duration);

}
