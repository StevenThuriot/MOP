package gui;

import java.util.ArrayList;

import model.Resource;
import model.Status;
import model.Task;
import model.User;

import controller.DispatchController;
import exception.DependencyException;

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
	
	private void updateTaskStatus(){
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.addAll(dController.getTaskController().getTasks(user));
		ArrayList<String> descr = new ArrayList<String>(tasks.size());
		for (Task t : tasks) {
			descr.add(t.getDescription());
		}
		int choice = menu.menu("Select Task", descr);
		Task task = tasks.get(choice);
		menu.println(task.getDescription());
		descr.clear();
		for (Task t : dController.getTaskController().getDependentTasks(task)) {
			descr.add(t.getDescription());
		}
		if(descr.isEmpty())
			descr.add("None");
		menu.printList("Dependent Tasks", descr);
		descr.clear();
		for (Task t : dController.getTaskController().getDependencies(task)) {
			descr.add(t.getDescription());
		}
		if(descr.isEmpty())
			descr.add("None");
		menu.printList("Dependencies", descr);
		descr.clear();
		for (Resource r : dController.getTaskController().getRequiredResources(task)) {
			descr.add(r.getDescription());
		}
		if(descr.isEmpty())
			descr.add("None");
		menu.printList("Required Resources", descr);
		menu.println("Start date: "+menu.format(task.getStartDate()) );
		menu.println("Due date: "+menu.format(task.getDueDate()) );
		menu.println("Duration: "+task.getDuration()+" Minutes");
		choice = menu.menu("Select Status", "Succesful", "Failed", "Unfinished");
		Status newStatus = null;
		switch (choice) {
			case 0:
				newStatus = Status.Successful;
				break;
			case 1:
				newStatus = Status.Failed;
				break;
			case 2:
				newStatus = Status.Unfinished;
				break;
		}
		try {
			dController.getTaskController().updateTaskStatus(task, newStatus);
		} catch (DependencyException e) {
			if(menu.dialogYesNo("Task has other depedent tasks, updating will change their stus aswell. Continue?"))
				dController.getTaskController().updateDependantTasks(task, newStatus);
		}
	}

}
