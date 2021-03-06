package model;

public interface Subject {
	
	/**
	 * Notify all subscribed Observers of changes to the Subject 
	 */
	public void publish();

}
