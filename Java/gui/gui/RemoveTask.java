package gui;

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
		Task choice = menu.menuGenOpt("Select Task to remove", dController.getTaskController().getTasks(user),"None");
		if(choice == null)
			return;
		if(!dController.getTaskController().hasDependentTasks( choice ) || (dController.getTaskController().hasDependentTasks( choice )
				&& menu.dialogYesNo("Task has dependant tasks, if you remove you remove them all. Continue?")) ){
			try {
				dController.getTaskController().removeTask( choice );
			} catch (IllegalStateCall e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}