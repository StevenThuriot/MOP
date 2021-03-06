package gui;

import java.util.ArrayList;
import java.util.List;

import model.Field;
import model.Task;
import model.TaskType;
import model.User;
import controller.DispatchController;
import controller.FocusController;
import model.focus.FocusType;
import exception.ArrayLengthException;
import exception.EmptyListPassedToMenuException;

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
	
	@SuppressWarnings("unchecked")
	private void focusWork() {
		focus: while (true) {
			//Which kind of Focus would we like to see?
			// 1. Deadline-based
			// 2. Duration-based
			// 3. Type-based
			
			List<String> focusTypes = new ArrayList<String>();
			for (FocusType focusType : FocusType.values()) {
				focusTypes.add(focusType.toString());
			}
			
			int choice=0;
			try {
				choice = menu.menu("How would you like your tasks to be shown?", focusTypes);
			} catch (EmptyListPassedToMenuException e1) {
			}
			model.focus.FocusWork focus;
			FocusController fController = dController.getFocusController();
			
			Object[] settings;
			try {
				switch (choice) {
					case 0:
						String numberOfTasksString = menu.prompt("How many tasks would you like to see?");
						int numberOfTasksInteger = Integer.parseInt(numberOfTasksString);
						
						settings = new Object[] {numberOfTasksInteger};
						
						focus = fController.createFocus(FocusType.DeadlineFocus, user, settings);
					
						break;
					case 1:
						String minDurationString = menu.prompt("Minimum duration of a task?");
						int minDurationInteger = Integer.parseInt(minDurationString);
						
						String maxDurationString = menu.prompt("Maximum duration of a task?");
						int maxDurationInteger = Integer.parseInt(maxDurationString);
						
						settings = new Object[] {minDurationInteger, maxDurationInteger};
						
						focus = fController.createFocus(FocusType.DurationFocus, user, settings);
						break;
					case 2:
					TaskType selectedTaskType = null;
						try {
							selectedTaskType = menu.menuGen("Select a tasktype to filter on", dController.getTaskController().getAllTypes());
						} catch (EmptyListPassedToMenuException e) {
							menu.println("There are no tasktypes to select. Going back to menu.");
							return;
						}
						int amountOfTasks = Integer.parseInt(menu.prompt("How many tasks would you like to see?"));
						settings = new Object[] {amountOfTasks,selectedTaskType};
						focus = fController.createFocus(FocusType.TaskTypeFocus, user, settings);
						break;
					default:
						focus = fController.createFocus(FocusType.Default, user, new Object[0]);
				}
				
				List<Task> tasks = focus.getTasks();
				
				if (tasks.size() > 0) 
				{
					loop: while (true) {
					
					Task task = null;
					try {
						task = menu.menuGen("Select Task", tasks);
					} catch (EmptyListPassedToMenuException e) {
						menu.println("There are no tasks to select. Going back to menu.");
						return;
					}
					menu.println(task.getDescription());
					
					List<Field> fields = task.getFields();
					if (fields.size() > 0)
					{
						menu.println("Fields");
						
						for (int i = 0; i < fields.size(); i++) {
							menu.println(i + ": " + fields.get(i).getName() + " ( " + fields.get(i).getType() + " ): \"" + fields.get(i).getValue() + "\"");
						}
					}
					
					if (dController.getTaskController().hasDependentTasks(task))
						menu.printListGen("Dependent Tasks", dController.getTaskController().getDependentTasks(task));
					else
						menu.println("Dependent Tasks \n0: None");
					if (dController.getTaskController().hasDependencies(task))
						menu.printListGen("Dependencies", dController.getTaskController().getDependencies(task));
					else
						menu.println("Dependencies \n0: None");
					if (dController.getTaskController().hasRequiredAssets(task))
						menu.printListGen("Required Resources", dController.getTaskController().getRequiredAssets(task));
					else
						menu.println("Required Resources \n0: None");
						menu.println("Start date: " + menu.format(task.getStartDate()));
						menu.println("Due date: " + menu.format(task.getDueDate()));
						menu.println("Duration: " + task.getDuration() + " Minutes");
						try {
							choice = menu.menu("Select Action", "Change type of focus", "Return to List", "Change Task Status", "Modify Task Details", "Return to Menu");
						} catch (EmptyListPassedToMenuException e) {
						}
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
				} else {
					System.out.println("No tasks has been found with the selected settings.");
					return;
				}
				
			} catch (ArrayLengthException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
