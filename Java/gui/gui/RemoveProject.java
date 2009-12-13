package gui;

import java.util.ArrayList;
import java.util.List;

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
		List<Project> projects = dController.getProjectController().getProjects(user);
		ArrayList<String> pDescr = new ArrayList<String>();
		for(Project p :  projects){
			pDescr.add(p.getDescription());
		}
		pDescr.add("None");
		int choice = menu.menu("Select Task to remove", pDescr);
		if(choice == pDescr.size()-1)
			return;
		dController.getProjectController().removeProject(projects.get(choice));
	}

}
