package gui;

import model.Task;
import model.User;

import controller.DispatchController;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.IllegalStateChangeException;

public class UpdateTaskStatus extends UseCase {
	@Override
	public String getDescription() {
		return "Update Task Status";
	}
	
	public UpdateTaskStatus(){}
	
	private UpdateTaskStatus(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new UpdateTaskStatus(menu,dController,mainGUI.getCurrentUser())).updateTaskStatus();
	}
	
	public void startUseCase(Menu menu, DispatchController dController, User user, Task task) {
		(new UpdateTaskStatus(menu,dController,user)).updateTaskStatus(task);
	}
	
	private void updateTaskStatus(){
		updateTaskStatus(menu.menuGen("Select Task", dController.getTaskController().getTasks(user)));
	}

	private void updateTaskStatus(Task task) {
		int choice;
		menu.println(task.getDescription());
		if(!dController.getTaskController().hasDependentTasks(task))
			menu.printListGen("Dependent Tasks", dController.getTaskController().getDependentTasks(task));
		else
			menu.println("Dependent Tasks \n0: None");
		if(!dController.getTaskController().hasDependencies(task))
			menu.printListGen("Dependencies", dController.getTaskController().getDependencies(task));
		else
			menu.println("Dependcies \n0: None");
		if(!dController.getTaskController().hasRequiredResources(task))
			menu.printListGen("Required Resources", dController.getTaskController().getRequiredResources(task));
		else
			menu.println("Required Resources \n 0: None");
		menu.println("Start date: "+menu.format(task.getStartDate()) );
		menu.println("Due date: "+menu.format(task.getDueDate()) );
		menu.println("Duration: "+task.getDuration()+" Minutes");
		if(task.isFailed()||task.isSuccesful()){
			menu.println("Task status is "+task.getCurrentStateName()+" and can not be changed.");
			return;
		}else{
			menu.println("Current Task status is "+(task.canBeExecuted()?"Available.":"Unavailable."));
		}
		choice = menu.menu("Select New Status", "Succesful", "Failed");
		switch (choice) {
			case 0:
				try {
					dController.getTaskController().setSuccessful(task);
				} catch (IllegalStateChangeException e) {
					menu.println("Task can not be completed, god knows why, maybe no resource reservation.");
				} catch (BusinessRule2Exception e) {
					menu.println("Business Rule 2: A task can only be completed succesfully if all dependecies have been completed succesfully as wel.");
					for(Task t: dController.getTaskController().getDependencies(task)){
						if(!t.isSuccesful())
							menu.println(task.getDescription()+" depends on "+t.getDescription()+" which is "+(t.canBeExecuted()?"Available.":"Unavailable."));
					}
				} catch (BusinessRule3Exception e) {
					menu.println("A task can only be completed after the start date.");
				}
				break;
			case 1:
				
				break;
		} 
	}

}
