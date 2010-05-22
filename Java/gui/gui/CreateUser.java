package gui;

import model.UserType;
import controller.DispatchController;

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
		UserType selectedType = menu.menuGen("Select the type of user:", dController.getUserController().getAllUserTypes());
		String name = menu.prompt("What should the user be called?");
		dController.getUserController().createUser(selectedType,name);
	}

}
