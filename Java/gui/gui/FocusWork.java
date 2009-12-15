package gui;

import java.util.ArrayList;
import java.util.List;

import model.Resource;
import model.Task;
import model.User;
import model.focus.DeadlineFocus;
import model.focus.DurationFocus;
import model.focus.FocusStrategy;
import controller.DispatchController;
import controller.FocusFactory;
import controller.FocusFactory.FocusType;

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
		focus: while (true) {
			//Which kind of Focus would we like to see?
			// 1. Deadline-based
			// 2. Duration-based
			int choice = menu.menu("How would you like your tasks to be shown?", "Deadline Based", "Duration Based", "Default");
			model.focus.FocusWork focus;
			switch (choice) {
				case 0:
					focus = FocusFactory.createFocus(FocusType.DeadlineFocus, user, Integer.parseInt(menu.prompt("How many tasks would you like to see?")), 0);
					break;
				case 1:
					focus = FocusFactory.createFocus(FocusType.DurationFocus, user, Integer.parseInt(menu.prompt("Minimum duration of a task?")), Integer
							.parseInt(menu.prompt("Maximum duration of a task?")));
					break;
				default:
					focus = FocusFactory.createFocus(FocusType.Default, user, 0, 0);
			}
			loop: while (true) {
				List<Task> tasks = focus.getTasks();
				Task task = menu.menuGen("Select Task", tasks);
				menu.println(task.getDescription());
				if (!dController.getTaskController().hasDependentTasks(task))
					menu.printListGen("Dependent Tasks", dController.getTaskController().getDependentTasks(task));
				else
					menu.println("Dependent Tasks \n0: None");
				if (dController.getTaskController().hasDependencies(task))
					menu.printListGen("Dependencies", dController.getTaskController().getDependencies(task));
				else
					menu.println("Dependencies \n0: None");
				if (dController.getTaskController().hasRequiredResources(task))
					menu.printListGen("Required Resources", dController.getTaskController().getRequiredResources(task));
				else
					menu.println("Required Resources \n0: None");
				menu.println("Start date: " + menu.format(task.getStartDate()));
				menu.println("Due date: " + menu.format(task.getDueDate()));
				menu.println("Duration: " + task.getDuration() + " Minutes");
				choice = menu.menu("Select Action", "Change type of focus", "Return to List", "Change Task Status", "Modify Task Details", "Return to Menu");
				switch (choice) {
					case 0:
						continue focus;
					case 1:
						continue loop;
					case 2:
						(new UpdateTaskStatus()).startUseCase(menu, dController, user, task);
						break;
					case 3:
						(new ModifyTaskDetails()).startUseCase(menu, dController, user, task);
						break;
					case 4:
						break focus;
				}
			}
		}
	}

}
