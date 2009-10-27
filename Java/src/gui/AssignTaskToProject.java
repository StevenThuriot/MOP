package gui;

import java.util.ArrayList;
import java.util.List;

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
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.addAll(dController.getTaskController().getTasks(user));
		ArrayList<String> tDescrS = new ArrayList<String>(tasks.size());
		for (Task t : tasks) {
			tDescrS.add(t.getDescription());
		}
		int choice = menu.menu("Select task", tDescrS);
		Task task = tasks.get(choice);
		List<Project> projects = dController.getProjectController().getProjects(user);
		ArrayList<String> pDescr = new ArrayList<String>();
		for(Project p :  projects){
			pDescr.add(p.getDescription());
		}
		choice = menu.menu("Selct Project", pDescr);
		Project project = projects.get(choice);
		dController.getProjectController().bind(project, task);
	}

}
