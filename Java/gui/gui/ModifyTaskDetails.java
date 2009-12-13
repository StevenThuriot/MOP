package gui;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import model.Resource;
import model.Task;
import model.User;
import controller.DispatchController;
import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCall;

public class ModifyTaskDetails extends UseCase {
	public ModifyTaskDetails(){}
	
	private ModifyTaskDetails(Menu menu, DispatchController dController, User user) {
		this.menu = menu;
		this.user = user;
		this.dController = dController;
	}

	@Override
	public String getDescription() {
		return "Modify Task Details";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new ModifyTaskDetails(menu,dController,mainGUI.getCurrentUser())).modifyTaskDetails();
	}
	
	public void startUseCase(Menu menu, DispatchController dController, User user,Task task) {
		(new ModifyTaskDetails(menu,dController,user)).modifyTaskDetails(task);
	}
	
	private void modifyTaskDetails(){
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.addAll(dController.getTaskController().getTasks(user));
		ArrayList<String> descr = new ArrayList<String>(tasks.size());
		for (Task t : tasks) {
			descr.add(t.getDescription());
		}
		int choice = menu.menu("Select Task", descr);
		Task task = tasks.get(choice);
		modifyTaskDetails(task);
	}
	
	private void modifyTaskDetails(Task task){
		int choice;
		menu.println(task.getDescription());
		ArrayList<String> descr = new ArrayList<String>();
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
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.addAll(dController.getTaskController().getTasks(user));
		ArrayList<Task> deps = new ArrayList<Task>();
		deps.addAll(dController.getTaskController().getDependencies(task));
		tasks.removeAll(deps);
		ArrayList<Resource> res = new ArrayList<Resource>();
		res.addAll(dController.getResourceController().getResources());
		ArrayList<Resource> req = new ArrayList<Resource>();
		req.addAll(dController.getTaskController().getRequiredResources(task));
		res.removeAll(req);
		descr.clear();
		for(Task t : tasks){
			descr.add(t.getDescription());
		}
		for(Task t : deps){
			descr.add(t.getDescription()+"*");
		}
		menu.printList("Dependable Task, * for already depending", descr);
		descr.clear();
		for(Resource r : res){
			descr.add(r.getDescription());
		}
		for(Resource r : res){
			descr.add(r.getDescription()+"*");
		}
		menu.printList("Available resources, * for already required", descr);
		boolean exit = false;
		int choice2;
		do {
			choice = menu.menu("Select Action", "Add dependency", "Remove dependency", "Add required resource",
					"Change description", "Change schedule", "Return to Menu");
			switch (choice) {
				case 0:
					if (!tasks.isEmpty()) {
						descr.clear();
						for (Task t : tasks) {
							descr.add(t.getDescription());
						}
						choice2 = menu.menu("Select task", descr);
						try {
							dController.getTaskController().addDependency(task, tasks.get(choice2));
							deps.add(tasks.remove(choice2));
						} catch (BusinessRule1Exception e) {
							System.out.println("Dependency would violate BusinessRule1");
							choice2 = menu.menu("Select Action", "Retry", "Abort");
							exit = choice2 == 1;
							continue;
						} catch (DependencyCycleException e) {
							System.out.println("Dependency would cause cycle");
							choice2 = menu.menu("Select Action", "Retry", "Abort");
							exit = choice2 == 1;
							continue;
						} catch (IllegalStateCall e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						System.out.println("No more task to add");
					}
					break;
				case 1:
					if (!deps.isEmpty()) {
						descr.clear();
						for (Task t : deps) {
							descr.add(t.getDescription());
						}
						choice2 = menu.menu("Select task", descr);
						try {
							dController.getTaskController().removeDependency(task, deps.get(choice2));
							tasks.add(deps.remove(choice2));
						} catch (DependencyException e) {
							System.out.println("Dependency does not exist");
							choice2 = menu.menu("Select Action", "Retry", "Abort");
							exit = choice2 == 1;
							continue;
						} catch (IllegalStateCall e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}else{
						System.out.println("No dependencies to remove");
					}
					break;
				case 2:
					if (!res.isEmpty()) {
						descr.clear();
						for (Resource r : res) {
							descr.add(r.getDescription());
						}
						choice2 = menu.menu("Select resource", descr);
						try {
							dController.getTaskController().addRequiredResource(task, res.get(choice2));
						} catch (IllegalStateCall e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						req.add(res.remove(choice2));
					}else{
						System.out.println("No resources to add");
					}
					break;
				case 3:
					try {
						dController.getTaskController().setTaskDescription(task, menu.prompt("Give new task description"));
					} catch (NullPointerException e) {
						System.out.println("Description is null");
						choice2 = menu.menu("Select Action", "Retry", "Abort");
						exit = choice2==1;
						continue;
					} catch (EmptyStringException e) {
						System.out.println("Description is empty");
						choice2 = menu.menu("Select Action", "Retry", "Abort");
						exit = choice2==1;
						continue;
					} catch (IllegalStateCall e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 4:
					GregorianCalendar startDate = menu.promptDate("Give start Date");
					GregorianCalendar dueDate = menu.promptDate("Give due date");
					int duration = Integer.parseInt(menu.prompt("Duration?"));
					try {
						dController.getTaskController().setTaskSchedule(task, startDate, dueDate, duration);
					} catch (BusinessRule1Exception e) {
						System.out.println("New schedule would violate BusinessRule1");
						choice2 = menu.menu("Select Action", "Retry", "Abort");
						exit = choice2==1;
						continue;
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BusinessRule3Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					exit=true;
			}
		}while(!exit);		
	}

}
