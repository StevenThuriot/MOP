package gui;

import model.User;
import controller.DispatchController;
import exception.EmptyStringException;

public class CreateProject extends UseCase {
	public CreateProject(){}
	
	private CreateProject(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Create Project";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new CreateProject(menu,dController,mainGUI.getCurrentUser())).createProject();
	}
	
	private void createProject(){
		try {
			dController.getProjectController().createProject(menu.prompt("Give Project Description"), user);
		} catch (EmptyStringException e) {
			System.out.println("Project Description Empty");
		}
	}

}
