package gui;



import controller.DispatchController;

import model.User;

public abstract class UseCase implements Describable{
	
	protected Menu menu;
	protected User user;
	protected DispatchController dController;

	/**
	 * @param menu
	 * @param dController
	 * @param mainGUI
	 */
	public abstract void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI);
	
	/**
	 * @return
	 */
	public  abstract String getDescription();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return getDescription();
	}
	

}
