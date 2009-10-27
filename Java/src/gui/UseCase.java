package gui;



import controller.DispatchController;

import model.User;

public abstract class UseCase {
	
	protected Menu menu;
	protected User user;
	protected DispatchController dController;

	public abstract void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI);
	
	public  abstract String getDescription();
	
	public String toString(){
		return getDescription();
	}
	

}
