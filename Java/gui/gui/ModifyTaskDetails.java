package gui;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import model.Field;
import model.Task;
import model.TaskTypeConstraint;
import model.User;
import controller.DispatchController;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyListPassedToMenuException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.WrongFieldsForChosenTypeException;

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
	
	private void modifyTaskDetails()
	{
			try {
				modifyTaskDetails(menu.menuGen("Select Task", dController.getTaskController().getTasks(user)));
			} catch (EmptyListPassedToMenuException e) {
				menu.println("There are no tasks to select. Going back to menu.");
				return;
			}
	}
	
	@SuppressWarnings("unchecked")
	private void modifyTaskDetails(Task task){
		int choice =0;
		menu.println(task.getDescription());
		
		List<Field> fields = task.getFields();
		if (fields.size() > 0)
		{
			menu.println("Fields");
			
			for (int i = 0; i < fields.size(); i++) {
				menu.println(i + ": " + fields.get(i).getName() + " ( " + fields.get(i).getType() + " ): \"" + fields.get(i).getValue() + "\"");
			}
		}
		
		ArrayList<String> descr = new ArrayList<String>();
		if(dController.getTaskController().hasDependentTasks(task))
			menu.printListGen("Dependent Tasks", dController.getTaskController().getDependentTasks(task));
		else
			menu.println("Dependent Tasks \n0: None");
		if(dController.getTaskController().hasDependencies(task))
			menu.printListGen("Dependencies", dController.getTaskController().getDependencies(task));
		else
			menu.println("Dependcies \n0: None");
		if(dController.getTaskController().hasRequiredAssets(task))
			menu.printListGen("Required assets", dController.getTaskController().getRequiredAssets(task));
		else
			menu.println("Required Resources \n 0: None");
		menu.println("Start date: "+menu.format(task.getStartDate()) );
		menu.println("Due date: "+menu.format(task.getDueDate()) );
		menu.println("Duration: "+task.getDuration()+" Minutes");
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.addAll(dController.getTaskController().getAllTasks());
		ArrayList<Task> deps = new ArrayList<Task>();
		deps.addAll(dController.getTaskController().getDependencies(task));
		tasks.removeAll(deps);
		ArrayList<TaskTypeConstraint> req = new ArrayList<TaskTypeConstraint>();
		req.addAll(dController.getTaskController().getRequiredAssets(task));
		descr.clear();
		for(Task t : tasks){
			descr.add(t.getDescription());
		}
		for(Task t : deps){
			descr.add(t.getDescription()+"*");
		}
		menu.printList("Dependable Task, * for already depending", descr);
		descr.clear();
		for(TaskTypeConstraint r : req){
			descr.add(r.getDescription());
		}
		menu.printList("All required Assets", descr);
		boolean exit = false;
		int choice2=0;
		do {
			try {
				choice = menu.menu("Select Action", "Add dependency", "Remove dependency",
						"Change field values", "Change schedule", "Return to Menu");
			} catch (EmptyListPassedToMenuException e2) {
				
			}
			switch (choice) {
				case 0:
					if (!tasks.isEmpty()) {
						descr.clear();
						for (Task t : tasks) {
							descr.add(t.getDescription());
						}
						try {
							choice2 = menu.menu("Select task", descr);
						} catch (EmptyListPassedToMenuException e1) {
						}
						try {
							dController.getTaskController().addDependency(task, tasks.get(choice2));
							deps.add(tasks.remove(choice2));
						} catch (BusinessRule1Exception e) {
							System.out.println("Dependency would violate BusinessRule1");
							try {
								choice2 = menu.menu("Select Action", "Retry", "Abort");
							} catch (EmptyListPassedToMenuException e1) {
								
							}
							exit = choice2 == 1;
							continue;
						} catch (DependencyCycleException e) {
							System.out.println("Dependency would cause cycle");
							try {
								choice2 = menu.menu("Select Action", "Retry", "Abort");
							} catch (EmptyListPassedToMenuException e1) {
							}
							exit = choice2 == 1;
							continue;
						} catch (IllegalStateCallException e) {
							menu.println("The modification is cancelled, an illegal state was reached");
						} catch (BusinessRule2Exception e) {
							System.out.println("Dependency would violate BusinessRule2");
							try {
								choice2 = menu.menu("Select Action", "Retry", "Abort");
							} catch (EmptyListPassedToMenuException e1) {
							}
							exit = choice2 == 1;
							continue;
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
						try {
							choice2 = menu.menu("Select task", descr);
						} catch (EmptyListPassedToMenuException e1) {
						}
						try {
							dController.getTaskController().removeDependency(task, deps.get(choice2));
							tasks.add(deps.remove(choice2));
						} catch (DependencyException e) {
							System.out.println("Dependency does not exist");
							try {
								choice2 = menu.menu("Select Action", "Retry", "Abort");
							} catch (EmptyListPassedToMenuException e1) {
							}
							exit = choice2 == 1;
							continue;
						} catch (IllegalStateCallException e) {
						    menu.println("The modification is cancelled, an illegal state was reached");
						}	
					}else{
						System.out.println("No dependencies to remove");
					}
					break;

				case 2:
					if(menu.dialogYesNo("Would you like to change the Task name?"))
						try {
							task.setDescription(menu.prompt("Please give the new task name"));
						} catch (NullPointerException e1) {
							menu.println("Null was passed to the Task name");
						} catch (EmptyStringException e1) {
							menu.println("You can't give an empty name to a task");
						} catch (IllegalStateCallException e1) {
							menu.println("You can't change the name in the current state");
						}

					for(Field field:fields)
					{
						if(menu.dialogYesNo("Would you like to change " + field.getName() +"?")){
							String value = menu.prompt("Please set the value of " + field.getName());
							switch(field.getType())
							{
								case Numeric:
									field.setValue(Integer.parseInt(value));
									break;
								default:
									field.setValue(value);
							}
						}
					}
					try {
						task.setFields(fields);
					} catch (WrongFieldsForChosenTypeException e2) {
						menu.println("These fields are not correct for this task type");
					} catch (IllegalStateCallException e2) {
						menu.println("You can't change the fields of this task. It is not in the correct state");
					}
					break;
				case 3:
					GregorianCalendar time =  dController.getTimeController().getTime();
					GregorianCalendar startDate = menu.promptDate("Give start Date eg. "+ menu.format(time));
					int duration = Integer.parseInt(menu.prompt("Duration?"));
					time = (GregorianCalendar) time.clone();
					time.add(GregorianCalendar.MINUTE, duration);
					GregorianCalendar dueDate = menu.promptDate("Give due date eg. "+ menu.format(time));
					try {
						dController.getTaskController().setTaskSchedule(task, startDate, dueDate, duration);
					} catch (BusinessRule1Exception e) {
						System.out.println("New schedule would violate BusinessRule1");
						try {
							choice2 = menu.menu("Select Action", "Retry", "Abort");
						} catch (EmptyListPassedToMenuException e1) {
						}
						exit = choice2==1;
						continue;
					} catch (NullPointerException e) {
						menu.println("Something went wrong. Null was passed."); //This should theoretically never happen
					} catch (BusinessRule3Exception e) {
						menu.println("New schedule would violate Business Rule 3. This is probably: starttime after the current time, or the deadline before the current time.");
					}
					break;
				default:
					exit=true;
			}
		}while(!exit);		
	}

}
