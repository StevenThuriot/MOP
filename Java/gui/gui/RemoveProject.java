package gui;

import model.Project;
import model.User;
import controller.DispatchController;
import exception.IllegalStateCallException;

public class RemoveProject extends UseCase {
	public RemoveProject(){}
	
	private RemoveProject(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Remove Project";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new RemoveProject(menu, dController, mainGUI.getCurrentUser())).removeProject();
	}

	private void removeProject() {
		Project choice = menu.menuGenOpt("Select Task to remove", dController.getProjectController().getProjects(), "None");
		if(choice == null)
			return;
		try {
			dController.getProjectController().removeProject(choice);
		} catch (IllegalStateCallException e) {
			menu.println("This project can not be removed. This would bring tasks depending on this project in an inconsistent state.");
		}
	}

}
