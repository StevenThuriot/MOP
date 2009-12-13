package gui;

import java.util.ArrayList;

import model.Task;
import model.User;
import controller.DispatchController;
import exception.IllegalStateCall;

public class RemoveTask extends UseCase {

	public RemoveTask() {
	}

	private RemoveTask(Menu menu, DispatchController dController, User user) {
		this.menu = menu;
		this.user = user;
		this.dController = dController;
	}

	@Override
	public String getDescription() {
		return "Remove Task";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new RemoveTask(menu, dController, mainGUI.getCurrentUser())).removeTask();
	}

	private void removeTask() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.addAll(dController.getTaskController().getTasks(user));
		ArrayList<String> tDescrS = new ArrayList<String>(tasks.size());
		for (Task t : tasks) {
			tDescrS.add(t.getDescription());
		}
		tDescrS.add("None");
		int choice = menu.menu("Select Task to remove", tDescrS);
		if(choice == tDescrS.size()-1)
			return;
		if(!dController.getTaskController().hasDependentTasks( tasks.get(choice) ) || (dController.getTaskController().hasDependentTasks( tasks.get(choice) )
				&& menu.dialogYesNo("Task has dependant tasks, if you remove you remove them all. Continue?")) ){
			try {
				dController.getTaskController().removeTask( tasks.get(choice) );
			} catch (IllegalStateCall e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

}
