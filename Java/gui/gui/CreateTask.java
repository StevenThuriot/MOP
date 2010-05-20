package gui;

import model.Field;
import model.FieldType;
import model.Resource;
import model.Task;
import model.TaskTimings;
import model.TaskType;
import model.User;
import controller.DispatchController;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.WrongFieldsForChosenTypeException;

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
		TaskType type =  menu.menuGen("Select task type", dController.getTaskController().getAllTypes());
		
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
		
		boolean hasDep = menu.dialogYesNo("I can has Dependency?");
				
		ArrayList<Task> deps = new ArrayList<Task>();
		if(hasDep)
			deps = menu.menuGenMulti("Select dependency", dController.getTaskController().getTasks(user));
		
		boolean hasRes = menu.dialogYesNo("I can has Resource?");
		ArrayList<Resource> reqRes = new ArrayList<Resource>();
		if(hasRes)
			reqRes = menu.menuGenMulti("Select dependancy", dController.getResourceController().getResources());
		
		
		GregorianCalendar startDate = menu.promptDate("Give start Date");
		GregorianCalendar dueDate = menu.promptDate("Give due date");
		int duration = Integer.parseInt(menu.prompt("Duration?"));
		
		TaskTimings timing = new TaskTimings(startDate, dueDate, duration);
		
				
		if (hasDep || hasRes){
			try {
				dController.getTaskController().createTask(type, taskFields, user, timing, deps);
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
				menu.println("Nice try mate, this call is not allowed on the current state.");
			} catch (BusinessRule3Exception e) {
				menu.println("This schedule would violate Business Rule 3. This is probably: starttime after the current time, or the deadline before the current time.");
			} catch (WrongFieldsForChosenTypeException e) {
				menu.println("The passed fields do not match the selected type");
			}
		}else{
			try {
				dController.getTaskController().createTask(type, taskFields, user, timing);
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
				menu.println("Nice try mate, this call is not allowed on the current state.");
				e.printStackTrace();
			} catch (BusinessRule3Exception e) {
				menu.println("This schedule would violate Business Rule 3. This is probably: starttime after the current time, or the deadline before the current time.");
			} catch (WrongFieldsForChosenTypeException e) {
				menu.println("The passed fields do not match the selected type");
			}
		}
	}
}
