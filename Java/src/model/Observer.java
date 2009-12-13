package model;

/**
 * @author koen
 *
 * @param <S extends Subject> Type of Subject the Observer is watching. 
 */
public interface Observer<S extends Subject> {
	
	/**
	 * Notify this Observer of changes to the Subject.
	 * Observer should first verify it is subscribed to the Subject to ensure consistency.
	 * @param subject Subject whose state change cause this update to be called.
	 */
	public void update(S subject);

}
