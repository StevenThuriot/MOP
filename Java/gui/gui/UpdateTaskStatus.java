package gui;

import model.Task;
import model.User;

import controller.DispatchController;

public class UpdateTaskStatus extends UseCase {
	@Override
	public String getDescription() {
		return "Update Task Status";
	}
	
	public UpdateTaskStatus(){}
	
	private UpdateTaskStatus(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new UpdateTaskStatus(menu,dController,mainGUI.getCurrentUser())).updateTaskStatus();
	}
	
	public void startUseCase(Menu menu, DispatchController dController, User user, Task task) {
		(new UpdateTaskStatus(menu,dController,user)).updateTaskStatus(task);
	}
	
	private void updateTaskStatus(){
		updateTaskStatus(menu.menuGen("Select Task", dController.getTaskController().getTasks(user)));
	}

	private void updateTaskStatus(Task task) {
		int choice;
		menu.println(task.getDescription());
		if(!dController.getTaskController().hasDependentTasks(task))
			menu.printListGen("Dependent Tasks", dController.getTaskController().getDependentTasks(task));
		else
			menu.println("Dependent Tasks \n0: None");
		if(!dController.getTaskController().hasDependencies(task))
			menu.printListGen("Dependencies", dController.getTaskController().getDependencies(task));
		else
			menu.println("Dependcies \n0: None");
		if(!dController.getTaskController().hasRequiredResources(task))
			menu.printListGen("Required Resources", dController.getTaskController().getRequiredResources(task));
		else
			menu.println("Required Resources \n 0: None");
		menu.println("Start date: "+menu.format(task.getStartDate()) );
		menu.println("Due date: "+menu.format(task.getDueDate()) );
		menu.println("Duration: "+task.getDuration()+" Minutes");
		choice = menu.menu("Select Status", "Succesful", "Failed", "Unfinished");
		/*switch (choice) {
			case 0:
				newStatus = Status.Successful;
				break;
			case 1:
				newStatus = Status.Failed;
				break;
			case 2:
				newStatus = Status.Unfinished;
				break;
		} */
		/*
		try {
			dController.getTaskController().updateTaskStatus(task, newStatus);
		} catch (DependencyException e) {
			if(menu.dialogYesNo("Task has other depedent tasks, updating will change their stus aswell. Continue?"))
			{
				dController.getTaskController().updateDependantTasks(task, newStatus);
			}
		}*/
	}

}
