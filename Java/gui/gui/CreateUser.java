package gui;

import model.UserType;
import controller.DispatchController;
import exception.EmptyListPassedToMenuException;

public class CreateUser extends UseCase {

	public CreateUser(){}
	
	private CreateUser(Menu menu,DispatchController dController)
	{
		this.menu = menu;
		this.dController = dController;
	}
	
	@Override
	public String getDescription() {
		return "Menu to create a user";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController,
			MainGUI mainGUI) {
		(new CreateUser(menu,dController)).show();
	}

	private void show() {
		UserType selectedType = null;
		try {
			selectedType = menu.menuGen("Select the type of user:", dController.getUserController().getAllUserTypes());
		} catch (EmptyListPassedToMenuException e) {
			menu.println("There are no usertypes to select. Going back to menu.");
			return;
		}
		String name = menu.prompt("What should the user be called?");
		dController.getUserController().createUser(selectedType,name);
	}

}
