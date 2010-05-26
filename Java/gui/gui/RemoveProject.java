package gui;

import model.Project;
import model.User;
import controller.DispatchController;

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

		dController.getProjectController().removeProject(choice);
	}

}
