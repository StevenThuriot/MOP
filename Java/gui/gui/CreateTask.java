package gui;

import model.Field;
import model.Project;
import model.Task;
import model.TaskTimings;
import model.TaskType;
import model.User;
import controller.DispatchController;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyListPassedToMenuException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;

public class CreateTask extends UseCase {
	public CreateTask(){		
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new CreateTask(menu, dController, mainGUI.getCurrentUser())).createTask();
	}
	
	private CreateTask(Menu menu,  DispatchController dController, User user){
		this.dController = dController;
		this.menu = menu;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Create Task";
	}
	
	@SuppressWarnings("unchecked")
	private void createTask(){
		String descr = menu.prompt("Give Task Description");
		
		TaskType type = null;
		try {
			type = menu.menuGen("Select task type", dController.getTaskController().getAllTypes());
		} catch (EmptyListPassedToMenuException e1) {
			menu.println("There are no tasktypes to select. Going back to menu.");
			return;
		}
		
		List<Field> taskFields = type.getTemplate();
		
		for (Field field : taskFields) {
			String prompt = menu.prompt(field.getName());
			
			switch (field.getType()) {
				case Numeric:
					int i = Integer.parseInt( prompt );
					field.setValue(i);
					break;
					
				case Text:
				default:
					field.setValue( prompt );
					break;
			}
		}		
		
		boolean hasDep = menu.dialogYesNo("Does this task have any dependancies?");
				
		ArrayList<Task> deps = new ArrayList<Task>();
		if(hasDep)
			try {
				deps = menu.menuGenMulti("Select dependency", dController.getTaskController().getTasks(user));
			} catch (EmptyListPassedToMenuException e2) {
				menu.println("There are no tasks to select as dependency.");
				hasDep = false;
			}
		
		GregorianCalendar startDate = menu.promptDate("Give start Date");
		GregorianCalendar dueDate = menu.promptDate("Give due date");
		int duration = Integer.parseInt(menu.prompt("Duration?"));
		
		TaskTimings timing = new TaskTimings(startDate, dueDate, duration);
		
		Project project;
		try {
			project = menu.menuGen("What project does the Task belong too?", 
					dController.getProjectController().getProjects());
		} catch (EmptyListPassedToMenuException e1) {
			menu.println("There are no projects to select. Going back to menu.");
			return;
		}
		
				
		if (hasDep){
			try {
				dController.getTaskController().createTask(descr, type, taskFields, user, timing, deps, project);
			} catch (EmptyStringException e) {
				menu.println("Empty description");
			} catch (BusinessRule1Exception e) {
				menu.println("Task not completable");
			} catch (DependencyCycleException e) {
				menu.println("Cyclic Dependency");
			} catch (NullPointerException e) {
				menu.println("Something very bad has happend");
				e.printStackTrace();
			} catch (IllegalStateCallException e) {
				menu.println("This call is not allowed on the current state.");
			} catch (BusinessRule3Exception e) {
				menu.println("This schedule would violate Business Rule 3. This is probably: starttime after the current time, or the deadline before the current time.");
			} catch (WrongFieldsForChosenTypeException e) {
				menu.println("The passed fields do not match the selected type");
			} catch (WrongUserForTaskTypeException e) {
				menu.println(e.getMessage());
			} catch (BusinessRule2Exception e) {
				menu.println("One the dependencies was Failed");
			}
		}else{
			try {
				dController.getTaskController().createTask(descr, type, taskFields, user, timing, project);
			} catch (EmptyStringException e) {
				menu.println("Empty description");
			} catch (BusinessRule1Exception e) {
				menu.println("Task not completable");
			} catch (NullPointerException e) {
				menu.println("Something very bad has happend");
				e.printStackTrace();
			} catch (BusinessRule3Exception e) {
				menu.println("This schedule would violate Business Rule 3. This is probably: starttime after the current time, or the deadline before the current time.");
			} catch (WrongFieldsForChosenTypeException e) {
				menu.println("The passed fields do not match the selected type");
			} catch (WrongUserForTaskTypeException e) {
				menu.println(e.getMessage());
			}
		}
	}
}
