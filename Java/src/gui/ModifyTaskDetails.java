package gui;

import model.User;
import controller.DispatchController;

public class ModifyTaskDetails extends UseCase {
	public ModifyTaskDetails(){}
	
	private ModifyTaskDetails(Menu menu, DispatchController dController, User user) {
		this.menu = menu;
		this.user = user;
		this.dController = dController;
	}

	@Override
	public String getDescription() {
		return "Modify Task Details";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new ModifyTaskDetails(menu,dController,mainGUI.getCurrentUser())).modifyTaskDetails();
	}
	
	private void modifyTaskDetails(){
		
	}

}
