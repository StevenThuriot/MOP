package gui;

import model.Project;
import model.Task;
import model.User;
import controller.DispatchController;

public class AssignTaskToProject extends UseCase {
	
	public AssignTaskToProject(){}
	
	private AssignTaskToProject(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Assign Task to Project";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new AssignTaskToProject(menu, dController, mainGUI.getCurrentUser())).assignTaskToProject();
	}
	
	private void assignTaskToProject(){
		Task task = menu.menuGen("Select task", dController.getTaskController().getTasks(user));
		Project project = menu.menuGen("Selct Project", dController.getProjectController().getProjects());
		dController.getProjectController().bind(project, task);
	}

}
