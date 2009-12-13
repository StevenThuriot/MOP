package gui;

import java.util.ArrayList;

import model.Resource;
import model.Task;
import model.User;
import controller.DispatchController;
import java.util.GregorianCalendar;
import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyStringException;
import exception.IllegalStateCall;

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
	
	private void createTask(){
		String descr = menu.prompt("Give Task Description");
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
		if (hasDep||hasRes){
			try {
				dController.getTaskController().createTask(descr, startDate, dueDate, duration, deps, reqRes, user);
			} catch (EmptyStringException e) {
				System.out.println("Empty description");
			} catch (BusinessRule1Exception e) {
				System.out.println("Task not completable");
			} catch (DependencyCycleException e) {
				System.out.println("Cyclic Dependency");
			} catch (NullPointerException e) {
				System.out.println("Something very bad has happend");
				e.printStackTrace();
			} catch (IllegalStateCall e) {
				System.out.println("Nice try mate");
				e.printStackTrace();
			} catch (BusinessRule3Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				dController.getTaskController().createTask(descr, startDate, dueDate, duration, user);
			} catch (EmptyStringException e) {
				System.out.println("Empty description");
			} catch (BusinessRule1Exception e) {
				System.out.println("Task not completable");
			} catch (DependencyCycleException e) {
				System.out.println("Cyclic Dependency");
			} catch (NullPointerException e) {
				System.out.println("Something very bad has happend");
				e.printStackTrace();
			} catch (IllegalStateCall e) {
				System.out.println("Nice try mate");
				e.printStackTrace();
			} catch (BusinessRule3Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
