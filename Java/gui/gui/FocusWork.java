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
        //Which kind of Focus would we like to see?
        // 1. Deadline-based
        // 2. Duration-based
        int choice = menu.menu("How would you like your tasks to be shown?", "Deadline Based","Duration Based","Default");
        model.focus.FocusWork focus;
        switch(choice)
        {
            case 0:
                focus = FocusFactory.createFocus(FocusType.DeadlineFocus, user, Integer.parseInt(menu.prompt("How many tasks would you like to see?")), 0);
                break;
            case 1:
                focus = FocusFactory.createFocus(FocusType.DurationFocus, user, Integer.parseInt(menu.prompt("Minimum duration of a task?")), Integer.parseInt(menu.prompt("Maximum duration of a task?")));
                break;
            default:
                focus = FocusFactory.createFocus(FocusType.Default, user, 0, 0);
        }
		loop:
		while (true) {

			List<Task> tasks = focus.getTasks();
			ArrayList<String> descr = new ArrayList<String>(tasks.size());
			for (Task t : tasks) {
				descr.add(t.getDescription());
			}
			// descr.add("Exit");
			choice = menu.menu("Select Task", descr);
			/*
			 * if(choice == descr.size()-1) return;
			 */
			Task task = tasks.get(choice);
			menu.println(task.getDescription());
			descr.clear();
			for (Task t : dController.getTaskController().getDependentTasks(task)) {
				descr.add(t.getDescription());
			}
			if (descr.isEmpty())
				descr.add("None");
			menu.printList("Dependent Tasks", descr);
			descr.clear();
			for (Task t : dController.getTaskController().getDependencies(task)) {
				descr.add(t.getDescription());
			}
			if (descr.isEmpty())
				descr.add("None");
			menu.printList("Dependencies", descr);
			descr.clear();
			for (Resource r : dController.getTaskController().getRequiredResources(task)) {
				descr.add(r.getDescription());
			}
			if (descr.isEmpty())
				descr.add("None");
			menu.printList("Required Resources", descr);
			menu.println("Start date: " + menu.format(task.getStartDate()));
			menu.println("Due date: " + menu.format(task.getDueDate()));
			menu.println("Duration: " + task.getDuration() + " Minutes");
			choice = menu.menu("Select Action", "Change type of focus", "Return to List", "Change Task Status", "Modify Task Details","Return to Menu");
			switch (choice) {
			    case 0:
			        focusWork();
			        break loop;
				case 1:
					continue loop;
				case 2:
					(new UpdateTaskStatus()).startUseCase(menu, dController, user, task);
					break;
				case 3:
					(new ModifyTaskDetails()).startUseCase(menu, dController, user, task);
					break;
				case 4:
					break loop;
			}
		}
	}

}
