package gui;

import java.util.ArrayList;
import java.util.List;

import model.Resource;
import model.Task;
import model.User;
import controller.DispatchController;

public class FocusWork extends UseCase {
	
	public FocusWork(){}
	
	private FocusWork(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Focus Work";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new FocusWork(menu, dController,mainGUI.getCurrentUser())).focusWork();
	}
	
	private void focusWork() {
//		loop:
//		while (true) {
//			List<Task> tasks = dController.getTaskController().focusWork(user);
//			ArrayList<String> descr = new ArrayList<String>(tasks.size());
//			for (Task t : tasks) {
//				descr.add(t.getDescription());
//			}
//			// descr.add("Exit");
//			int choice = menu.menu("Select Task", descr);
//			/*
//			 * if(choice == descr.size()-1) return;
//			 */
//			Task task = tasks.get(choice);
//			menu.println(task.getDescription());
//			descr.clear();
//			for (Task t : dController.getTaskController().getDependentTasks(task)) {
//				descr.add(t.getDescription());
//			}
//			if (descr.isEmpty())
//				descr.add("None");
//			menu.printList("Dependent Tasks", descr);
//			descr.clear();
//			for (Task t : dController.getTaskController().getDependencies(task)) {
//				descr.add(t.getDescription());
//			}
//			if (descr.isEmpty())
//				descr.add("None");
//			menu.printList("Dependencies", descr);
//			descr.clear();
//			for (Resource r : dController.getTaskController().getRequiredResources(task)) {
//				descr.add(r.getDescription());
//			}
//			if (descr.isEmpty())
//				descr.add("None");
//			menu.printList("Required Resources", descr);
//			menu.println("Start date: " + menu.format(task.getStartDate()));
//			menu.println("Due date: " + menu.format(task.getDueDate()));
//			menu.println("Duration: " + task.getDuration() + " Minutes");
//			choice = menu.menu("Select Action", "Return to List", "Change Task Status", "Modify Task Details","Return to Menu");
//			switch (choice) {
//				case 0:
//					continue loop;
//				case 1:
//					(new UpdateTaskStatus()).startUseCase(menu, dController, user, task);
//					break;
//				case 2:
//					(new ModifyTaskDetails()).startUseCase(menu, dController, user, task);
//					break;
//				case 3:
//					break loop;
//			}
//		}
	}

}
