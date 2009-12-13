package gui;

import java.util.ArrayList;

import model.Resource;
import model.Task;
import model.User;
import controller.DispatchController;
import java.util.GregorianCalendar;
import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.EmptyStringException;

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
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.addAll(dController.getTaskController().getTasks(user));
		boolean hasDep = menu.dialogYesNo("I can has Dependency?");
		ArrayList<Task> deps = new ArrayList<Task>();
		ArrayList<String> tDescrS = new ArrayList<String>();
		for (Task t : tasks) {
			tDescrS.add(t.getDescription());
		}
		tDescrS.add("No More");
		int choice = 0;
		while (hasDep && choice>-1) {
			choice = menu.menu("Select dependency", tDescrS);
			if (!(choice == tDescrS.size() - 1)) {
				deps.add(tasks.get(choice));
				tasks.remove(choice);
				tDescrS.remove(choice);
			}else{
				choice = -1;
			}
		}
		boolean hasRes = menu.dialogYesNo("I can has Resource?");
		ArrayList<Resource> res = new ArrayList<Resource>();
		ArrayList<Resource> reqRes = new ArrayList<Resource>();
		res.addAll(dController.getResourceController().getResources());
		ArrayList<String> rDescrS = new ArrayList<String>(tasks.size());
		for (Resource r : res) {
			rDescrS.add(r.getDescription());
		}
		rDescrS.add("No More");
		choice = 0;
		while (hasRes && choice>-1) {
			choice = menu.menu("Select dependancy", rDescrS);
			if (!(choice == rDescrS.size() - 1)) {
				reqRes.add(res.get(choice));
				res.remove(choice);
				rDescrS.remove(choice);
			}else{
				choice = -1;
			}
		}
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
			}
		}
	}
}
